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
    private boolean isSolvable;
    private final Board[] solution;

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
                moves = -1;
                break;
            }
            minPriorityBoard = boardQueue.delMin();
            solution[index++] = minPriorityBoard;
            if (minPriorityBoard.isGoal()) {
                isSolvable = true;
                break;
            }
            Iterable<Board> neighbours = minPriorityBoard.neighbors();
            for (Board neighbour : neighbours) {
                if (neighbour != null) {
                    if (predecessor == null || !predecessor.equals(neighbour))
                        boardQueue.insert(neighbour);
                }
            }
            predecessor = minPriorityBoard;
            moves++;
        }
    }

    public boolean isSolvable() {
        return isSolvable;
    }

    public int moves() {
        return moves;
    }

    public Iterable<Board> solution() {
        if (!isSolvable())
            return null;
        return Arrays.asList(solution);
    }

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
    }
}
