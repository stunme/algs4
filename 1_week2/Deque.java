import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;



public class Deque<Item> implements Iterable<Item> {
  private Node first = null, last = null;
  private int size = 0;
  
  private class Node {
    Item item;
    Node next;
    Node previous;
  }
  
  public Deque() {                           // construct an empty deque
  }
   
  public boolean isEmpty() {                 // is the deque empty?
    return size == 0 ; 
  }
   
  public int size() {                        // return the number of items on the deque
    return size;
  } 
     
  public void addFirst(Item item) {          // add the item to the front
    if ( item == null) throw new java.lang.IllegalArgumentException("client attempts to add a null item"); 
    Node oldFirst = first;
    first = new Node();
    first.item = item;
    first.previous = null;
    first.next = oldFirst;
    size ++;
    if (size == 1 ){
      last = first;
    } else if (size >1) {
      oldFirst.previous = first;
    }
  } 
   
  public void addLast(Item item) {           // add the item to the end
    if ( item == null ) throw new java.lang.IllegalArgumentException("client attempts to add a null item");
    Node oldLast = last;
    last = new Node();
    last.item = item; 
    last.next = null;
    last.previous = oldLast;
    size ++;
    if ( size == 1 ) {
      first = last;
    } else if (size > 1){
      oldLast.next = last;
    }
  }   
   
  public Item removeFirst() {               // remove and return the item from the front
    if ( first == null ) throw new java.util.NoSuchElementException ("client attempts to remove an item from an empty deque");
    Item item = first.item;
    first = first.next;
    size --;
    if (size > 0 ){
      first.previous = null;
    } else if (size == 0) {
      last = null;
    }
    return item;
  }
   
  public Item removeLast() {                // remove and return the item from the end
    if ( last == null ) throw new java.util.NoSuchElementException ("client attempts to remove an item from an empty deque");
    Item item = last.item;
    last = last.previous;
    size --;
    if (size > 0 ){
      last.next = null;
    } else if (size == 0){
      first = null;
    }
    return item ;
  }
   
  public Iterator<Item> iterator(){        // return an iterator over items in order from front to end
     return new ListIterator();
  }
  
  private class ListIterator implements Iterator<Item>{
    private Node current = first;
    public boolean hasNext(){
      return current != null;
    } 
    public Item next(){
      if (current == null) throw new java.util.NoSuchElementException(" there are no more items to return");
      Item item = current.item;
      current = current.next;
      return item;
    } 
    public void remove(){
      throw new java.lang.UnsupportedOperationException("client calls the remove() method in the iterator");
    }
  }
  
   
  public static void main(String[] args) {   // unit testing (optional)   
  }
   
   
}
