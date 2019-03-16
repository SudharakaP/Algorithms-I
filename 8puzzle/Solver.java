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

    private int movesTwin;
    private final Board[] solutionTwin;

    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();
        moves = 0;
        movesTwin = 0;
        MinPQ<SearchNode> boardQueue = new MinPQ<>(
                (o1, o2) -> Integer.compare(o1.priority, o2.priority));
        boardQueue.insert(new SearchNode(initial, moves));
        MinPQ<SearchNode> boardQueueTwin = new MinPQ<>(
                (o1, o2) -> Integer.compare(o1.priority, o2.priority));
        boardQueueTwin.insert(new SearchNode(initial.twin(), moves));
        Board predecessor = null;
        Board predecessorTwin = null;

        solution = new Board[1000];
        solutionTwin = new Board[1000];
        int index = 0;
        do {
            predecessor = computeAStar(boardQueue, predecessor);
            predecessorTwin = computeAStarTwin(boardQueueTwin, predecessorTwin);
        } while (!predecessor.isGoal() && !predecessorTwin.isGoal());

        if (!predecessor.isGoal()) {
            moves = -1;
        }
    }

    private Board computeAStar(MinPQ<SearchNode> boardQueue, Board predecessor) {
        SearchNode minPriorityBoardNode = boardQueue.delMin();
        while (minPriorityBoardNode.moves != moves)
            minPriorityBoardNode = boardQueue.delMin();
        Board minPriorityBoard = minPriorityBoardNode.board;
        solution[moves] = minPriorityBoard;

        if (minPriorityBoard.isGoal()) {
            isSolvable = true;
            return minPriorityBoard;
        }
        Iterable<Board> neighbours = minPriorityBoard.neighbors();
        moves++;
        for (Board neighbour : neighbours) {
            if (neighbour != null) {
                if (predecessor == null || !predecessor.equals(neighbour))
                    boardQueue.insert(new SearchNode(neighbour, moves));
            }
        }
        return minPriorityBoard;
    }


    private Board computeAStarTwin(MinPQ<SearchNode> boardQueue, Board predecessor) {
        SearchNode minPriorityBoardNode = boardQueue.delMin();
        while (minPriorityBoardNode.moves != movesTwin)
            minPriorityBoardNode = boardQueue.delMin();
        Board minPriorityBoard = minPriorityBoardNode.board;
        solutionTwin[movesTwin] = minPriorityBoard;

        if (minPriorityBoard.isGoal()) {
            return minPriorityBoard;
        }
        Iterable<Board> neighbours = minPriorityBoard.neighbors();
        movesTwin++;
        for (Board neighbour : neighbours) {
            if (neighbour != null) {
                if (predecessor == null || !predecessor.equals(neighbour))
                    boardQueue.insert(new SearchNode(neighbour, movesTwin));
            }
        }
        return minPriorityBoard;
    }

    private class SearchNode {
        private int priority;
        private Board board;
        private int moves;

        public SearchNode(Board board, int moves) {
            this.board = board;
            this.moves = moves;
            this.priority = moves + board.manhattan();
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
        Board[] nonNullArray = new Board[moves + 1];
        for (int i = 0; i < moves + 1; i++) {
            if (solution[i] != null)
                nonNullArray[i] = solution[i];
        }
        return Arrays.asList(nonNullArray);
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
