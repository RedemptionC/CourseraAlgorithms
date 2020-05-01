/* *****************************************************************************
 *  Name:Cheng Geng
 *  Date:2019年12月1日
 *  Description:percolation
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private static final double CONFIDENCECOEFFICIENT = 1.96;
    // private Percolation percolation;
    private int trials;
    private double mean;
    private double stddev;
    private double confidenceLo;
    private double confidenceHi;
    private int n;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n and trials must >= 0");
        }
        this.trials = trials;
        this.n = n;
        monteCarlo();
    }

    private void monteCarlo() {
        boolean p = false;
        double[] sample = new double[trials];

        for (int i = 0; i < trials; i++) {
            // create exactly one Percolation object per trial
            Percolation percolation = new Percolation(n);
            // 产生的点的范围应该在(1,1)~(n,n)
            while (!p) {
                // Returns a random integer uniformly in [0, n).
                // 我需要的是x:1~n
                if (percolation.percolates()) {
                    p = true;
                    break;
                }
                int x = StdRandom.uniform(n) + 1;
                int y = StdRandom.uniform(n) + 1;
                if (percolation.isOpen(x, y)) {
                    continue;
                }
                percolation.open(x, y);
            }
            sample[i] = percolation.numberOfOpenSites() / (double) (n * n);
            // StdOut.println(percolation.numberOfOpenSites()); // 怎么这么多？
            p = false;
        }
        mean = StdStats.mean(sample);
        stddev = StdStats.stddev(sample);
        confidenceLo = mean - CONFIDENCECOEFFICIENT * stddev / Math.sqrt(trials);
        confidenceHi = mean + CONFIDENCECOEFFICIENT * stddev / Math.sqrt(trials);
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return confidenceLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return confidenceHi;
    }

    // test client (see below)
    public static void main(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("Usage: <n> <trials>");
        }
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(n, trials);
        // StdOut.println(percolationStats.mean);
        // StdOut.println(percolationStats.stddev);
        StdOut.println(percolationStats.confidenceLo());
        StdOut.println(percolationStats.confidenceHi());
        StdOut.printf("mean\t = %f\n", percolationStats.mean());
        StdOut.printf("stddev\t = %f\n", percolationStats.stddev());
        StdOut.printf("95% confidence interval\t = [%f, %f]\n", percolationStats.confidenceLo(),
                      percolationStats.confidenceHi());


    }

}
