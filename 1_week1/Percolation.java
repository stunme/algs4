import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
  private final WeightedQuickUnionUF percolationSet, noBottom;     // create two UF objects, noBottom used for avoid back water 
  private final int n, nN;            // grid size and UF size(with top and bottom) 
  private boolean[] setsOpen;    // record open boolean for each site
  private int factionOpened = 0;    // record opened sites
  public Percolation(int n){              // create n-by-n grid, with all sites blocked
       if ( n <= 0 ) throw new IllegalArgumentException( "n <= 0, th" );
       nN = n*n+2;
      percolationSet = new WeightedQuickUnionUF( nN );  
      noBottom = new WeightedQuickUnionUF( nN-1 );  
      setsOpen = new boolean[nN];
      setsOpen[0] = true;
      setsOpen[nN-1] = true;
      this.n = n;                        // inital all arguments
  }
  public    void open(int row, int col){    // open site (row, col) if it is not open already
    if ( row <= 0 || row > n ) throw new IllegalArgumentException( "row index row out of bounds" );
    if ( col <= 0 || col > n ) throw new IllegalArgumentException( "col index col out of bounds" );
    if ( !isOpen( row, col ) ){
      int idx = (row-1)*n+col;
      setsOpen[idx] = true;
      factionOpened++;
      if (n == 1){                      // deal with grid in size 1
        unionOpen(0, idx);
        percolationSet.union(idx, nN-1);
      }else {                                                  
        if (col > 1 && isOpen(row, col-1)) unionOpen(idx, idx-1);        
        if (col < n && isOpen(row, col+1)) unionOpen(idx, idx+1);
        if (row == 1) {                                  // top row
          unionOpen(idx, 0);
          if (isOpen(2, col))  unionOpen(idx, idx+n);
        }
        else if (row == n){                              // bottom row
          percolationSet.union(idx, nN-1);
          if (isOpen(row-1, col)) unionOpen(idx, idx-n);
        }else{
          if (isOpen(row-1, col))  unionOpen(idx, idx-n);
          if (isOpen(row+1, col)) unionOpen(idx, idx+n);  
        }
      }
    }
  }
  
  private void unionOpen(int p, int q){      // union two UF objects
    percolationSet.union(p, q);
    noBottom.union(p, q);
  }
  
  public boolean isOpen(int row, int col)  // is site (row, col) open?
  {
    if (row <= 0 || row > n) throw new IllegalArgumentException("row index row out of bounds");
    if (col <= 0 || col > n) throw new IllegalArgumentException("col index col out of bounds");
    return setsOpen[(row-1)*(n)+col];
    
  }
  public boolean isFull(int row, int col)  // is site (row, col) full?
  {
    if (row <= 0 || row > n) throw new IllegalArgumentException("row index row out of bounds");
     if (col <= 0 || col > n) throw new IllegalArgumentException("col index col out of bounds");
    return noBottom.connected(0, (row-1)*(n)+col);
  }
  public     int numberOfOpenSites(){       // number of open sites
    return factionOpened;
  }
  public boolean percolates(){              // does the system percolate?
    return percolationSet.connected(0, nN-1);
  }

  public static void main(String[] args){   // test client (optional)
 
  }
}
