import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;


public class PercolationStats {
  private final int trials;                  // input arguments
  private final static double confCoefficent = 1.96;          // confidential coefficent for 95%
  private final double mean, stddev;
  public PercolationStats(int n, int trials){    // perform trials independent experiments on an n-by-n grid
    if (n <= 0 || trials <= 0) throw new java.lang.IllegalArgumentException("n <= 0 || trials <= 0");
    this.trials = trials;
    int row, col;
    final int[] factionSet = new int[trials];              // keep total sites for each trial
    while(trials > 0){                            // repeat trials
      Percolation perc = new Percolation(n);
      while(!perc.percolates()){          
        do{                                     // generate random site 
          row = StdRandom.uniform(n)+1;
          col = StdRandom.uniform(n)+1;
        }  while(perc.isOpen(row, col));
        perc.open(row, col);
      }
      trials--;
      factionSet[trials] = perc.numberOfOpenSites();

    }
    mean = StdStats.mean(factionSet)/(n*n);
    stddev = StdStats.stddev(factionSet)/(n*n);
  }
  public double mean(){                            // sample mean of percolation threshold
    return mean;
  }
  public double stddev(){                        // sample standard deviation of percolation threshold
    return stddev;
  }
  public double confidenceLo(){                 // low  endpoint of 95% confidence interval
    return (mean-confCoefficent*stddev/Math.sqrt(trials));
  }
  public double confidenceHi(){                  // high endpoint of 95% confidence interval
    return (mean+confCoefficent*stddev/Math.sqrt(trials));

  }
  public static void main(String[] args){        // test client (described below)
    if (args.length<2){throw new java.lang.IllegalArgumentException("n<=0||trials<=0");}
    int n = Integer.parseInt(args[0]);
    int T = Integer.parseInt(args[1]);
    PercolationStats percStats = new PercolationStats(n, T);
    StdOut.println("mean                    = "+percStats.mean);
    StdOut.println("stddev                  = "+percStats.stddev);
    StdOut.println("95% confidence interval = ["+percStats.confidenceLo()+", "+percStats.confidenceHi()+"]");
  }
}
