/* *****************************************************************************
 *  Name: Percolation
 *  Date: 2019-03-01
 *  Description: Creates the Percolation data type using Weighted Quick Union
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    WeightedQuickUnionUF weightedQuickUnionUF;
    int size;
    int numberOfOpenSites;
    boolean[][] openClose;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        size = n;
        openClose = new boolean[n][n];
        weightedQuickUnionUF = new WeightedQuickUnionUF(n * n + 2);
        for (int i = 1; i <= n; i++)
            weightedQuickUnionUF.union(0, i);
        for (int i = n * n - n; i <= n * n; i++)
            weightedQuickUnionUF.union(i, n * n + 1);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                openClose[i][j] = false;
    }

    public void open(int row, int col) {
        if ((row < 1 || row > size) || (col < 1 || col > size)) {
            throw new IllegalArgumentException();
        }
        if (!isOpen(row, col)) {
            if (col != 1 && isOpen(row, col - 1))
                weightedQuickUnionUF.union(size * (row - 1) + col, size * (row - 1) + col - 1);
            if (col != size && isOpen(row, col + 1))
                weightedQuickUnionUF.union(size * (row - 1) + col, size * (row - 1) + col + 1);
            if (row != 1 && isOpen(row - 1, col))
                weightedQuickUnionUF.union(size * (row - 2) + col, size * (row - 1) + col);
            if (row != size && isOpen(row + 1, col))
                weightedQuickUnionUF.union(size * row + col, size * (row - 1) + col);
            openClose[row - 1][col - 1] = true;
            numberOfOpenSites++;
        }
    }

    public boolean isOpen(int row, int col) {
        if ((row < 1 || row > size) || (col < 1 || col > size)) {
            throw new IllegalArgumentException();
        }

        return openClose[row - 1][col - 1];
    }

    public boolean isFull(int row, int col) {
        if ((row < 1 || row > size) || (col < 1 || col > size)) {
            throw new IllegalArgumentException();
        }
        return weightedQuickUnionUF.connected(0, size * (row - 1) + col);
    }

    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    public boolean percolates() {
        return weightedQuickUnionUF.connected(0, size * size + 1);
    }
}
