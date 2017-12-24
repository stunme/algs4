import edu.princeton.cs.algs4.StdDraw;
import java.util.Comparator;

public class Point implements Comparable<Point>{
    private final int x;
    private final int y;
    private final static double d = 0.000001; //for double comparasion
    
    public Point(int x, int y){          // constructs the point (x, y)
        this.x = x;
        this.y = y;
    }
    
    public void draw(){                  // draws this point
        StdDraw.point(x, y);
    }
    
    
    public void drawTo(Point that){        // draws the line segment from this point to that point
        StdDraw.line(this.x, this.y, that.x, that.y);
    }
    
    
    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }
    
    
    public int compareTo(Point that){     // compare two points by y-coordinates, breaking ties by x-coordinates
        if (that == null) throw new java.lang.NullPointerException("argument is null");
        if ( that.y != this.y ) return this.y - that.y;
        if ( that.x != this.x) return this.x - that.x;
        return 0;
    }
    
    public double slopeTo(Point that){       // the slope between this point and that point
        if (this.compareTo( that ) == 0) return Double.NEGATIVE_INFINITY;
        if (that.y == this.y ) return 0.0;
        if (that.x == this.x ) return Double.POSITIVE_INFINITY;
        return (double)( that.y-this.y ) / (double)( that.x-this.x );
    }
    
    public Comparator<Point> slopeOrder(){              // compare two points by slopes they make with this point
        return new SlopeOrder();
    }
    
    private class SlopeOrder implements Comparator<Point>{
        public int compare (Point v, Point w){
            Double toW = Point.this.slopeTo(w);
            Double toV = Point.this.slopeTo(v);
            return Double.compare(toV, toW);
        }
    }
    
    
    
     public static void main(String[] args) {
    /*    Point[] p = new Point[3];
        p[1] = new Point(1,20);
        p[2] = new Point(3,23);
        p[0] = new Point(3,10);
        System.out.println(p[0].compareTo(p[1]));
        System.out.println(p[1].slopeTo(p[2]));
        System.out.println(p[0].toString());
        System.out.println(p[0].slopeOrder().compare(p[1],p[2]));
        Arrays.sort(p, p[0].slopeOrder());
*/
    }
    
}
