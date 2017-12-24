import java.util.TreeSet;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ArrayList;

public class KdTree {
    private Node root;
    private int size;
    
    private class Node{
        double keyX;
        double keyY;
        Point2D value;
        Node left, right;
        boolean splitX;
    }   
    
    public KdTree(){                          // construct an empty set of points 
        root = null;
    }
    
    public boolean isEmpty(){                      // is the set empty? 
        return root == null;
    }
    
    public int size(){                         // number of points in the set 
        return size;
    }
    
    public void insert(Point2D p){              // add the point to the set (if it is not already in the set)
        if ( p == null ) throw new java.lang.IllegalArgumentException("null inserted");
        
        Node addi = new Node();
        addi.keyX = p.x();
        addi.keyY = p.y();
        addi.value = p;
        
        if (root == null ){
            root = addi;
            root.splitX = true;
            size = 1;
            return;
        }
        Node cur = root;
        while(true){
            if (p.equals(cur.value)) return;
            if (cur.splitX) {
                if (Point2D.X_ORDER.compare(cur.value, p) >= 0 ) {
                    if (cur.left == null ){
                        cur.left = addi;
                        addi.splitX = false;
                        size ++;
                        return;
                    }
                    cur = cur.left;
                } else {
                    if (cur.right == null ){
                        cur.right = addi;
                        addi.splitX = false;
                        size ++;
                        return;
                    }
                    cur = cur.right;
                }
            }else {
                if (Point2D.Y_ORDER.compare(cur.value, p ) >= 0 ) {
                    if (cur.left == null ){
                        cur.left = addi;
                        addi.splitX = true;
                        size ++;
                        return;
                    }
                    cur = cur.left;
                } else {
                    if (cur.right == null ){
                        cur.right = addi;
                        addi.splitX = true;
                        size ++;
                        return;
                    }
                    cur = cur.right;
                }
            }
        }
    }
    
    
    public boolean contains(Point2D p){            // does the set contain point p? 
        if ( p == null) throw new java.lang.IllegalArgumentException("null contains input");
        Node cur = root;
        while( cur!= null ){
            if(p.equals(cur.value)) return true;
            if (cur.splitX) {
                if (Point2D.X_ORDER.compare(cur.value, p) >= 0 ) cur = cur.left;
                else cur = cur.right;
            }else{
                if (Point2D.Y_ORDER.compare(cur.value, p ) >= 0 ) cur = cur.left;
                else cur = cur.right;
            }
        }
        return false;
    }
    
    public void draw(){                         // draw all points to standard draw 
        if ( root == null ) throw new java.lang.IllegalArgumentException("null");
        draw(root, 0,1,0,1);        
    }
    
    private void draw(Node cur, double minX, double maxX, double minY, double maxY){
        if (cur == null ) return ;
        Point2D p = cur.value;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.point(p.x(), p.y());
        if(cur.splitX){
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(p.x(),minY,p.x(),maxY);
            draw(cur.left, minX, p.x(), minY, maxY);
            draw(cur.right, p.x(), maxX, minY, maxY);
        }else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(minX,p.y(),maxX,p.y());
            draw(cur.left, minX, maxX, minY, p.y());
            draw(cur.right, minX, maxX, p.y(), maxY);
        }
        
    }
    
    public Iterable<Point2D> range(RectHV rect){             // all points that are inside the rectangle (or on the boundary) 
        if (rect == null ) throw new java.lang.IllegalArgumentException("null");
        ArrayList<Point2D> list = new ArrayList<>();
        range(list, root, rect);
        return list;
    }
    
    
    private void range(ArrayList<Point2D> list, Node cur, RectHV rect){
        if ( cur == null ) return;
            if (cur.splitX){
                if (cur.value.x() > rect.xmax()) {
                    range(list, cur.left, rect);
                } else if (cur.value.x() < rect.xmin()){
                    range(list, cur.right, rect);
                } else {
                    if(cur.value.y() >= rect.ymin() && cur.value.y()<=rect.ymax()) list.add(cur.value);
                    range(list, cur.left, rect);
                    range(list, cur.right, rect);
                }
            } else {
                if (cur.value.y() > rect.ymax()) {
                    range(list, cur.left, rect);
                } else if (cur.value.y() < rect.ymin()){
                    range(list, cur.right, rect);
                } else {
                    if(cur.value.x() >= rect.xmin() && cur.value.x()<=rect.xmax()) list.add(cur.value);
                    range(list, cur.left, rect);
                    range(list, cur.right, rect);
                }
            
            }
        
    }
    
    public Point2D nearest(Point2D p){             // a nearest neighbor in the set to point p; null if the set is empty 
        if (p == null ) throw new java.lang.IllegalArgumentException("null");
        if (root == null) return null;
        Point2D near = root.value;
        near = nearest(near, Double.POSITIVE_INFINITY, root, p);
        return near;
    }
    
    private Point2D nearest(Point2D near, double distance, Node cur, Point2D p){
    
        if (cur == null) return near;

            double curDis = p.distanceTo(cur.value);
            if (curDis < distance){
                near = cur.value;
                distance = curDis;
            }

        if ( cur.splitX ){
        
            if( p.x()<= cur.value.x() ) {
                
                near = nearest(near, distance, cur.left, p);
                distance = p.distanceTo(near);
                if (distance > cur.value.x() - p.x() )
                    near = nearest(near,distance, cur.right, p);
            }else{
                near = nearest(near, distance, cur.right, p);
                distance = p.distanceTo(near);
                if (distance > p.x() - cur.value.x() )
                    near = nearest(near,distance, cur.left, p);
            }
        }else{
             if( p.y()<= cur.value.y() ) {
                near = nearest(near, distance, cur.left, p);
                distance = p.distanceTo(near);
                if (distance > cur.value.y() - p.y() )
                    near = nearest(near,distance, cur.right, p);
            }else{
                near = nearest(near, distance, cur.right, p);
                distance = p.distanceTo(near);
                if (distance > p.y() - cur.value.y() )
                    near = nearest(near,distance, cur.left, p);
            }
        }
        return near;
    }
    
    
    public static void main(String[] args){                  // unit testing of the methods (optional) 
        
    }
   
}
