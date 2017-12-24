import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.LinkedList;
//import java.util.HashMap;


public class SAP {
   private final Digraph g;
   private LinkedList<Integer> vQ, wQ;
   private int[] vMap, wMap;
   private boolean[] vMarked, wMarked;
   
   

   // constructor takes a digraph (not necessarily a DAG)
   public SAP(Digraph G){
      if ( G == null ) throw new java.lang.IllegalArgumentException("null input G");
      this.g = new Digraph(G);
   }
   
   private boolean invalidV(int v){
      return v < 0 || v >= g.V(); 
   }
   
   private void bsf(LinkedList<Integer> Q, int[] map, boolean[] marked){
      while(!Q.isEmpty()){
         int v = Q.poll();
         for (int s : g.adj(v)){         
            if (!marked[s]){
               marked[s] = true;
               map[s] = map[v]+1;
               Q.offer(s);
            }
         }
      
      }
   }
   
   private int Shortlen(boolean len){
      int min = -1;
      int ans = -1;
      for (int i= 0; i< g.V(); i++){
         if (vMarked[i]&&wMarked[i]){
            int tmp = vMap[i] + wMap[i];
            if (min > tmp || min == -1){
               min = tmp;
               ans = i;
            }
         }
      }
      
      return len?min:ans;
   }
   
   private void single(int v, int w){   
   

      vQ = new LinkedList<Integer>();
      vQ.offer(v);
      vMap = new int[g.V()];
      vMarked = new boolean[g.V()];
      vMarked[v] = true;
      bsf(vQ, vMap, vMarked);     

      wQ = new LinkedList<Integer>();
      wQ.offer(w);
      wMap = new int[g.V()];
      wMarked = new boolean[g.V()];
      wMarked[w] = true;
      bsf(wQ, wMap, wMarked);
   //   for( int i : vMap){
         
//StdOut.println(i);
  //    }
      
    //  for( int i : wMap){
    //     StdOut.println(i);
   //   }
   }
   
   
   // length of shortest ancestral path between v and w; -1 if no such path
   public int length(int v, int w){
      if (invalidV(v)) throw new java.lang.IllegalArgumentException("invalid v");
      if (invalidV(w)) throw new java.lang.IllegalArgumentException("invalid w");
      this.single(v, w);      
      return this.Shortlen(true);

   }

   // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
   public int ancestor(int v, int w){
      if (invalidV(v)) throw new java.lang.IllegalArgumentException("invalid v");
      if (invalidV(w)) throw new java.lang.IllegalArgumentException("invalid w");
      this.single(v, w);
      return this.Shortlen(false);
   }

   private void multi(Iterable<Integer> v, Iterable<Integer> w){
      vQ = new LinkedList<Integer>();
      vMap = new int[g.V()];
      vMarked = new boolean[g.V()];
      for (int i : v){
         vQ.offer(i);
         vMarked[i] = true;
      }
      bsf(vQ, vMap,vMarked);
   
      
      wQ = new LinkedList<Integer>();
      wMap = new int[g.V()];
      wMarked = new boolean[g.V()];
      for (int i : w){
         wQ.offer(i);
         wMarked[i] = true;
      }
      bsf(wQ, wMap,wMarked);
   
   }

   // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
   public int length(Iterable<Integer> v, Iterable<Integer> w){
      if (v == null || w==null) throw new java.lang.IllegalArgumentException("null v");
      for (int i : v){
         if (invalidV(i)) throw new java.lang.IllegalArgumentException("invalid v");
      }
      for (int i : w){
         if (this.invalidV(i)) throw new java.lang.IllegalArgumentException("invalid w");
      }
 
      this.multi(v, w);
  
      return this.Shortlen(true);

   
   }

   // a common ancestor that participates in shortest ancestral path; -1 if no such path
   public int ancestor(Iterable<Integer> v, Iterable<Integer> w){
   
      if (v == null || w==null) throw new java.lang.IllegalArgumentException("null v");
      for (int i : v){
         if (this.invalidV(i)) throw new java.lang.IllegalArgumentException("invalid v");
      }
      for (int i : w){
         if (this.invalidV(i)) throw new java.lang.IllegalArgumentException("invalid w");
      }
      this.multi(v, w);
      return this.Shortlen(false);

   
   }

   // do unit testing of this class
   public static void main(String[] args) {
      In in = new In(args[0]);
      Digraph G = new Digraph(in);
      SAP sap = new SAP(G);
      while (!StdIn.isEmpty()) {
         int v = StdIn.readInt();
         int w = StdIn.readInt();
         int length   = sap.length(v, w);
         int ancestor = sap.ancestor(v, w);
         StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
}
}
