package core;

import tileengine.TETile;
import tileengine.Tileset;

import java.util.List;

public class Room {
    private int xCord;
    private int yCord;
    private int width;
    private int height;

    public Room(int x, int y, int width, int height) {
        this.xCord = x;
        this.yCord = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return xCord;
    }

    public int getY() {
        return yCord;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getCenterX() {
        return xCord + width / 2;
    }

    public int getCenterY() {
        return yCord + height / 2;
    }

    public void connectToHallway(TETile[][] ourWorld, List<Coordinate> hallwayTiles) {
        for (int x = this.xCord - 1; x <= this.xCord + this.width + 1; x++) {
            for (int y = this.yCord - 1; y <= this.yCord + this.height + 1; y++) {
                if (inBounds(x, y) && !hallwayTiles.contains(new Coordinate(x, y))) {
                    if (ourWorld[x][y] == Tileset.FLOOR) {
                        hallwayTiles.add(new Coordinate(x, y));
                    }
                }
            }
        }
    }

    public boolean inBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
}
