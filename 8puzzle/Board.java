/* *****************************************************************************
 *  Name: Board.java
 *  Date: 2019-03-14
 *  Description: Designs the Board for the 8 Puzzle.
 **************************************************************************** */

import java.util.Arrays;

public class Board {
    private final int[][] blocks;
    private final int dimension;
    private int hamming;
    private int manhattan;

    public Board(int[][] blocks) {
        dimension = blocks.length;
        this.blocks = new int[dimension][dimension];

        // Hamming Distance and deep clone
        for (int row = 0; row < dimension; row++)
            for (int col = 0; col < dimension; col++) {
                this.blocks[row][col] = blocks[row][col];
                if (blocks[row][col] != 0 && blocks[row][col] != dimension * row + col + 1)
                    hamming++;
            }

        // Manhatten Distance
        for (int row = 0; row < dimension; row++)
            for (int col = 0; col < dimension; col++)
                if (blocks[row][col] != 0) {
                    int rowGoal = (blocks[row][col] - 1) / dimension;
                    int colGoal = (blocks[row][col] - 1) % dimension;
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
        return manhattan == 0;
    }

    public Board twin() {
        int[][] twinBlock = deepClone(blocks);
        int swapEle = twinBlock[0][0];
        if (swapEle != 0 && twinBlock[0][1] != 0) {
            twinBlock[0][0] = twinBlock[0][1];
            twinBlock[0][1] = swapEle;
            return new Board(twinBlock);
        }
        else {
            swapEle = twinBlock[1][0];
            twinBlock[1][0] = twinBlock[1][1];
            twinBlock[1][1] = swapEle;
            return new Board(twinBlock);
        }
    }

    public boolean equals(Object y) {
        if (y != null && y.getClass().getName().equals("Board")) {
            Board board = (Board) y;
            if (board.dimension != this.dimension)
                return false;
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
        int count = 0;
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
            int[][] blocks1 = deepClone(blocks);
            blocks1[zeroRow][zeroCol] = blocks1[zeroRow][zeroCol - 1];
            blocks1[zeroRow][zeroCol - 1] = 0;
            boards[0] = new Board(blocks1);
            count++;
        }
        if (zeroCol < dimension - 1) {
            int[][] blocks2 = deepClone(blocks);
            blocks2[zeroRow][zeroCol] = blocks2[zeroRow][zeroCol + 1];
            blocks2[zeroRow][zeroCol + 1] = 0;
            boards[1] = new Board(blocks2);
            count++;
        }
        if (zeroRow > 0) {
            int[][] blocks3 = deepClone(blocks);
            blocks3[zeroRow][zeroCol] = blocks3[zeroRow - 1][zeroCol];
            blocks3[zeroRow - 1][zeroCol] = 0;
            boards[2] = new Board(blocks3);
            count++;
        }
        if (zeroRow < dimension - 1) {
            int[][] blocks4 = deepClone(blocks);
            blocks4[zeroRow][zeroCol] = blocks4[zeroRow + 1][zeroCol];
            blocks4[zeroRow + 1][zeroCol] = 0;
            boards[3] = new Board(blocks4);
            count++;
        }

        Board[] boardReturnArray = new Board[count];

        int index = 0;
        for (Board board : boards) {
            if (board != null) {
                boardReturnArray[index] = board;
                index++;
            }
        }

        return Arrays.asList(boardReturnArray);
    }

    private int[][] deepClone(int[][] block) {
        int length = block.length;
        int[][] deepCloneBlock = new int[block.length][block.length];
        for (int row = 0; row < length; row++) {
            for (int col = 0; col < length; col++) {
                deepCloneBlock[row][col] = block[row][col];
            }
        }
        return deepCloneBlock;
    }

    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append(dimension + "");
        for (int row = 0; row < dimension; row++) {
            output.append("\n ");
            for (int col = 0; col < dimension; col++) {
                output.append(blocks[row][col] + " ");
            }
        }
        output.append("\n ");
        return output.toString();
    }

    public static void main(String[] args) {
        int[][] block = { { 1, 3, 2 }, { 4, 0, 7 }, { 9, 6, 2 } };
        Board board = new Board(block);
        Iterable<Board> boards = board.neighbors();
        for (Board x : boards) {
            System.out.println(x);
            System.out.println();
        }
    } // unit tests (not graded)
}
