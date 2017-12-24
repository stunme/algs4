import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;


public class RandomizedQueue<Item> implements Iterable<Item> {
  private Item[] queue;
  private int size = 0;
  
  public RandomizedQueue(){                // construct an empty randomized queue
    queue = (Item[]) new Object[1];
  }
  
  
  
  
  public boolean isEmpty() {                // is the queue empty?
    return size == 0 ;
  } 
  
  public int size(){                   // return the number of items on the queue
    return size;
  }
  
  
  
  
  public void enqueue(Item item) {          // add the item
    if (item == null) throw new java.lang.IllegalArgumentException("client attempts to add a null item");
    if (size == queue.length) {
      Item[] newQueue = (Item[]) new Object[2*size];
      for (int i = 0; i < size; i++){
        newQueue[i] = queue[i];
      }
      queue = newQueue;
    }
    queue[size] = item;
    size++;
  }
  
  
  
  
  public Item dequeue() {                   // remove and return a random item
    if ( size == 0 ) throw new java.util.NoSuchElementException("client attempts to sample or dequeue an item from an empty randomized queue");
    int a = StdRandom.uniform(size);
    Item deItem = queue[a];
    while ( a < size-1 ) {
      queue[a]=queue[++a];
    }
    queue[a] = null;
    size --;
    if (size < queue.length/4) {
      Item[] newQueue = (Item[]) new Object[queue.length/2];
      for (int i = 0; i < size; i++){
        newQueue[i] = queue[i];
      }
      queue = newQueue;
    }
    return deItem;
  }
  
  
  
  
  public Item sample() {                    // return (but do not remove) a random item
    if ( size == 0 ) throw new java.util.NoSuchElementException("client attempts to sample or dequeue an item from an empty randomized queue");
    return queue[StdRandom.uniform(size)] ;
  }
  
  
  
  public Iterator<Item> iterator() {         // return an independent iterator over items in random order
    return new ListInterator();
  }
  
  private class ListInterator implements Iterator<Item> {
    private int[] seed = new int[size];
    private int idx = size;

    
    public ListInterator(){
      for (int i = 0; i < size; i++){
        seed[i] = i;
      }
      StdRandom.shuffle(seed);
    }
    
    public boolean hasNext(){
      return idx > 0;
    }
    
    public Item next(){
      if (idx == 0) throw new java.util.NoSuchElementException("client calls the next() method in the iterator and there are no more items to return");
      return queue[seed[--idx]];  
    }
    
    public void remove(){
      throw new java.lang.UnsupportedOperationException("client calls the remove() method in the iterator");
    }

    
    
    
  }
  
  
  
  
  public static void main(String[] args) {}  // unit testing (optional)





}



