import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.*;
import java.util.Comparator;



public class BruteCollinearPoints {
    private int num;
    private LineSegment[] res;
    private Point[][] map;
    private Point[] points;
    private final int len;
    
    public BruteCollinearPoints(Point[] points){    // finds all line segments containing 4 points
        if (points == null ) throw new java.lang.IllegalArgumentException("null points");
        if (points[0] == null ) throw new java.lang.IllegalArgumentException("null element");
        len = points.length;
        this.points = new Point[len];
        this.points[0] = points[0];
        for (int i = 0; i < len-1; i++){
            for (int j = i+1; j < len; j++){
                if (points[j] == null ) throw new java.lang.IllegalArgumentException("null elemnt");
                if (points[i].compareTo(points[j]) == 0) throw new java.lang.IllegalArgumentException("repeated points");
                this.points[j] = points[j];
            }
        }
        
        map = new Point[1][2];
        num = 0;

       if (len <4)  {
            res = new LineSegment[0];
            return;
        }
        
        for (int i = 0; i < this.len-3; i ++){
            for ( int j = i+1; j < this.len-2; j ++ ) 
            label : {
                for ( int k = j + 1; k < this.len-1; k++){
                    if ( this.points[j].slopeOrder().compare(this.points[i],this.points[k]) == 0 ){
                        for ( int l = k + 1; l < this.len; l++){
                            if ( this.points[k].slopeOrder().compare(points[j],this.points[l]) == 0 ){
                                Point[] insrt = minmax(new Point[]{this.points[i],this.points[j],this.points[k],this.points[l]});
                                insert(insrt);
                                break label;
                            }
                        }
                    }
                }
            }
        }
        res = new LineSegment[num];
        for (int i = 0; i < num; i++) {
            res[i] = new LineSegment(map[i][0],map[i][1]);
        }
    }
    
    private Point[] minmax(Point[] list){
        Point max = list[0];
        Point min = list[0];
        for (int a = 1; a < list.length; a++){
            if (max.compareTo(list[a])<0) max = list[a];
            if (min.compareTo(list[a])>0) min = list[a];
        }

        return new Point[]{min, max};   
    }
    
     private void insert(Point[] list){
        
        for (int i = 0; i < num; i ++) {
           if (map[i][0].compareTo(list[0]) == 0 && map[i][1].compareTo(list[1]) == 0) return;
        }
        
        if (map.length == num) {
            Point[][] new_map = new Point[2*num][2];
            for (int i = 0; i < num; i ++){
                new_map[i] = map[i];
            }
            map = new_map;
        }
        
        
        
        map[num] = list;
        num++;
        
    
     }
    
    
    public int numberOfSegments(){        // the []number of line segments
        return num;
    }
    public LineSegment[] segments(){                // the line segments
        return res;
    }
    
    
    
    
    public static void main(String[] args) {

    // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
    
    
    
    
}
