import edu.princeton.cs.algs4.BinaryStdOut;
//import java.util.HashMap;
import java.util.Arrays;
//import edu.princeton.cs.algs4.StdOut;

public class CircularSuffixArray {
   private int len;
   private String s;
   private int[] index;
   private char[] cur;
   private int[] aux;
   private final char[] count = new char[256];

   
   public CircularSuffixArray(String s){    // circular suffix array of s
      if (s == null) throw new java.lang.IllegalArgumentException("null s input");
      len = s.length();
      this.s = s;
      
      index = new int[len];
          
      aux = new int[len];
      cur = new char[len];
      
      for (int i = 0; i < len; i++){
         index[i]=i;
         //StdOut.print(s.charAt(i));
      }
     // StdOut.println(" ");
      sort(0,len-1,0);
      
  /*    for (int i = 0; i < len; i++){
         StdOut.print(s.charAt(index[i]));
      }
      StdOut.println(" ");*/
   }
   
   private void sort(int lo, int hi, int depth){
      if (depth >= len || hi <= lo) return;
   //   StdOut.println(lo + " and " + hi + " in "+ depth);
      for (int i = lo; i <= hi; i ++){
         cur[i] = s.charAt((index[i]+depth)%len);         
      }
     
      int left = lo;
      int right = hi;
      int i = lo + 1;
      char v = cur[lo];
      while (i<=right){
         char t = cur[i];
         //StdOut.println(i + " ==> " + t);
         if (t < v){
           // StdOut.println(t + " < " + v);
            exch(cur, i++, left++);
         }else if ( t > v ){
            //StdOut.println(t + " > " + v);
            exch(cur, i, right--);
         }else {
            //StdOut.println(t + " = " + v);
            i++;
         }
      }
      
      sort(lo, left-1, depth);
      sort(left,right, depth+1);
      sort(right+1,hi, depth);
   }
   
   private void exch(char[] cur, int a, int b){
      int tmp = index[a];
      index[a] = index[b];
      index[b] = tmp;
      char tp = cur[a];
      cur[a] = cur[b];
      cur[b] = tp;
      
   }
   
   
   
   
   
   private void sortMSD(int lo, int hi, int depth){
      if (depth>=len) return;
      HashMap<Character, Integer> map = new HashMap<>();
      //int[] count = new int[258];
      for (int i = lo; i< hi ; i ++ ){
         cur[i] = s.charAt((index[i]+depth)%len);
         if (map.containsKey(cur[i])){
            map.put(cur[i],map.get(cur[i])+1);
         }else{
            map.put(cur[i], 1);
         }
         //count[cur[i] + 2]++;
      }
      int size = map.size();
      char[] idx = new char[size];
      int id = 0;
      
      for (char c: map.keySet()){
         idx[id++] = c;
      }
      Arrays.sort(idx);
      int total = 0;
      int[] end = new int[size];

      for (int i = 0; i < size; i ++){
         total += map.get(idx[i]);
         map.put(idx[i], total);
         end[i] = total;
      }
      /*
      for (int i = 0; i < count.length-1; i++){
         count[i+1] += count[i];
      }*/
      for (int i = lo; i < hi ; i ++){
         int tmp = map.get(cur[i])-1;
         aux[lo+tmp] = index[i];
         map.put(cur[i],tmp);
         //aux[lo+count[cur[i]+1]++] = index[i];
      }

      for (int i = lo; i < hi ; i ++){
         index[i] = aux[i];
      }
      
      for (int i = 0; i < size; i ++){
         if(end[i]-map.get(idx[i]) > 1){
            sortMSD(lo + map.get(idx[i]), lo + end[i], depth+1);
         }
      }
      
      /*
      for (int i = 0; i < count.length-1; i++){
         if(count[i+1]-count[i] > 1){
//            StdOut.println(count[i+1] + " ==> " + count[i] + " & " +depth);
            sort(lo+count[i],lo+count[i+1],depth+1);
         }
      }*/
          
   }
   
   public int length(){                     // length of s
      return len;
   }
   public int index(int i) {                // returns index of ith sorted suffix
      if (i >= len || i < 0) throw new java.lang.IllegalArgumentException("i out of range");
      return index[i];
   }
   
   public static void main(String[] args){  // unit testing (required)
      CircularSuffixArray csa = new CircularSuffixArray(args[0]);
      BinaryStdOut.write(csa.length());
      for (int i = 0; i< csa.length(); i ++){
//         StdOut.println(csa.index(i) + " ==> " + args[0].charAt(csa.index(i)));
         BinaryStdOut.write(csa.index(i));
      }
      BinaryStdOut.close();
   }
}
