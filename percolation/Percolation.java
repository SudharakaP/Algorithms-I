/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    WeightedQuickUnionUF weightedQuickUnionUF = new WeightedQuickUnionUF(n);
    int[][] grid;
    int numberOfOpenSites;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        grid = new int[n][n];
        int count = 1;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; i++)
                grid[i][j] = count++;
    }

    public void open(int row, int col) {
        if ((row <= 1 || row >= grid.length) || (col <= 1 || col >= grid.length)) {
            throw new IllegalArgumentException();
        }
        if (!isOpen(row, col)) {
            grid[row - 1][col - 1] = true;
            numberOfOpenSites++;
        }
    }

    public boolean isOpen(int row, int col) {
        if ((row <= 1 || row >= grid.length) || (col <= 1 || col >= grid.length)) {
            throw new IllegalArgumentException();
        }

        return grid[row - 1][col - 1];
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        if ((row <= 1 || row >= grid.length) || (col <= 1 || col >= grid.length)) {
            throw new IllegalArgumentException();
        }

    }

    // number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return false;
    }

    public static void main(String[] args) {

    }
}
