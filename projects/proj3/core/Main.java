package core;

import tileengine.TERenderer;
import tileengine.TETile;
import edu.princeton.cs.algs4.StdDraw;
import tileengine.Tileset;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;

import java.util.List;
import java.util.ArrayList;

public class Main {

    private static World world;
    private static TERenderer ter;
    private static List<Room> roomList;
    private static List<Coordinate> hallwayList;

    public static void main(String[] args) {
        StdDraw.setCanvasSize(800, 300);
        StdDraw.setXscale(0, 500);
        StdDraw.setYscale(0, 500);
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(250, 400, "CS61B: THE GAME");
        StdDraw.text(250, 200, "New Game (N)");
        StdDraw.text(250, 150, "Load Game (L)");
        StdDraw.text(250, 100, "Quit (Q)");
        StdDraw.show();


        String number = "";

        for (int i = 0; i < args.length; i++) {
            number = number + args[i];
        }

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char character = StdDraw.nextKeyTyped();
                if (character == 'N' || character == 'n') {

                    StdDraw.clear(StdDraw.BLACK);
                    StdDraw.text(250, 250, "Enter Seed: ");
                    StdDraw.show();

                    String seed = "";
                    boolean enterPressed = false;
                    int xPos = 250;

                    while (!enterPressed) {
                        if (StdDraw.hasNextKeyTyped()) {
                            char ch = StdDraw.nextKeyTyped();
                            if (ch == 'S' || ch == 's') {
                                enterPressed = true;
                            } else {
                                seed += ch;
                                StdDraw.text(xPos, 200, String.valueOf(ch));
                                StdDraw.show();
                                xPos += 7;
                            }
                        }
                    }

                    long longSeed = Long.parseLong(seed.trim());
                    System.out.println("longSeed: " + longSeed);
                    world = new World(longSeed);
                    System.out.println("enter while");

                    ter = new TERenderer();
                    ter.initialize(world.getBoardWidth(), world.getBoardHeight());
                    ter.drawTiles(world.getBoard());

                } else if (character == 'L' || character == 'l') {
                    System.out.println("Load World");
                    loadWorld();
                } else if (character == 'Q' || character == 'q') { //quit the game without saving
                    System.exit(0);
                }
            }
        }

    }

    public static void loadWorld() {
        roomList = new ArrayList<>();
        hallwayList = new ArrayList<>();
        ter = new TERenderer();
        String fileName = "saved_game.txt";
        int width = 0;
        int height = 0;

        //@source: ChatGPT, showed how to read into a file user a BufferReader and a try-catch loop
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            Avatar avatar = null;
            String line = br.readLine();

            if (line == null) {
                System.exit(0);
            }
            String[] dimensions = line.split(" ");
            width = Integer.parseInt(dimensions[0]);
            height = Integer.parseInt(dimensions[1]);

            TETile[][] board = new TETile[width][height];

            int y = 0;
            boolean readingRooms = false;
            boolean readingMatrix = false;
            boolean readingHallways = false;
            while ((line = br.readLine()) != null && y < height) {
                System.out.println(line.equals("Room Coordinates:"));
                if (!readingRooms && !readingMatrix && line.equals("Room Coordinates:")) {
                    readingRooms = true;
                } else if (readingRooms && line.equals("Hallway Coordinates:")) {
                    System.out.println("Start reading hallway coordinates");
                    readingRooms = false;
                    readingHallways = true;
                } else if (!readingRooms && readingHallways && line.isEmpty()) {
                    readingHallways = false;
                    readingMatrix = true;
                } else if (!readingRooms && !readingHallways && readingMatrix && line.length() == width) {
                    System.out.println("Getting matrix");
                    for (int x = 0; x < width; x++) {
                        if (line.charAt(x) == '@') {
                            board[x][y] = Tileset.AVATAR;
                            avatar = new Avatar(80, 35, x, y);
                        } else if (line.charAt(x) == '✯') {
                            board[x][y] = Tileset.WALL;
                        } else if (line.charAt(x) == '∙') {
                            board[x][y] = Tileset.FLOOR;
                        } else if (line.charAt(x) == ' ') {
                            board[x][y] = Tileset.NOTHING;
                        } else if (line.charAt(x) == '✧') {
                            board[x][y] = Tileset.LIGHT;
                        } else if (line.charAt(x) == 'º') {
                            board[x][y] = Tileset.GRASS;
                        }
                    }
                    y++;
                } else if (readingRooms && !readingHallways) {
                    System.out.println("Read Room!");
                    String [] roomCords = line.trim().split(" ");
                    if (roomCords.length == 4) {
                        int roomX = Integer.parseInt(roomCords[0]);
                        int roomY = Integer.parseInt(roomCords[1]);
                        int roomWidth = Integer.parseInt(roomCords[2]);
                        int roomHeight = Integer.parseInt(roomCords[3]);

                        Room tempRoom = new Room(roomX, roomY, roomWidth, roomHeight);
                        roomList.add(tempRoom);
                    }
                } else if (readingHallways && !readingRooms) {
                    System.out.println("Reading Hallways!");
                    String [] hallwayCords = line.trim().split(" ");

                    if (hallwayCords.length == 2) {
                        int hallX = Integer.parseInt(hallwayCords[0]);
                        int hallY = Integer.parseInt(hallwayCords[1]);

                        Coordinate tempHallway = new Coordinate(hallX, hallY);
                        hallwayList.add(tempHallway);
                    }
                }
            }

            ter.initialize(board.length, board[0].length);
            ter.renderFrame(board);

            world = new World(board, avatar, roomList, hallwayList);
            world.startGame();

        } catch (IOException e) {
            System.out.println("Error with reading file!");
        }
    }
}
