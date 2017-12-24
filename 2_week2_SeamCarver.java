import java.awt.Color;
import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

   private Picture pic;
   private int[][] edgeFrom;
   private int width, height;

   public SeamCarver(Picture picture)                // create a seam carver object based on the given picture
   {
      if (picture == null ) throw new java.lang.IllegalArgumentException("picture is nulls");
      this.pic = new Picture(picture);
      this.width = this.pic.width();
      this.height = this.pic.height();

   }
      
   public Picture picture(){                          // current picture
      return new Picture(pic);
   }
    
   public int width(){                            // width of current picture
      return this.width;
   }
   
   public int height(){                           // height of current picture
      return this.height;
   }
   
   /*
   private void reset(){
      this.width = this.pic.width();
      this.height = this.pic.height();
      energyMap = new double[this.height][this.width];
      distTo = new double[this.height][this.width];
      edgeFrom = new int[this.height][this.width];
      
      for (int i = 0; i < this.height; i ++){
         for (int j = 0; j < this.width; j ++){
            energyMap[i][j] = this.energy(j,i);
         }
      }
   }
   */
   
   public  double energy(int x, int y){               // energy of pixel at column x and row y
      if (x >= this.width || x < 0 ) throw new java.lang.IllegalArgumentException("X out of the range");
      if (y >= this.height || y < 0 ) throw new java.lang.IllegalArgumentException("Y out of the range" + y +" > " + height);
      
      if (x == 0 || y == 0 || x == this.width-1 || y == this.height-1) return 1000;
      Color color_L = this.pic.get(x-1, y);
      Color color_R = this.pic.get(x+1, y);
      Color color_T = this.pic.get(x, y-1);
      Color color_B = this.pic.get(x, y+1);
      int Rx = color_L.getRed()-color_R.getRed();
      int Gx = color_L.getGreen()-color_R.getGreen();
      int Bx = color_L.getBlue()-color_R.getBlue();
      int Ry = color_T.getRed()-color_B.getRed();
      int Gy = color_T.getGreen()-color_B.getGreen();
      int By = color_T.getBlue()-color_B.getBlue();
      return Math.sqrt( (double) (Rx * Rx + Gx * Gx + Bx * Bx + Ry * Ry + Gy * Gy + By * By));
   }

   public int[] findHorizontalSeam(){               // sequence of indices for horizontal seam
      edgeFrom = new int[this.height][this.width];
      double[] dpmap = new double[this.height];
      int[] seam = new int[this.width];
      int tmp = this.height -1;
      double keeper = 1000;
      double curenergy = 0;
      for (int i = 0; i < this.height; i ++){
         dpmap[i] = 1000;
      }
      
      for (int j = 1; j < this.width; j ++){
         
         dpmap[0] += 1000;
         dpmap[tmp] += 1000;
         edgeFrom[0][j]= 0;
         edgeFrom[tmp][j] = tmp;
         keeper = dpmap[0];
         
         for (int i =1; i < tmp; i ++){
            curenergy = this.energy(j, i);
            
            if (dpmap[i] <= keeper) {
               keeper = dpmap[i];
               if (dpmap[i] <= dpmap[i+1]){
                  dpmap[i] += curenergy;
                  edgeFrom[i][j] = i;
               }else{
                  dpmap[i] = curenergy + dpmap[i+1];
                  edgeFrom[i][j] = i+1;
               }
            }else if( keeper <= dpmap[i+1]){
               double prekeeper = keeper;
               keeper = dpmap[i];
               dpmap[i] = prekeeper + curenergy;
               edgeFrom[i][j] = i-1;
            }else{
               keeper = dpmap[i];
               dpmap[i] = curenergy + dpmap[i+1];
               edgeFrom[i][j] = i+1;
            }
         }
  
      }
  
      
      double min = Double.POSITIVE_INFINITY;
      tmp = this.width-1;
      
      for (int i = 0; i < this.height; i ++){
         if ( min > dpmap[i] ){
            min = dpmap[i];
            seam[tmp] = i;
         }
      }
      for (int j = tmp; j > 0; j--){
         seam[j-1] = edgeFrom[seam[j]][j];
      }
     return seam;
   }
   
   
   public int[] findVerticalSeam(){                 // sequence of indices for vertical seam
      edgeFrom = new int[this.height][this.width];
      double[] dpmap = new double[this.width];
      int[] seam = new int[this.height];
      int tmp = this.width -1;
      double keeper = 1000;
      double curenergy = 0;
      for (int j = 0; j < this.width; j ++){
         dpmap[j] = 1000;
      }
      
      for (int i = 1; i < this.height; i ++){
         
         dpmap[0] += 1000;
         dpmap[tmp] += 1000;
         edgeFrom[i][0]= 0;
         edgeFrom[i][tmp] = tmp;
         keeper = dpmap[0];
         

         
         for (int j =1; j < tmp; j ++){
            curenergy = this.energy(j, i);
            
            if (dpmap[j] <= keeper) {
               keeper = dpmap[j];
               if (dpmap[j] <= dpmap[j+1]){
                  dpmap[j] += curenergy;
                  edgeFrom[i][j] = j;
               }else{
                  dpmap[j] = curenergy + dpmap[j+1];
                  edgeFrom[i][j] = j+1;
               }
            }else if( keeper <= dpmap[j+1]){
               double prekeeper = keeper;
               keeper = dpmap[j];
               dpmap[j] = prekeeper + curenergy;
               edgeFrom[i][j] = j-1;
            }else{
               keeper = dpmap[j];
               dpmap[j] = curenergy + dpmap[j+1];
               edgeFrom[i][j] = j+1;
            }
         }
      }
      
     
      double min = Double.POSITIVE_INFINITY;
      tmp = this.height-1;
      
      
      for (int j = 0; j < this.width; j ++){
         if ( min > dpmap[j] ){
            min = dpmap[j];
            seam[tmp] = j;
         }
      }

      for (int i = tmp; i > 0; i--){
         seam[i-1] = edgeFrom[i][seam[i]];
      }
     return seam;
   }
   
   
   
   
   public void removeHorizontalSeam(int[] seam)   // remove horizontal seam from current picture
   {
      if (seam == null ) throw new java.lang.IllegalArgumentException( "seam is null" );
      if (seam.length != this.width()) throw new java.lang.IllegalArgumentException( "invalid length of seam,width");
      if (this.height() <= 1) throw new java.lang.IllegalArgumentException( "picture height less than 1" );
      //if (!isHorizontal) throw new java.lang.IllegalArgumentException("invalid seam");
      if ( !validSeam(seam, this.height()) ) throw new java.lang.IllegalArgumentException( "invalid seam");
      
      Picture tmp = new Picture(this.width, this.height-1);
      
      for (int j =0; j < this.width; j ++){
         for (int i =0; i < seam[j]; i ++){
            tmp.setRGB(j, i, pic.getRGB(j, i));
         }
         for (int i = seam[j]; i < this.height-1; i ++){
            tmp.setRGB(j, i, pic.getRGB(j, i+1));
         }
      }
      
      this.pic = tmp;    
      this.width = this.pic.width();
      this.height = this.pic.height();

   }
   
   
   public void removeVerticalSeam(int[] seam)     // remove vertical seam from current picture
   {
      if ( seam == null ) throw new java.lang.IllegalArgumentException( "seam is null" );
      if (seam.length != this.height()) throw new java.lang.IllegalArgumentException( "invalid length of seam,height");
      if (this.width() <= 1) throw new java.lang.IllegalArgumentException( "picture width less than 1" );
      //if (isHorizontal) throw new java.lang.IllegalArgumentException("invalid seam");
      if ( !validSeam(seam, this.width()) ) throw new java.lang.IllegalArgumentException( "invalid seam");
      
      Picture tmp = new Picture(this.width-1, this.height);
      
      for (int i =0; i < this.height; i ++){
         for (int j =0; j < seam[i]; j ++){
            tmp.setRGB(j, i, pic.getRGB(j, i));
         }
         for (int j = seam[i]; j < this.width-1; j ++){
            tmp.setRGB(j, i, pic.getRGB(j+1, i));
         }
      }
                  
      this.pic = tmp;
      this.width = this.pic.width();
      this.height = this.pic.height();

   }
   
   private boolean validSeam(int[] seam, int range){
      if (seam == null ) return false;
      if (seam.length < 2) return true;
      int cur = seam[0];
      for (int i : seam){
         if (i >= range || i < 0 ) return false;
         if ( cur > i +1 || cur < i -1 ) return false;
         cur = i;
      }
      return true;
   }
}
