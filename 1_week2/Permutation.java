import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;


public class Permutation {
   private static String string;
   public static void main(String[] args){
     int k = Integer.parseInt(args[0]);
     RandomizedQueue queue = new RandomizedQueue();
     while ( !StdIn.isEmpty() ){
       string = StdIn.readString();
       queue.enqueue(string);
     }
     while ( (k--) > 0 ){
       StdOut.println(queue.dequeue());
     }
   
   }
}
