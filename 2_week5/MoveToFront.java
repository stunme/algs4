import java.util.LinkedList;
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

//import edu.princeton.cs.algs4.StdOut;


public class MoveToFront {
   // apply move-to-front encoding, reading from standard input and writing to standard output

   public static void encode(){
      LinkedList<Integer> list = new LinkedList<>();
      for (int i = 0; i < 256; i ++){
         list.add(i);
      }
      
      while(!BinaryStdIn.isEmpty()){
         char c = BinaryStdIn.readChar();
         int i = list.indexOf((int)c);
         list.offerFirst(list.remove(i));
         BinaryStdOut.write((char)i);
      }      
      BinaryStdOut.close();
   }

    // apply move-to-front decoding, reading from standard input and writing to standard output
   public static void decode(){
      LinkedList<Integer> list = new LinkedList<>();
      for (int i = 0; i < 256; i ++){
         list.add(i);
      }
      
      while(!BinaryStdIn.isEmpty()){
         char i = BinaryStdIn.readChar();
         int c = list.get(i);
         list.offerFirst(list.remove((int)i));
         BinaryStdOut.write((char) c);
      }
      BinaryStdOut.close();
      
   }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
   public static void main(String[] args){
      if (args[0].equals("-")) {
         MoveToFront.encode();         
      }else if (args[0].equals("+")){
         MoveToFront.decode();
      }
      //StdOut.println(args[0].charAt[0]-0);
      
   }
}
