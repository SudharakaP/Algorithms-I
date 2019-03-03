/* *****************************************************************************
 *  Name: PercolationStats
 *  Date: 2019-03-01
 *  Description: Print out the Percolation statistics
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final double[] fractionOfOpenSites;
    private final int numTrials;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        fractionOfOpenSites = new double[trials];
        numTrials = trials;
        while (trials > 0) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates())
                percolation.open(StdRandom.uniform(n) + 1, StdRandom.uniform(n) + 1);
            fractionOfOpenSites[trials - 1] = percolation.numberOfOpenSites() / (double) (n * n);
            trials--;
        }
    }

    public double mean() {
        return StdStats.mean(fractionOfOpenSites);
    }

    public double stddev() {
        return StdStats.stddev(fractionOfOpenSites);
    }

    public double confidenceLo() {
        return mean() - (CONFIDENCE_95 * stddev()) / Math.sqrt(numTrials);
    }

    public double confidenceHi() {
        return mean() + (CONFIDENCE_95 * stddev()) / Math.sqrt(numTrials);
    }

    public static void main(String[] args) {
        PercolationStats percolationStats = new PercolationStats(Integer.parseInt(args[0]),
                                                                 Integer.parseInt(args[1]));
        System.out.println("mean = " + percolationStats.mean());
        System.out.println("stddev = " + percolationStats.stddev());
        System.out.println(
                "95% confidence interval = [" + percolationStats.confidenceLo() + ","
                        + percolationStats.confidenceHi() + "]");
    }
}
