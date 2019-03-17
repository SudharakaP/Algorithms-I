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
    private SearchNode solution;

    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();
        MinPQ<SearchNode> boardQueue = new MinPQ<>(
                (o1, o2) -> Integer.compare(o1.priority, o2.priority));
        boardQueue.insert(new SearchNode(initial, 0, null));
        MinPQ<SearchNode> boardQueueTwin = new MinPQ<>(
                (o1, o2) -> Integer.compare(o1.priority, o2.priority));
        boardQueueTwin.insert(new SearchNode(initial.twin(), 0, null));

        while (!isSolvable && moves != -1) {
            computeAStar(boardQueue);
            computeAStarTwin(boardQueueTwin);
        }
    }

    private void computeAStar(MinPQ<SearchNode> boardQueue) {
        SearchNode minPriorityBoardNode = boardQueue.delMin();
        SearchNode predecessor = minPriorityBoardNode.prev;

        Board minPriorityBoard = minPriorityBoardNode.board;
        Board predecessorBoard = null;
        if (predecessor != null)
            predecessorBoard = predecessor.board;

        if (minPriorityBoard.isGoal()) {
            isSolvable = true;
            solution = minPriorityBoardNode;
            moves = solution.movesSearch;
            return;
        }
        Iterable<Board> neighbours = minPriorityBoard.neighbors();
        for (Board neighbour : neighbours) {
            if (neighbour != null) {
                if (predecessorBoard == null || !predecessorBoard.equals(neighbour))
                    boardQueue
                            .insert(new SearchNode(neighbour, minPriorityBoardNode.movesSearch + 1,
                                                   minPriorityBoardNode));
            }
        }
    }


    private void computeAStarTwin(MinPQ<SearchNode> boardQueue) {
        SearchNode minPriorityBoardNode = boardQueue.delMin();
        SearchNode predecessor = minPriorityBoardNode.prev;

        Board minPriorityBoard = minPriorityBoardNode.board;
        Board predecessorBoard = null;
        if (predecessor != null)
            predecessorBoard = predecessor.board;

        if (minPriorityBoard.isGoal()) {
            moves = -1;
            return;
        }
        Iterable<Board> neighbours = minPriorityBoard.neighbors();
        for (Board neighbour : neighbours) {
            if (neighbour != null) {
                if (predecessorBoard == null || !predecessorBoard.equals(neighbour))
                    boardQueue
                            .insert(new SearchNode(neighbour, minPriorityBoardNode.movesSearch + 1,
                                                   minPriorityBoardNode));
            }
        }
    }

    private Board[] resizeArray(Board[] board) {
        Board[] newSolution = new Board[board.length * 2];
        for (int i = 0; i < moves; i++) {
            newSolution[i] = board[i];
        }
        return newSolution;
    }

    private class SearchNode {
        private final int priority;
        private final Board board;
        private final SearchNode prev;
        private final int movesSearch;

        public SearchNode(Board board, int movesSearch, SearchNode prev) {
            this.board = board;
            this.movesSearch = movesSearch;
            this.priority = movesSearch + board.manhattan();
            this.prev = prev;
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
        SearchNode solutionNode = solution;
        Board[] nonNullArray = new Board[solutionNode.movesSearch + 1];
        for (int i = solutionNode.movesSearch; i >= 0; i--) {
            nonNullArray[i] = solutionNode.board;
            solutionNode = solutionNode.prev;
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
