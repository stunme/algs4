import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.LinkedList;
//import java.util.HashMap;

public class Outcast {
   private final WordNet net;
   private String ms = new String();
   private int max;
   
   public Outcast(WordNet wordnet){         // constructor takes a WordNet object
      this.net = wordnet;
   }
   
   public String outcast(String[] nouns){   // given an array of WordNet nouns, return an outcast      
      ms = nouns[0];
      max = 0;
      for ( String i : nouns){
         int temp = 0;
         for ( String j : nouns ){
            temp += net.distance(i, j);
         }
      if (temp > max){
         max = temp;
         ms = i ;
      }
       
         
      }
      return ms;
   }
   
   
   public static void main(String[] args) {
      WordNet wordnet = new WordNet(args[0], args[1]);
      Outcast outcast = new Outcast(wordnet);
      for (int t = 2; t < args.length; t++) {
         In in = new In(args[t]);
         String[] nouns = in.readAllStrings();
         StdOut.println(args[t] + ": " + outcast.outcast(nouns));
      }
   }
}
