package game2048;

import java.util.Formatter;


/** The state of a game of 2048.
 *  @author P. N. Hilfinger + Josh Hug
 */
public class Model {
    /** Current contents of the board. */
    public final Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore) {
        board = new Board(rawValues);
        this.score = score;
        this.maxScore = maxScore;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board. */
    public int size() {
        return board.size();
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        board.clear();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        return maxTileExists(board) || !atLeastOneMoveExists(board);
    }

    /** Checks if the game is over and sets the maxScore variable
     *  appropriately.
     */
    private void checkGameOver() {
        if (gameOver()) {
            maxScore = Math.max(score, maxScore);
        }
    }
    
    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
        @author: Shreya Mantripragada
     * */
    public static boolean emptySpaceExists(Board b) {
        // TODO: Fill in this function.

        for (int col = 0; col < b.size(); col += 1) {
            for (int row = 0; row < b.size(); row +=1) {
                if (b.tile(col, row) == null) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by this.MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
        @author: Shreya Mantripragada
     */
    public static boolean maxTileExists(Board b) {
        // TODO: Fill in this function.

        for (int col = 0; col < b.size(); col += 1) {
            for (int row = 0; row < b.size(); row += 1) {
                if (b.tile(col, row) != null && b.tile(col, row).value() == MAX_PIECE) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
        @author: Shreya Mantripragada
     */
    public static boolean atLeastOneMoveExists(Board b) {
        // TODO: Fill in this function.

        if (emptySpaceExists(b)) {
            return true;
        }

        for (int col = 0; col < b.size(); col += 1) {
            for (int row = b.size()-1; row > 0; row -=1) {

                if (b.tile(col, row) != null) {
                    int curr_val = b.tile(col, row).value();
                    if (row == 0 && col == 0) {
                        if (b.tile(col+1, row) == null || b.tile(col, row+1) == null) {
                            return true;
                        }
                        else if (b.tile(col+1, row).value() == curr_val || b.tile(col, row+1).value() == curr_val) {
                            return true;
                        }
                    }
                    else if (row == 0 && col == b.size()-1) {
                        if (b.tile(col-1, row).value() == curr_val || b.tile(col, row+1).value() == curr_val) {
                            return true;
                        }
                    }
                    else if (row == 0) {
                        if (b.tile(col-1, row).value() == curr_val || b.tile(col, row+1).value() == curr_val ||
                                    b.tile(col+1, row).value() == curr_val) {
                            return true;
                        }
                    }
                    else if (row == b.size()-1 && col == 0) {
                        if (b.tile(col, row-1).value() == curr_val || b.tile(col+1, row).value() == curr_val) {
                            return true;
                        }
                    }
                    else if (row == b.size()-1 && col == b.size()-1 && b.tile(col-1, row) != null && b.tile(col, row-1) != null) {
                        if (b.tile(col-1, row).value() == curr_val || b.tile(col, row-1).value() == curr_val) {
                            return true;
                        }
                    }
                    else if (col == 0 && b.tile(col+1, row) != null && b.tile(col, row+1) != null &&
                                        b.tile(col, row-1) !=null) {
                        if (b.tile(col+1, row).value() == curr_val || b.tile(col, row+1).value() == curr_val ||
                                    b.tile(col, row-1).value() == curr_val) {
                            return true;
                        }
                    }
                    else if (col == b.size()-1 && b.tile(col-1, row) != null && b.tile(col, row+1) != null
                                                && b.tile(col, row-1) != null) {
                        if (b.tile(col-1, row).value() == curr_val || b.tile(col, row+1).value() == curr_val ||
                                    b.tile(col, row-1).value() == curr_val) {
                            return true;
                        }
                    }
                    else if (row == b.size()-1 && b.tile(col-1, row) != null && b.tile(col, row-1) != null
                                                && b.tile(col+1, row) != null) {
                        if (b.tile(col-1, row).value() == curr_val || b.tile(col, row-1).value() == curr_val ||
                                    b.tile(col+1, row).value() == curr_val) {
                            return true;
                        }
                    }
                    else {
                        System.out.println("col: " + col + " row: " + row);
                        if (b.tile(col, row-1).value() == curr_val || b.tile(col-1, row).value() == curr_val ||
                                   b.tile(col+1, row).value() == curr_val || b.tile(col, row+1).value() == curr_val) {
                            return true;
                        }
                    }

                }

            }
        }

        return false;
    }

    /** Tilt the board toward SIDE.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
          @author: Shreya Mantripragada
     * */
    public void tilt(Side side) {
        // TODO: Modify this.board (and if applicable, this.score) to account
        // for the tilt to the Side SIDE.

        board.setViewingPerspective(side);

        for (int col = 0; col < board.size(); col += 1) {
            int emptyRow = 0, emptyCol = col;
            for (int row = board.size()-1; row >= 0; row -= 1) {
                 Tile currTile = board.tile(col, row);

                if (valueAtTile(col, row) == 0) {
                    if (row > emptyRow) {
                        emptyRow = row;
                        emptyCol = col;
                    }
                }
                else {
                    if (row != board.size() - 1) {
                        int origRow = row;
                        int origCol = col;

                        if (emptyRow > row && !board.move(emptyCol, emptyRow, currTile)) {
                            if (board.tile(emptyCol, emptyRow-1) == null) {
                                origRow = emptyRow-1;
                            }
                            emptyRow = origRow;
                            emptyCol = origCol;
                        }
                    }
                }
            }
        }

        board.setViewingPerspective(side);
        for (int c = 0; c < board.size(); c++) {
            for (int r = board.size()-1; r > 0; r--){
                System.out.println("curr col: " + c);
                Tile currTile = board.tile(c, r);

                if (currTile != null) {
                    Tile bottTile = board.tile(c, r-1);
                    if (bottTile != null) {
                        if (currTile.value() == board.tile(c, r - 1).value()) {
                            board.move(c, r, bottTile);
                            score += currTile.value();
                            score += bottTile.value();
                        }
                    }

                }

            }
        }

        board.setViewingPerspective(side);
        for (int colVal = 0; colVal < board.size(); colVal += 1) {
            int emptyRow = 0, emptyCol = colVal;
            for (int rowVal = board.size()-1; rowVal >= 0; rowVal -= 1) {
                Tile currTile = board.tile(colVal, rowVal);

                if (valueAtTile(colVal, rowVal) == 0) {
                    if (rowVal > emptyRow) {
                        emptyRow = rowVal;
                        emptyCol = colVal;
                    }
                }
                else {
                    if (rowVal != board.size() - 1) {
                        int origRow = rowVal;
                        int origCol = colVal;

                        if (emptyRow > rowVal && !board.move(emptyCol, emptyRow, currTile)) {
                            if (board.tile(emptyCol, emptyRow-1) == null) {
                                origRow = emptyRow-1;
                            }
                            emptyRow = origRow;
                            emptyCol = origCol;
                        }
                    }
                }
            }
        }

        board.setViewingPerspective(Side.NORTH);

        checkGameOver();
    }

    public int valueAtTile(int colVal, int rowVal) {
        Tile tileVal = board.tile(colVal, rowVal);
        if (tileVal == null) {
            return 0;
        }
        return tileVal.value();
    }


    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
