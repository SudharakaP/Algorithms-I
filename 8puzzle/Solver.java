/* *****************************************************************************
 *  Name: Solver.java
 *  Date: 2019-03-14
 *  Description: Immutable data type that implements A* algorithm.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Solver {
    private int moves;
    private boolean isSolvable = true;
    private Board[] solution;

    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();
        MinPQ<Board> boardQueue = new MinPQ<>(
                (o1, o2) -> Integer.compare(o1.manhattan(), o2.manhattan()));
        boardQueue.insert(initial);
        Board minPriorityBoard;
        Board predecessor = null;

        solution = new Board[initial.manhattan() + 1];
        int index = 0;
        while (true) {
            if (moves > initial.manhattan()) {
                isSolvable = false;
                moves = -1;
                break;
            }
            minPriorityBoard = boardQueue.delMin();
            solution[index++] = minPriorityBoard;
            if (minPriorityBoard.hamming() == 0)
                break;
            Iterable<Board> neighbours = minPriorityBoard.neighbors();
            for (Board neighbour : neighbours) {
                if (neighbour != null) {
                    if (minPriorityBoard.getPredecessor() == null || !minPriorityBoard
                            .getPredecessor().equals(neighbour)) {
                        boardQueue.insert(neighbour);
                        neighbour.setPredecessor(minPriorityBoard);
                    }
                }
            }
            moves++;
        }
    }           // find a solution to the initial board (using the A* algorithm)

    public boolean isSolvable() {
        return isSolvable;
    }            // is the initial board solvable?

    public int moves() {
        return moves;
    }                    // min number of moves to solve initial board; -1 if unsolvable

    public Iterable<Board> solution() {
        if (!isSolvable())
            return null;
        return Arrays.asList(solution);
    }      // sequence of boards in a shortest solution; null if unsolvable

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    } // solve a slider puzzle (given below)
}
