import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.HashMap;
import java.util.Stack;


public class WordNet {
   private final String[] synset;
   private final String[] gloss;
   private final HashMap<String, Stack<Integer>> map;
   private final Digraph g;
   private int v;
   private int e;
   private final boolean[] marked;
//   private final int[] edgeTo;
   private final boolean[] returned;
   private Integer root = null;
   private final SAP sap;
   
   // constructor takes the name of the two input files
   public WordNet(String synsets, String hypernyms){
      if(synsets == null || hypernyms == null ) throw new java.lang.IllegalArgumentException("null input");
            
      In in = new In(synsets);
      
      String[] temp = in.readAll().split("\n");
      v = temp.length;
      synset = new String[v];
      gloss = new String[v];
      String[] tmp;
      map = new HashMap<String, Stack<Integer>>();
      for (String s : temp){
         tmp = s.split(",");
         int x = Integer.parseInt(tmp[0]);
         synset[x] = tmp[1];
         gloss[x] = tmp[2];
            
         for (String j : tmp[1].split(" ")){
            if ( !map.containsKey(j) ){
               map.put(j, new Stack<Integer>());
            }
            map.get(j).push(x);
         }       
      }
      
      g = new Digraph(v);
      
      in = new In(hypernyms);
      temp = in.readAll().split("\n");
      e = temp.length;
    
      for (String s : temp){
         tmp = s.split(",");
         int x = Integer.parseInt(tmp[0]);
         for (int i = 1; i < tmp.length; i ++){
            g.addEdge(x, Integer.parseInt(tmp[i]));
         }
      }
      
      marked = new boolean[v];
      returned = new boolean[v];
      for (int i = 0; i<v; i++){
         if(!marked[i]){
            dfsN(g, i);
            returned[i] = true;

         }
      }

      sap = new SAP(g);
   }
   
   private void dfsN(Digraph G, int n){
      marked[n] = true;
      if ( G.outdegree(n) == 0 ){
         if ( root == null ) {
            root = n;
            
         }else if(root != n) {
            throw new java.lang.IllegalArgumentException("multiple root");
         }
      }
      for (int s : G.adj(n)){   
         if (!marked[s]){
            dfsN(G, s);
            returned[s] = true;
            
         }else if (!returned[s]){
            throw new java.lang.IllegalArgumentException("directed circle detected");
         }
      }
   }
   

   // returns all WordNet nouns
   public Iterable<String> nouns(){
      return map.keySet();
   }

   // is the word a WordNet noun?
   public boolean isNoun(String word){
      if (word == null ) throw new java.lang.IllegalArgumentException("null");
      return map.containsKey(word);
   }

   // distance between nounA and nounB (defined below)
   public int distance(String nounA, String nounB){
      if (!isNoun(nounA) ) throw new java.lang.IllegalArgumentException(nounA + " is not a valid noun");
      if (!isNoun(nounB) ) throw new java.lang.IllegalArgumentException(nounB + " is not a valid noun");
      return sap.length(map.get(nounA), map.get(nounB));
   }

   // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
   // in a shortest ancestral path (defined below)
   
   public String sap(String nounA, String nounB){
      if (!isNoun(nounA) ) throw new java.lang.IllegalArgumentException(nounA + " is not a valid noun");
      if (!isNoun(nounB) ) throw new java.lang.IllegalArgumentException(nounB + " is not a valid noun");

      return synset[sap.ancestor(map.get(nounA), map.get(nounB))];

   }

   // do unit testing of this class
   public static void main(String[] args){
      WordNet wordnet = new WordNet(args[0], args[1]);
      
      
      while (!StdIn.isEmpty()) {
         String v = StdIn.readString();
         String w = StdIn.readString();
         int length   = wordnet.distance(v, w);
         String ancestor = wordnet.sap(v, w);
         StdOut.printf("length = " + length + " ancestor = " + ancestor + "\n");
      }

   }
}
