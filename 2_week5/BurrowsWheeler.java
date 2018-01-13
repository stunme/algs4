import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.BinaryStdIn;



public class BurrowsWheeler {
    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform(){
      StringBuilder s = new StringBuilder();
      while(!BinaryStdIn.isEmpty()){
         s.append(BinaryStdIn.readChar());
      }
      CircularSuffixArray array = new CircularSuffixArray(s.toString());
      int len = array.length();
      for(int i = 0; i < len; i ++){
         if (array.index(i) == 0){
            BinaryStdOut.write(i);           
         }
      }
      for (int i = 0; i < len; i ++){
         BinaryStdOut.write(s.charAt( (len+array.index(i)-1)%len ));
      }
      BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform(){
      int first = BinaryStdIn.readInt();
      StringBuilder s = new StringBuilder(); 
      while(!BinaryStdIn.isEmpty()){
         s.append(BinaryStdIn.readChar());
      }
      int len = s.length();
      
      int[] count = new int[258];
      for (int i = 0; i < len; i ++){
         count[(int)s.charAt(i)+2]++;
      }
      for (int i = 0; i < 257; i ++ ){
         count[i+1] += count[i];
      }
      char[] head = new char[len];
      int[] index = new int[len];

      for (int i = 0; i < len; i ++){
         index[count[s.charAt(i)+1]] = i;
         head[count[s.charAt(i)+1]++] = s.charAt(i);
         
      }

      
      int[] next = new int[len];
      
      for (int i = 0; i < 257; i ++){
         if (count[i+1] > count[i]){
            for (int j = count[i]; j < count[i+1]; j++ ){
               next[j]=index[j];
            }
         }
      }
      
      for (int i = 0; i < len; i ++){
         BinaryStdOut.write(head[first]);
         first = next[first];
      }
      BinaryStdOut.close();
      
    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args){
      if (args[0].equals("-")) {
         BurrowsWheeler.transform();         
      }else if (args[0].equals("+")){
         BurrowsWheeler.inverseTransform();
      }
      
    }
}
