import java.util.TreeSet;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ArrayList;

public class PointSET {
    private TreeSet<Point2D> pointSet;
    
    public PointSET(){                          // construct an empty set of points 
        pointSet = new TreeSet<>(pointOrder());
    }
    
    private Comparator<Point2D> pointOrder(){
        return new PointOrder();
    }
    
    private class PointOrder implements Comparator<Point2D>{
        public int compare(Point2D w, Point2D v){
            return w.compareTo(v);
        }
    }  
   
    public boolean isEmpty(){                      // is the set empty? 
        return pointSet.isEmpty();
    }
    
    public int size(){                         // number of points in the set 
        return pointSet.size();
    }
    
    public void insert(Point2D p){              // add the point to the set (if it is not already in the set)
        if ( p == null ) throw new java.lang.IllegalArgumentException("null inserted");
        pointSet.add( p );
    }
    public boolean contains(Point2D p){            // does the set contain point p? 
        if ( p == null) throw new java.lang.IllegalArgumentException("null contains input");
        return pointSet.contains( p );
    }
    public void draw(){                         // draw all points to standard draw 
        if ( pointSet == null ) throw new java.lang.IllegalArgumentException("null");
        Iterator<Point2D> i = pointSet.iterator();
        while(i.hasNext()){
            Point2D p= i.next();
            StdDraw.point(p.x(), p.y());
        }
    }
    
    public Iterable<Point2D> range(RectHV rect){             // all points that are inside the rectangle (or on the boundary) 
        if (rect == null ) throw new java.lang.IllegalArgumentException("null");
        ArrayList<Point2D> list = new ArrayList<>();
        Iterator<Point2D> i = pointSet.iterator();
        while(i.hasNext()){
            Point2D p = i.next();
            if ( p.x() >= rect.xmin() && p.x() <= rect.xmax() && p.y() >= rect.ymin() && p.y() <= rect.ymax() ){
                list.add(p);
            }
        }
        return list;
    }
    
    public Point2D nearest(Point2D p){             // a nearest neighbor in the set to point p; null if the set is empty 
        if (p == null ) throw new java.lang.IllegalArgumentException("null");
        Point2D nearest = null;
        Point2D cur = null;
        
        double distance = Double.POSITIVE_INFINITY;
        double curDis = 0.0;
        
        Iterator<Point2D> i = pointSet.iterator();
        while(i.hasNext()){
            cur = i.next();
            //if ( p.equals(cur) ) continue;
            curDis = p.distanceTo(cur);
            if ( curDis <= distance ) {
                distance = curDis;
                nearest = cur;
            }
        }
        return nearest;
    }
    
    
    public static void main(String[] args){                  // unit testing of the methods (optional) 
        
    }
   
}
