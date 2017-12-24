import edu.princeton.cs.algs4.*;
import java.util.Iterator;
import java.util.Comparator;
import java.util.LinkedList;



public class Solver {
    private Board blocks;
    private LinkedList<Board> res = new LinkedList<>();
    private int moves=0;
    private boolean isSolvable = true;
    
    public Solver(Board initial){           // find a solution to the initial board (using the A* algorithm)
        if (initial == null ) throw new java.lang.IllegalArgumentException(" null ");
        blocks = initial;
        // Manhattan comparator
        // defined as ManhOrder
   
        
        //Manhattan MinPQ
        
        MinPQ<Node> map = new MinPQ<>(new ManhOrder());
        map.insert(new Node(null, blocks, moves));
        map.insert(new Node(null, blocks.twin(), moves));

        while(!map.min().cur.isGoal() ){
            Node curN = map.delMin();
            moves = curN.moves + 1;
            Iterator<Board> p = curN.cur.neighbors().iterator();
            while(p.hasNext()){
                Board a = p.next();
                if (curN.last == null || !a.equals(curN.last.cur)) map.insert(new Node(curN, a, moves));
            }
        }
        
        moves = map.min().moves;
        
        Node pp = map.min();
        while(pp.last != null ){
            res.push(pp.cur);
            pp = pp.last;
        }
        
        if (pp.cur.equals(blocks)) {
            res.push(pp.cur);
        }else {
            moves = -1;
            res = null;
            isSolvable = false;
        }
    }
    
   
    private class Node{
        Node last;
        Board cur;
        int moves;
        Integer manh;
        public Node(Node last, Board cur, int moves){
            this.last = last;
            this.cur = cur;
            this.moves = moves;
            this.manh = cur.manhattan();
        }
        
      
        
    }
   
   
    private class ManhOrder implements Comparator<Node>{
        public int compare(Node w, Node v){
            int i = Integer.compare(w.manh + w.moves, v.manh + v.moves);
            if (i != 0) return i;
            return Integer.compare(w.manh, v.manh);
        }
    }
   
    public boolean isSolvable() {           // is the initial board solvable?
        return isSolvable;
    }
    public int moves() {                     // min number of moves to solve initial board; -1 if unsolvable
        return moves;
    }
    public Iterable<Board> solution() {     // sequence of boards in a shortest solution; null if unsolvable/
        return res;
    }
    public static void main(String[] args) { // solve a slider puzzle (given below)
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
