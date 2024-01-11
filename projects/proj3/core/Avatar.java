package core;

public class Avatar {

    private final int width;
    private final int height;
    private int xPos, yPos;

    public Avatar(int worldWidth, int worldHeight, int x, int y) {
        xPos = x;
        yPos = y;
        width = worldWidth;
        height = worldHeight;

    }

    public int getX() {
        return xPos;
    }

    public int getY() {
        return yPos;
    }

    public void setX(int x) {
        this.xPos = x;
    }

    public void setY(int y) {
        this.yPos = y;
    }

    public boolean canMove(int x, int y) {
        if (inBounds(x, y)) {
            return true;
        }
        return false;
    }

    public boolean inBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
}
