import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.*;



public class FastCollinearPoints {
    private int num;
    private LineSegment[] res;
    private int[] mapF = new int[1];
    private int[] mapR = new int[1];
    private Double[] slopes;
    private int[] slopeIdx;
    private final static int CUT = 2;       //set the cut off
    private final int len;
    private Point[] points;

    
    public FastCollinearPoints(Point[] points){     // finds all line segments containing 4 or more points
        if (points == null ) throw new java.lang.IllegalArgumentException("null points");
        len= points.length;
        if (points[0] == null ) throw new java.lang.IllegalArgumentException("null element");

        this.points = new Point[len];
        this.points[0] = points[0];


        for (int i = 0; i < len-1; i++){
            for (int j = i+1; j < len; j++){
                if ( points[j] == null) throw new java.lang.IllegalArgumentException("null element");
                if (points[i].compareTo(points[j]) == 0) throw new java.lang.IllegalArgumentException("repeated points");
                this.points[j] = points[j];
            }
        }
        num = 0;
        mapF = new int[1];
        mapR = new int[1];
        
        if (len <4)  {
            res = new LineSegment[0];
            return;
        }

        
        slopes = new Double[len];
        slopeIdx = new int[len];
        
        for (int i =0 ; i < len; i++){
            for (int j = 0; j < len; j ++){
                slopes[j] = this.points[i].slopeTo(this.points[j]);
                slopeIdx[j] = j;
            }            
            sort(0,len-1,slopes,slopeIdx);
            findDup();
        }
        
        if ( num == 0) {
            res = new LineSegment[0];
            return;
        }
        
        Double[] mapC = new Double[num];
        
        for (int i = 0; i < num; i++) mapC[i] = (double)mapF[i]+ (double)mapR[i]/len;
        
        sort(0,num-1,mapC,mapR);
        
        int numN = 0;        
        res = new LineSegment[num];
        
        for (int i = 1, j =0; i < num; i++) {
            if ( Double.compare(mapC[i],mapC[j]) ==0 ) {
                if ( i < num - 1) continue;
                res[numN++] = new LineSegment(this.points[(int)Math.floor(mapC[j])],this.points[mapR[j]]);
                break;
            }   
            
            res[numN] = new LineSegment(this.points[(int)Math.floor(mapC[j])],this.points[mapR[j]]);
            numN ++ ;
            j = i ;
                  
            if (i == num -1 )  {
            res[numN++] = new LineSegment(this.points[(int)Math.floor(mapC[j])],this.points[mapR[j]]);
            }
        }
        num = numN;
        
        LineSegment[] res_new = new LineSegment[num];
        for (int i = 0; i < num; i ++){
            res_new[i] = res[i];
        }
        res =  res_new;
    }
    
    
    private void sort(int i, int j,Double[] slopes, int[] slopeIdx){
        if( i == j ) return;
        int k = (i+j)/2+1;
        sort(i,k-1,slopes,slopeIdx);
        sort(k,j,slopes,slopeIdx);
        mergeSort(i,j,k,slopes, slopeIdx);
    }
    
    
    private void mergeSort(int i, int j, int k, Double[] slopes, int[] slopeIdx){
        Double[] tempD = new Double[k-i];
        int[] tempI = new int[k-i];
        
        for (int m = i; m < k; m++){
            int idx = m - i;
            tempD[idx] = slopes[m];
            tempI[idx] = slopeIdx[m];
        }
        
        for (int n = i, idxA = n-i, idxB = k; n <= j; n++){
            if ( idxA == k -i ) {
                break;
            }
            if ( idxB == j +1 ) {
                slopes[n] = tempD[idxA];
                slopeIdx[n] = tempI[idxA++];
                continue;
            }
            if ( Double.compare(tempD[idxA], slopes[idxB]) <= 0){              //for double comparasion
                slopes[n] = tempD[idxA];
                slopeIdx[n] = tempI[idxA++];
            } else {
                slopes[n] = slopes[idxB];
                slopeIdx[n] = slopeIdx[idxB++];
            }
        }
        
    }
    
    
    
    private void findDup(){
        int i = 1;
        for (int j = 2; j < len; j ++ ){
            if ( Double.compare(slopes[j], slopes[i]) == 0 ) {
                if (j < len-1) continue;
                j ++;          
            }
            if ( j - i > CUT){
                int[] list = new int[j-i+1];
                list[0] = slopeIdx[0];
                int k  = 1;
                while (i < j){
                    list[k++] = slopeIdx[i++];                    
                }
            insert(minmax(list));
            } else {
                i = j;
                
            }
            
        
        }
           
    };
    
    
    private int[] minmax(int[] list){
        int max = list[0];
        int min = list[0];
        for (int a = 1; a < list.length; a++){
            if (points[max].compareTo(points[list[a]])<0) max = list[a];
            if (points[min].compareTo(points[list[a]])>0) min = list[a];
        }

        return new int[]{min, max};   
    }
    
     private void insert(int[] list){      
        if (mapF.length == num) {
            int[] new_mapF = new int[2*num];
            int[] new_mapR = new int[2*num];

            for (int i = 0; i < num; i ++){
                new_mapF[i] = mapF[i];
                new_mapR[i] = mapR[i];

            }
            mapF = new_mapF;
            mapR = new_mapR;

        }
        
        
        
        mapF[num] = list[0];
        mapR[num] = list[1];
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
        for (int i = 0; i < n; i++) {
            points[i] = new Point(0, 0);
        }
       for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
        }
        StdOut.println("====================================");
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
        }
                StdOut.println("====================================");

        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
        }       
         StdOut.println("====================================");

        StdOut.println(collinear.numberOfSegments());
        StdOut.println(collinear.numberOfSegments());
        StdOut.println(collinear.numberOfSegments());
        StdOut.println(collinear.numberOfSegments());
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
        }       
         StdOut.println("====================================");
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
        }       
         StdOut.println("====================================");
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
        }       
         StdOut.println("====================================");

        points = new Point[n];
        for (int i = 0; i < n; i++) {
            points[i] = new Point(1, 1);
        }
        
                for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
        }       
         StdOut.println("====================================");
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
        }       
         StdOut.println("====================================");
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
        }       
         StdOut.println("====================================");
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
        }       
         StdOut.println("====================================");
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
        }       
         StdOut.println("====================================");
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
        }       
         StdOut.println("====================================");

    }
    
    
    
    
    
    
}
