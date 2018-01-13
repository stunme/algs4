import java.util.HashSet;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;



public class BoggleSolver
{
   private Node dicroot;
   private Digraph map;
   private String[] dic;
   private char[] charmap;
   private boolean[] marked;
   private HashSet<String> result;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
   public BoggleSolver(String[] dictionary){
      if (dictionary == null || dictionary.length == 0) throw new java.lang.IllegalArgumentException("empty dic array");
      //construct dic by Node;
      dicroot = new Node();
      int val = 0;
      dic = new String[dictionary.length];
      for (String s : dictionary){
         dic[val] = s;
         Node cur = dicroot;
         for (char c: s.toCharArray()){
            int i = c - 'A';
            if (cur.next[i] == null ){
               Node tmp = new Node();
               cur.next[i] = tmp;
               cur = tmp;
            }else {
               cur = cur.next[i];
            }
         }
         cur.val = val++;
      }
   }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
   public Iterable<String> getAllValidWords(BoggleBoard board){
      result = new HashSet<>();
   
      int row = board.rows();
      int col = board.cols();
      int total = row * col;
      map = new Digraph(total);
      charmap = new char[total];
      
      //build digraph and charmap;
      
      for (int i = 0; i < row; i ++){
         for (int j = 0; j< col; j++){
            int center = i * col + j;
            charmap[center] = board.getLetter(i, j);
            for (int ci = i-1; ci < i + 2; ci ++){
               if (ci < 0 || ci == row) continue;
               for (int cj = j-1; cj < j + 2; cj ++){
                  if (ci == 1 && cj ==1 ) continue;
                  if (cj < 0 || cj == col ) continue;
                  //StdOut.println("ci = " + ci + " cj = " + cj + " total = " + (ci * col + cj));
                  
                  map.addEdge(center, ci*col + cj);
               }
            }
         }
      }
      
      //DSF 
      marked = new boolean[total];
      for (int i = 0; i < total; i ++){
         marked[i] = true;
         Node cur = dicroot;
         dsf(cur, i);
         marked[i] = false;
      }
      
      return result;
   }
   
   private void dsf(Node cur, int i){
         //word validation
         int tmp = charmap[i] - 'A';
         if (cur.next[tmp] == null){
            return;
         }else if (charmap[i] == 'Q'){
            cur = cur.next[tmp];
            if (cur.next[20] == null){
               return;
            }else{
               cur = cur.next[20];
            }
         }else {
            cur = cur.next[tmp];
         }
         if (cur.val != -1 && dic[cur.val].length()>2) {
            result.add(dic[cur.val]);
         }
         
         
         // dfs to next
         for (int j : map.adj(i)){
            if (!marked[j]){
               marked[j] = true;
               Node curcopy = cur;
               dsf(curcopy, j);
               marked[j] = false;
            }
         }
   }
   

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
   public int scoreOf(String word){
      if (word == null || word.length() == 0) throw new java.lang.IllegalArgumentException("invalid String inputed");
      if (dicroot == null) throw new java.lang.IllegalArgumentException("empty dic used for searching");
      int len = word.length();
      if (len < 3 ) return 0;
      Node cur = dicroot;
      for (char c : word.toCharArray()){
         int i = c-'A';
         if (cur.next[i] == null){
            return 0;
         }else {
            cur = cur.next[i];
         }
      }
      if (len < 5 ) return 1;
      else if (len < 6) return 2;
      else if (len < 7) return 3;
      else if (len < 8) return 5;
      else return 11;
   }
     
   private class Node{
      int val;
      Node[] next;
      public Node(){
         val = -1;
         next = new Node[26];
      }
   }
   
   public static void main(String[] args) {
      In in = new In(args[0]);
      String[] dictionary = in.readAllStrings();
      BoggleSolver solver = new BoggleSolver(dictionary);
      BoggleBoard board = new BoggleBoard(args[1]);
      StdOut.println(board.toString());
      int score = 0;
      for (String word : solver.getAllValidWords(board)) {
        StdOut.println(word);
        score += solver.scoreOf(word);
      }
      StdOut.println("Score = " + score);
   }
}


