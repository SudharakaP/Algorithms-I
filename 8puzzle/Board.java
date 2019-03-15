/* *****************************************************************************
 *  Name: Board.java
 *  Date: 2019-03-14
 *  Description: Designs the Board for the 8 Puzzle.
 **************************************************************************** */

import java.util.Arrays;

public class Board {
    private int[][] blocks;
    private int dimension;
    private int hamming = -1;
    private int manhattan = -1;

    public Board(int[][] blocks) {
        this.blocks = blocks.clone();
        dimension = blocks.length;

        // Hamming Distance
        for (int row = 0; row < dimension; row++)
            for (int col = 0; col < dimension; col++)
                if (blocks[row][col] != 3 * row + col + 1)
                    hamming++;
        if (blocks[dimension - 1][dimension - 1] == 0)
            hamming--;

        // Manhatten Distance
        for (int row = 0; row < dimension; row++)
            for (int col = 0; col < dimension; col++)
                if (blocks[row][col] == 0)
                    manhattan += 2 * (dimension - 1) + row + col;
                else {
                    int rowGoal = blocks[row][col] / dimension;
                    int colGoal = blocks[row][col] % dimension;
                    manhattan += Math.abs(row - rowGoal) + Math.abs(col - colGoal);
                }
    }

    public int dimension() {
        return dimension;
    }

    public int hamming() {
        return hamming;
    }

    public int manhattan() {
        return manhattan;
    }

    public boolean isGoal() {
        return hamming == 0;
    }

    public Board twin() {
        int[][] twinBlock = blocks.clone();
        int swapEle = twinBlock[0][0];
        twinBlock[0][0] = twinBlock[0][1];
        twinBlock[0][1] = swapEle;
        return new Board(twinBlock);
    }

    public boolean equals(Object y) {
        if (y.getClass().getName().equals("Board")) {
            Board board = (Board) y;
            for (int row = 0; row < dimension; row++) {
                for (int col = 0; col < dimension; col++) {
                    if (blocks[row][col] != board.blocks[row][col]) {
                        return false;
                    }
                }
            }
            return true;
        }
        else {
            return false;
        }
    }

    public Iterable<Board> neighbors() {
        Board[] boards = new Board[4];
        int zeroRow = 0, zeroCol = 0;
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                if (blocks[row][col] == 0) {
                    zeroRow = row;
                    zeroCol = col;
                }
            }
        }
        if (zeroCol > 0) {
            Board board1 = new Board(blocks);
            int[][] blocks1 = board1.blocks;
            blocks1[zeroRow][zeroCol] = blocks1[zeroRow][zeroCol - 1];
            blocks1[zeroRow][zeroCol - 1] = 0;
            boards[0] = board1;
        }
        if (zeroCol < dimension - 1) {
            Board board2 = new Board(blocks);
            int[][] blocks2 = board2.blocks;
            blocks2[zeroRow][zeroCol] = blocks2[zeroRow][zeroCol + 1];
            blocks2[zeroRow][zeroCol + 1] = 0;
            boards[1] = board2;
        }
        if (zeroRow > 0) {
            Board board3 = new Board(blocks);
            int[][] blocks3 = board3.blocks;
            blocks3[zeroRow][zeroCol] = blocks3[zeroRow - 1][zeroCol];
            blocks3[zeroRow - 1][zeroCol] = 0;
            boards[2] = board3;
        }
        if (zeroRow < dimension - 1) {
            Board board4 = new Board(blocks);
            int[][] blocks4 = board4.blocks;
            blocks4[zeroRow][zeroCol] = blocks4[zeroRow + 1][zeroCol];
            blocks4[zeroRow + 1][zeroCol] = 0;
            boards[3] = board4;
        }

        return Arrays.asList(boards);
    }

    public String toString() {
        String output = dimension + "";
        for (int row = 0; row < dimension; row++) {
            output = output + "\n";
            for (int col = 0; col < dimension; col++) {
                output = output + blocks[row][col] + " ";
            }
        }
        return output;
    }

    public static void main(String[] args) {


    } // unit tests (not graded)
}
