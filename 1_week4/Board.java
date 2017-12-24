import edu.princeton.cs.algs4.*;
import java.util.LinkedList;
import java.util.Iterator;



public class Board{
    private final int n;
    private final int[][] blocks;
    private final int[][] goal;
    private boolean isGoal = true;
    private int hamming = 0;
    private int manhattan = 0;
    private int iz, jz;
    
    public Board(int[][] blocks) {           // construct a board from an n-by-n array of blocks
                                           // (where blocks[i][j] = block in row i, column j)
        n = blocks.length;
        this.blocks = new int[n][n];
        goal = new int[n][n];
        iz = n-1;
        jz = iz; 
        if ( n == 0 ){
            isGoal = true;
            hamming = 0;
            manhattan = 0;
            return;
        }
        int max = n*n;
        for ( int i = 0; i < n; i ++ ){
            for ( int j = 0; j < n; j ++ ){
                this.blocks[i][j] = blocks[i][j];
                goal[i][j] = i * n + j + 1;
                if(goal[i][j] == max) goal[i][j] = 0;
                
                if (this.blocks[i][j] != 0){             
                    int dis = (Math.abs((this.blocks[i][j] -1) / n - i) + Math.abs((this.blocks[i][j] -1) % n -j));
                    manhattan += dis;                       // determine manhattan    
                    if (dis > 0) hamming ++;
                }else{
                    iz = i;
                    jz = j;
                }

                if ( isGoal && blocks[i][j] != goal[i][j] ) isGoal = false;                                 // determine isGoal
            }
        }
    }
                                                                                   
    public int dimension(){                 // board dimension n
        return n;
    }
    
    public int hamming(){                   // number of blocks out of place
        return hamming;
    }
    
    public int manhattan(){                 // sum of Manhattan distances between blocks and goal
        return manhattan;
    }
    public boolean isGoal(){                // is this board the goal board?
        return isGoal;
    }
    
    public Board twin(){                    // a board that is obtained by exchanging any pair of blocks
        
        int[][] temp = new int[n][n];
        
        if( n > 0) {
            for (int i = 0; i < n ; i ++){
                for (int j = 0; j < n ; j ++){
                    temp[i][j] = blocks[i][j]; 
                }
            }      
        }
        
        if ( n > 1){
            int zero = iz * n + jz;
            int a = zero, b = zero;
            while ( a == zero ) a = StdRandom.uniform( n * n );
            while ( b == zero || b == a ) b = StdRandom.uniform( n * n );
            int ai = a / n, aj = a % n, bi = b / n, bj = b % n;            
            int k = temp[ai][aj];
            temp[ai][aj] = temp[bi][bj];
            temp[bi][bj] = k;
        }
        return new Board(temp);
    }
    
    public boolean equals(Object y){        // does this board equal y?
        if ( y == null) return false;
        if ( y == this) return true;
        if (y.getClass() != this.getClass()) return false;
        Board x = (Board)y;
        if (x.n != n ) return false;
        
        for (int i = 0; i < n ; i ++){
            for (int j = 0; j < n ; j ++){
                if (x.blocks[i][j] != blocks[i][j]) return false; 
            }
        }
        return true;
    }
    
    public Iterable<Board> neighbors(){     // all neighboring boards
        LinkedList<Board> res = new LinkedList<>();
        int[][] temp = new int[n][n];
        
        if( n > 1) {
            for (int i = 0; i < n ; i ++){
                for (int j = 0; j < n ; j ++){
                    temp[i][j] = blocks[i][j]; 
                }
            }      
        } 
        
        if ( iz > 0 ){
            temp[iz][jz] = temp[iz-1][jz] ;
            temp[iz-1][jz] = 0;
            res.add(new Board(temp));
            temp[iz-1][jz] = temp[iz][jz] ;
            temp[iz][jz] = 0;
        }
        
        if ( jz > 0){
            temp[iz][jz] = temp[iz][jz-1] ;
            temp[iz][jz-1] = 0;
            res.add(new Board(temp));
            temp[iz][jz-1] = temp[iz][jz] ;
            temp[iz][jz] = 0;
        }
        
        if ( iz < n-1 ){
            temp[iz][jz] = temp[iz+1][jz] ;
            temp[iz+1][jz] = 0;
            res.add(new Board(temp));
            temp[iz+1][jz] = temp[iz][jz] ;
            temp[iz][jz] = 0;
        }
        
        if ( jz < n-1 ){
            temp[iz][jz] = temp[iz][jz+1] ;
            temp[iz][jz+1] = 0;
            res.add(new Board(temp));
            temp[iz][jz+1] = temp[iz][jz] ;
            temp[iz][jz] = 0;
        }
        return res;
    }   
    
    public String toString(){               // string representation of this board (in the output format specified below)
        StringBuffer s = new StringBuffer();
        s.append(n + "\n");
        for (int i = 0; i < n; i ++ ){
            for (int j = 0; j < n ; j ++){
                s.append(" " + blocks[i][j] + " ");
            }
            s.append("\n");
        }
        return s.toString();
    }
    
    
    
    public static void main(String[] args){ // unit tests (not graded)
        String[] s = new String[2];
        s[1] = "abcd";
        s[0] = 12313 + "." ;
        StringBuilder a = new StringBuilder();
        a.append("abcd");
        StdOut.println(a.delete(0,4).toString());
/*        In in = new In(args[0]);
        int n = in.readInt();
        int[][] map = new int[n][n];
        
        for (int i = 0; i < n; i++) {
           for (int j = 0; j < n; j ++){
                map[i][j] = in.readInt();
           }
        }
        Board test = new Board(map);
        Iterator<Board> p = test.neighbors().iterator();
        StdOut.println(test.toString());
       for (int i = 0; i < 1000; i ++){
            if (test.twin().equals(test))
            StdOut.println("equals to twin==>" + test.twin().equals(test));
        }
        int kk = 0;
        StdOut.println(kk++ + "  hamming = " + test.hamming() + "  manhattan = " + test.manhattan() + " " + test.isGoal() );
        
        while(p.hasNext()){
            Board a = p.next();
            StdOut.println(kk++ + "  hamming = " + a.hamming() + "  manhattan = " + a.manhattan() + " " + a.isGoal() );
            StdOut.print(a.toString());
            StdOut.println(a.equals(test));
        }*/
    }
}
