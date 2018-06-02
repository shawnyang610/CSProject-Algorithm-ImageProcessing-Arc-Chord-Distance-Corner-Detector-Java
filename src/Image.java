//Project 8.2 Maximum arc-Chord distance edge detector
//Shawn Yang
import java.io.PrintWriter;

public class Image {
    int numRows;
    int numCols;
    int minVal;
    int maxVal;
    int[][] img;
    Image(int numRows, int numCols, int minVal, int maxVal){
        this.numRows=numRows;
        this.numCols=numCols;
        this.minVal=minVal;
        this.maxVal=maxVal;
        img= new int[numRows][numCols];
    }
    public void plotPt2Img(BoundaryPt[] ptAry){
        for (int i=0; i<ptAry.length; i++)
            img[ptAry[i].y][ptAry[i].x]=ptAry[i].corner;
    }

    public void prettyPrint(PrintWriter outfile){
        for (int i=0; i<numRows;i++){
            for (int j=0; j<numCols; j++){
                if (img[i][j]>0) {
                    outfile.print(img[i][j] + " ");
                }
                else outfile.print("  ");
            }
            outfile.println();
        }
    }

}
