package core;

import edu.princeton.cs.algs4.StdDraw;
import tileengine.TERenderer;
import tileengine.TETile;
import tileengine.Tileset;
import utils.RandomUtils;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class World {
    private TETile[][] board;
    private boolean[][] occupied;
    private Random random;

    private Avatar avatar;

    private static final int WIDTH = 80;
    private static final int HEIGHT = 35;

    private List<Room> rooms;
    private List<Coordinate> hallwayTiles;
    private List<Coordinate> floorPositions;

    private TERenderer renderer;
    private String charMovements;

    private boolean colonPressed;

    public World(long seed) {
        colonPressed = false;
        floorPositions = new ArrayList<>();
        rooms = new ArrayList<>();
        board = new TETile[WIDTH][HEIGHT];
        occupied = new boolean[WIDTH][HEIGHT];

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                board[i][j] = Tileset.NOTHING;
                occupied[i][j] = false;
            }
        }

        random = new Random(seed);
        renderer = new TERenderer();
        renderer.initialize(WIDTH, HEIGHT);
        generateRooms();
        generateHallways();
        initialDisplay();
        setupTimeDisplay();
        ensureLightInEveryRoom();

        int randomFloor = RandomUtils.uniform(random, floorPositions.size());
        Coordinate getRandomFloor = floorPositions.get(randomFloor);

        avatar = new Avatar(WIDTH, HEIGHT, getRandomFloor.getX(), getRandomFloor.getY());
        board[avatar.getX()][avatar.getY()] = Tileset.AVATAR;

        renderer.renderFrame(board);
        initialDisplay();
        startGame();
    }

    public World(long seed, String movements) {
        charMovements = movements;
        floorPositions = new ArrayList<>();
        rooms = new ArrayList<>();
        board = new TETile[WIDTH][HEIGHT];
        occupied = new boolean[WIDTH][HEIGHT];

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                board[i][j] = Tileset.NOTHING;
                occupied[i][j] = false;
            }
        }

        random = new Random(seed);

        generateRooms();
        generateHallways();

        int randomFloor = RandomUtils.uniform(random, floorPositions.size());
        Coordinate getRandomFloor = floorPositions.get(randomFloor);

        avatar = new Avatar(WIDTH, HEIGHT, getRandomFloor.getX(), getRandomFloor.getY());
        board[avatar.getX()][avatar.getY()] = Tileset.AVATAR;

        startAvatarMovements();
    }

    public World(TETile[][] board, Avatar avatar) {
        renderer = new TERenderer();
        renderer.initialize(WIDTH, HEIGHT);
        this.board = board;
        this.avatar = avatar;

        renderer.renderFrame(board);
    }

    public World (TETile[][] board, Avatar avatar, List<Room> roomList, List<Coordinate> hallwayList) {
        renderer = new TERenderer();
        renderer.initialize(WIDTH, HEIGHT);
        this.board = board;
        this.avatar = avatar;
        this.rooms = roomList;
        this.hallwayTiles = hallwayList;

        renderer.renderFrame(board);
    }

    public World(String movements, TETile[][] board, Avatar avatar) {
        this.board = board;
        this.avatar = avatar;
        charMovements = movements;
        startAvatarMovements();
    }

    public TETile[][] getBoard() {
        return board;
    }

    public int getBoardWidth() {
        return WIDTH;
    }

    public int getBoardHeight() {
        return HEIGHT;
    }

    public void generateRooms() {
        int numRooms = RandomUtils.uniform(random, 30, 40);

        for (int i = 0; i < numRooms; i++) {
            int width = RandomUtils.uniform(random, 5, 12);
            int height = RandomUtils.uniform(random, 5, 12);
            int x = RandomUtils.uniform(random, 1, WIDTH - width - 1);
            int y = RandomUtils.uniform(random, 1, HEIGHT - height - 5);

            Room newRoom = new Room(x, y, width, height);

            if (checkValid(newRoom)) {
                createRoom(newRoom);
                rooms.add(newRoom);
                addLightToRoom(newRoom);
            }
        }
    }

    public void createRoom(Room room) {
        for (int x = room.getX(); x < room.getX() + room.getWidth(); x++) {
            for (int y = room.getY(); y < room.getY() + room.getHeight(); y++) {
                if (inBounds(x, y)) {
                    board[x][y] = Tileset.FLOOR;
                    occupied[x][y] = true;
                    floorPositions.add(new Coordinate(x, y));
                }
            }
        }

        // Add walls around the room
        for (int x = room.getX() - 1; x <= room.getX() + room.getWidth(); x++) {
            for (int y = room.getY() - 1; y <= room.getY() + room.getHeight(); y++) {
                if (inBounds(x, y) && board[x][y] != Tileset.FLOOR) {
                    board[x][y] = Tileset.WALL;
                    occupied[x][y] = true;
                }
            }
        }
    }

    public boolean checkValid(Room room) {
        for (int x = room.getX() - 1; x <= room.getX() + room.getWidth() + 1; x++) {
            for (int y = room.getY() - 1; y <= room.getY() + room.getHeight() + 1; y++) {
                if (inBounds(x, y) && occupied[x][y]) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean inBounds(int x, int y) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
    }

    public void generateHallways() {
        hallwayTiles = new ArrayList<>();
        RoomComparator comp = new RoomComparator();
        rooms.sort(comp);

        for (int i = 0; i < rooms.size() - 1; i++) {
            Room currentRoom = rooms.get(i);
            Room nextRoom = rooms.get(i + 1);

            int startX = currentRoom.getCenterX();
            int startY = currentRoom.getCenterY();
            int endX = nextRoom.getCenterX();
            int endY = nextRoom.getCenterY();

            generateHallway(startX, startY, endX, endY);
        }

        // Connect rooms to hallways
        for (Room room : rooms) {
            room.connectToHallway(board, hallwayTiles);
        }

        // Fill hallway tiles
        for (Coordinate tile : hallwayTiles) {
            int x = tile.getX();
            int y = tile.getY();
            board[x][y] = Tileset.FLOOR;
        }

        // Add walls around hallways
        addWallsAroundHallways(board, hallwayTiles);
    }

    public void generateHallway(int startX, int startY, int endX, int endY) {
        int x = startX;
        int y = startY;

        // Choose to move horizontally or vertically first
        boolean moveHorizontallyFirst = random.nextBoolean();

        if (moveHorizontallyFirst) {
            // Move horizontally
            while (x != endX) {
                hallwayTiles.add(new Coordinate(x, y));
                x += Integer.compare(endX, x);
            }
            // Then move vertically
            while (y != endY) {
                hallwayTiles.add(new Coordinate(x, y));
                y += Integer.compare(endY, y);
            }
        } else {
            // Move vertically
            while (y != endY) {
                hallwayTiles.add(new Coordinate(x, y));
                y += Integer.compare(endY, y);
            }
            // Then move horizontally
            while (x != endX) {
                hallwayTiles.add(new Coordinate(x, y));
                x += Integer.compare(endX, x);
            }
        }
    }

    public void addWallsAroundHallways(TETile[][] ourWorld, List<Coordinate> hallTiles) {
        for (Coordinate tile : hallTiles) {
            int x = tile.getX();
            int y = tile.getY();

            for (int i = x - 1; i <= x + 1; i++) {
                for (int j = y - 1; j <= y + 1; j++) {
                    if (inBounds(i, j) && ourWorld[i][j] == Tileset.NOTHING) {
                        ourWorld[i][j] = Tileset.WALL;
                    }
                }
            }
        }
    }

    public void startGame() {
        while (true) {
            StdDraw.clear(StdDraw.BLACK);
            renderer.drawTiles(getBoard());
            setupTimeDisplay();
            double mouseX = StdDraw.mouseX();
            double mouseY = StdDraw.mouseY();

            if (mouseX >= 0 && mouseX < getBoardWidth() && mouseY >= 0 && mouseY < getBoardHeight()) {
                TETile hoveredTile = getBoard()[(int) mouseX][(int) mouseY];
                displayTileType(hoveredTile);// Display the hoverboard information
            } else {
                initialDisplay();
            }

            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                if ((key == 'M' || key == 'm') && mouseX >= 0 && mouseX < getBoardWidth() && mouseY >= 0 && mouseY < getBoardHeight()) {
                    System.out.println("entered the M button");
                    int x = (int) mouseX;
                    int y = (int) mouseY;
                    TETile hoveredTile = getBoard()[x][y];
                    if (hoveredTile == Tileset.LIGHT) {
                        turnOnOff(x, y);
                    }
                }
                else {
                    checkMovement(key);
                }

            }
            StdDraw.show();
        }
    }

    public void startAvatarMovements() {
        moveAvatarWithCommands(charMovements);
    }

    public void checkMovement(char inputKey) {
        int newX = avatar.getX();
        int newY = avatar.getY();

        boolean validMove = false;

        if (inputKey == 'W' || inputKey == 'w') {
            newY += 1;
            validMove = true;
        } else if (inputKey == 'S' || inputKey == 's') {
            newY -= 1;
            validMove = true;
        } else if (inputKey == 'D' || inputKey == 'd') {
            newX += 1;
            validMove = true;
        } else if (inputKey == 'A' || inputKey == 'a') {
            newX -= 1;
            validMove = true;
        } else if (inputKey == ':') {
            colonPressed = true;
        } else if (colonPressed && (inputKey == 'q' || inputKey == 'Q')) {
            saveGameToFile(); //save the rooms too in this file
            System.exit(0);
        }
        int oldX = avatar.getX();
        int oldY = avatar.getY();
        //previous loc
        if (validMove && avatar.canMove(newX, newY) && board[newX][newY] != Tileset.WALL && board[newX][newY] != Tileset.LIGHT) {
            Room currentRoom = findRoomWithLightSwitch(oldX, oldY);
            boolean isCurrentRoomDark = (currentRoom != null) && isRoomDark(currentRoom);

            Room newRoom = findRoomWithLightSwitch(newX, newY);
            boolean isNewRoomDark = (newRoom != null) && isRoomDark(newRoom);

            avatar.setX(newX);
            avatar.setY(newY);
            board[newX][newY] = Tileset.AVATAR;

            if (isCurrentRoomDark && isNewRoomDark) {
                if (board[oldX][oldY] != Tileset.GRASS) {
                    board[oldX][oldY] = Tileset.GRASS;
                }
            } else if (!isCurrentRoomDark && !isNewRoomDark) {
                board[oldX][oldY] = Tileset.FLOOR;
            } else if (isCurrentRoomDark && !isNewRoomDark) {
                board[oldX][oldY] = Tileset.GRASS;
            } else {
                board[oldX][oldY] = Tileset.FLOOR;
            }
        }
    }

    private boolean isRoomDark(Room room) {
        for (int x = room.getX(); x < room.getX() + room.getWidth(); x++) {
            for (int y = room.getY(); y < room.getY() + room.getHeight(); y++) {
                if (board[x][y] == Tileset.GRASS) {
                    return true;
                }
            }
        }
        return false;
    }


    public void saveGameToFile() {
        try {
            FileWriter fileWriter = new FileWriter("saved_game.txt");
            PrintWriter printWriter = new PrintWriter(fileWriter);

            printWriter.println(WIDTH + " " + HEIGHT);
            printWriter.println("Room Coordinates:");

            for (Room room : rooms) {
                printWriter.println(room.getX() + " " + room.getY() + " " + room.getWidth() + " " + room.getHeight());
            }
            printWriter.println("Hallway Coordinates:");

            for (Coordinate hallway: hallwayTiles) {
                printWriter.println(hallway.getX() + " " + hallway.getY());
            }
            printWriter.println();

            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    printWriter.print(board[x][y].character());
                }
                printWriter.println();
            }

            printWriter.close();
        } catch (IOException e) {
            System.out.println("Error with writing to the file");
        }
    }

    public void moveAvatarWithCommands(String movement) {
        for (int i = 0; i < movement.length(); i++) {
            char command = movement.charAt(i);
            int newX = avatar.getX();
            int newY = avatar.getY();

            if (command == 'W' || command == 'w') {
                newY += 1;
            } else if (command == 'S' || command == 's') {
                newY -= 1;
            } else if (command == 'D' || command == 'd') {
                newX += 1;
            } else if (command == 'A' || command == 'a') {
                newX -= 1;
            } else if (command == ':') {
                colonPressed = true;
            } else if (colonPressed && (command == 'q' || command == 'Q')) {
                saveGameToFile();
            }

            if (avatar.canMove(newX, newY) && board[newX][newY] != Tileset.WALL && board[newX][newY] != Tileset.LIGHT) {
                board[avatar.getX()][avatar.getY()] = Tileset.FLOOR;
                avatar.setX(newX);
                avatar.setY(newY);
                board[newX][newY] = Tileset.AVATAR;
            }
        }
    }

    public void displayTileType(TETile tile) {
        int rectX = 10;
        int rectY = HEIGHT - 1;
        int rectWidth = 10;
        int rectHeight = 2;

        StdDraw.setPenColor(Color.decode("#2d4286"));
        StdDraw.filledRectangle(rectX, rectY, rectWidth, rectHeight);

        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.rectangle(rectX, rectY, rectWidth, rectHeight);

        StdDraw.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        String tileType = getTileTypeString(tile);
        StdDraw.text(rectX, rectY + 0.20, "Tile: " + tileType);  // Position slightly higher
        StdDraw.text(rectX, rectY - 0.95, "Hover over Light Switch & Press 'M' for light");  // Position slightly lower
    }

    public void initialDisplay() {
        int rectX = 10;
        int rectY = HEIGHT - 1;
        int rectWidth = 10;
        int rectHeight = 2;

        // Clear the area where the rectangle will be drawn
        StdDraw.setPenColor(Color.decode("#2d4286"));
        StdDraw.filledRectangle(rectX, rectY, rectWidth, rectHeight);

        // Draw the rectangle
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.rectangle(rectX, rectY, rectWidth, rectHeight);

        // Set font and draw the text
        StdDraw.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        StdDraw.text(rectX, rectY + 0.20, "Tile: None");
        StdDraw.text(rectX, rectY - 0.95, "Hover over Light Switch & Press 'M' for light");

    }

    private String getTileTypeString(TETile tile) {
        if (tile == Tileset.WALL) {
            return "Wall";
        } else if (tile == Tileset.FLOOR) {
            return "Room Floor";
        } else if (tile == Tileset.LIGHT) {
            return "Light Switch";
        } else if (tile == Tileset.GRASS) {
            return "Light On Room Floor";
        } else if (tile == Tileset.AVATAR) {
            return "Avatar";
        }else {
            return "Board";
        }
    }

    public void addLightToRoom(Room room) {
        Random lightrand = new Random();
        if (room.getWidth() > 2 && room.getHeight() > 2) {
            int lightX = RandomUtils.uniform(lightrand, room.getX() + 1, room.getX() + room.getWidth() - 1);
            int lightY = RandomUtils.uniform(lightrand, room.getY() + 1, room.getY() + room.getHeight() - 1);

            // Put switch
            if (board[lightX][lightY] == Tileset.FLOOR) {
                board[lightX][lightY] = Tileset.LIGHT;
            }
        }
    }

    public void ensureLightInEveryRoom() {
        for (Room room : rooms) {
            if (!hasLightSwitch(room)) {
                addRandomLightSwitchToRoom(room);
            }
        }
    }
    //only used if there is a fault in previous, if there is no light switch present
    private void addRandomLightSwitchToRoom(Room room) {
        int lightX, lightY;
        do {
            lightX = RandomUtils.uniform(random, room.getX() + 1, room.getX() + room.getWidth() - 1);
            lightY = RandomUtils.uniform(random, room.getY() + 1, room.getY() + room.getHeight() - 1);
        } while (board[lightX][lightY] != Tileset.FLOOR); //if it's not a floor tile
        board[lightX][lightY] = Tileset.LIGHT;
    }

    private boolean hasLightSwitch(Room room) {
        for (int x = room.getX(); x < room.getX() + room.getWidth(); x++) {
            for (int y = room.getY(); y < room.getY() + room.getHeight(); y++) {
                if (board[x][y] == Tileset.LIGHT) {
                    return true; //light in room
                }
            }
        }
        return false;
    }

    private void changeAdjacentTiles(int x, int y) {
        Room room = findRoomWithLightSwitch(x, y);

        if (room != null) {
            System.out.println("room isn't null");
            for (int i = room.getX(); i < room.getX() + room.getWidth(); i++) {
                for (int j = room.getY(); j < room.getY() + room.getHeight(); j++) {
                    if (x == i && y == j) {
                        continue;
                    }

                    if (board[i][j] == Tileset.FLOOR) {
                        board[i][j] = Tileset.GRASS;
                    } else if (board[i][j] == Tileset.GRASS) {
                        board[i][j] = Tileset.FLOOR;
                    }
                }
            }
        }
    }



    private boolean isHallwayTileAvatar(int x, int y) {
        for (Coordinate c : hallwayTiles) {
            if (c.getX() == x && c.getY() == y) {
                return true;
            }
        }
        return false;
    }

    private Room findRoomWithLightSwitch(int x, int y) {
        System.out.println("rooms size: " + rooms.size());
        for (Room room : rooms) {
            System.out.println(room.getX() + " " + room.getY() + " " + room.getWidth() + " " + room.getHeight());
            if (x >= room.getX() && x < room.getX() + room.getWidth() &&
                    y >= room.getY() && y < room.getY() + room.getHeight()) {
                return room;
            }
        }
        return null;
    }
    public void turnOnOff(int x, int y) {
        if (board[x][y] == Tileset.LIGHT) {
            boolean isLightOn = (board[x][y] == Tileset.LIGHT);
            System.out.println("hovering over flower");
            changeAdjacentTiles(x, y);
            // state of the light switch
            if (isLightOn) {
                System.out.println("Light On");
                board[x][y] = Tileset.LIGHT;
            } else {
                System.out.println("LightOff");
                board[x][y] = Tileset.GRASS;
                renderer.renderFrame(board);
            }
        }
    }
    //3c secondary @source: Used chatGPT to figure out how to import date and time
    private String getCurrentDateTime() {
        // Get the current date and time in the desired format
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return currentDateTime.format(formatter);
    }

    // Updated time and date display
    void setupTimeDisplay() {
        int timeBoxX = WIDTH - 25;
        int timeBoxY = HEIGHT;
        int timeBoxWidth = 25;
        int timeBoxHeight = 2;

        StdDraw.setPenColor(Color.decode("#2d4286"));
        StdDraw.filledRectangle(timeBoxX + timeBoxWidth / 2.0, timeBoxY - timeBoxHeight / 2.0, timeBoxWidth / 2.0, timeBoxHeight / 2.0);

        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.rectangle(timeBoxX + timeBoxWidth / 2.0, timeBoxY - timeBoxHeight / 2.0, timeBoxWidth / 2.0, timeBoxHeight / 2.0);

        StdDraw.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        String currentDateTime = getCurrentDateTime(); // Get both date and time
        StdDraw.text(timeBoxX + timeBoxWidth / 2.0, timeBoxY - 1, "Date & Time: " + currentDateTime);

    }
}
