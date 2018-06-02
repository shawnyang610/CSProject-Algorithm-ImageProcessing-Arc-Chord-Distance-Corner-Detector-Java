//Project 8.2 Maximum arc-Chord distance edge detector
//Shawn Yang
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import static java.lang.Math.*;


public class ArcChord {
    Scanner infile;
    PrintWriter outfile1;
    PrintWriter outfile2;
    PrintWriter outfile3;
    int numRows;
    int numCols;
    int minVal;
    int maxVal;
    int label;
    int numPts;
    int k;
    int chordLength;
    BoundaryPt[] ptAry;
    double [] chordAry;
    Image img;

    ArcChord(String locationInfile, String locationOut1, String locationOut2,
             String locationOut3){
        //step1
        try{
            infile= new Scanner(new FileReader(locationInfile));
            outfile1=new PrintWriter(locationOut1);
            outfile2=new PrintWriter(locationOut2);
            outfile3=new PrintWriter(locationOut3);
        }catch (IOException e){
            System.out.println("error opening files.");
            System.exit(1);
        }
        numRows=infile.nextInt();
        numCols=infile.nextInt();
        minVal=infile.nextInt();
        maxVal=infile.nextInt();
        label=infile.nextInt();
        numPts=countPts(infile);
        infile.close();
        try{
            infile=new Scanner(new FileReader(locationInfile));
        }catch(IOException e){
            System.out.println("error opening input file for the second time.");
            System.exit(1);
        }
        ptAry=new BoundaryPt[numPts];
        loadData(infile);
        System.out.print("Please enter a value for K:");
        k= (new Scanner(System.in)).nextInt();
        System.out.println("you entered k="+k);
        chordLength=2*k;
        chordAry= new double[chordLength];
    }
    void run(){
        //1
        int p1=0;
        int p2=chordLength-1;
        int index;
        int currPt;
        int maxIndex;
        int whichIndex;
        while (true) {
            //2
            index = 0;//at start: index=0 == currPt == p1+1
            currPt = (p1 + 1) % numPts;
            //3
            while (index < chordLength-2) { //exclusive of p1 and p2,
                //this leaves the the values
                //of the last 2 spot of ptAry 0.
                chordAry[index] = computeDistance(p1, p2, currPt);
                index++;
                currPt = (currPt + 1) % numPts;
            }//4
            //5
            print_chordAry(outfile3);
            //6
            maxIndex = findMax(chordAry);
            whichIndex = (maxIndex + p1) % numPts;
            ptAry[whichIndex].maxVotes++;
            if (ptAry[whichIndex].maxDist < chordAry[maxIndex])
                ptAry[whichIndex].maxDist = chordAry[maxIndex];
            //7
            printPtAry(p1, p2, outfile3);
            //8
            p1 = (p1 + 1) % numPts;
            p2 = (p2 + 1) % numPts;
            if (p2==chordLength-1)
                break;
        }//9
        //10
        printPtAry(0,numPts-1, outfile3);
        //11
        computeLocalMaxima(ptAry);
        //12
        setCorner(ptAry);
        //13
        print2Output1();
        //14
        img= new Image(numRows, numCols, minVal, maxVal);
        //15
        img.plotPt2Img(ptAry);
        //16
        img.prettyPrint(outfile2);
        //17
        infile.close();
        outfile1.close();
        outfile2.close();
        outfile3.close();

    }
    int countPts(Scanner infile){
        int trash;
        int numPts=0;
        while (infile.hasNextInt()){
            trash = infile.nextInt();
            numPts++;
        }
        System.out.println("Total points:"+numPts/2);
        return numPts/2;
    }
    void printPtAry(int p1, int p2, PrintWriter outfile){
        int index=0;
        outfile.println("ptAry: starting from p1's index:"+p1);
        for (int i=0; i<numPts; i++){
            index=(i+p1)%numPts;
            outfile.println("#"+index+" "+ptAry[index].y+" "+ptAry[index].x+" maxDist:"+
                    ptAry[index].maxDist+" maxVotes:"+ptAry[index].maxVotes+" Corner:"+
                    ptAry[index].corner
            );
        }
    }

    void loadData(Scanner infile){
        int x;
        int y;
        int index=0;
        int trash;
        for (int i=0; i<5; i++)
            trash=infile.nextInt();
        while (infile.hasNext()) {
            y=infile.nextInt();
            x=infile.nextInt();
            ptAry[index]=new BoundaryPt(x,y);

            index++;
        }
    }

    double computeDistance(int p1, int p2, int currP){
        int x1=ptAry[p1].x;
        int y1=ptAry[p1].y;
        int x2=ptAry[p2].x;
        int y2=ptAry[p2].y;
        int x=ptAry[currP].x;
        int y=ptAry[currP].y;
        double A=(double)y2-y1;
        double B=(double)x1-x2;
        double C=(double)x2*y1-(double)x1*y2;
        double numerator= abs(A*x+B*y+C);
        double denominator= sqrt(A*A+B*B);
        double d= numerator / denominator;
        return d;
    }
    int findMax(double [] in_chordAry){
        int maxIndex=0;
        for (int i=0; i<in_chordAry.length-2;i++) {
            if (in_chordAry[i]>maxIndex)
                maxIndex=i;
        }
        return maxIndex;
    }
    void computeLocalMaxima(BoundaryPt[] ptAry){
        for (int p=0; p<numPts; p++){
           if (ptAry[p].maxVotes>=ptAry[(p+numPts-2)%numPts].maxVotes&&
                   ptAry[p].maxVotes>=ptAry[(p+numPts-1)%numPts].maxVotes&&
                   ptAry[p].maxVotes>=ptAry[(p+1)%numPts].maxVotes&&
                   ptAry[p].maxVotes>=ptAry[(p+2)%numPts].maxVotes)
               ptAry[p].localMax=1;
           else
               ptAry[p].localMax=0;
        }
    }
    void setCorner (BoundaryPt [] ptAry){
        for (int p=0; p<numPts; p++){
            if (ptAry[p].localMax==1 && ptAry[(p+numPts-2)%numPts].localMax!=1
                    &&ptAry[(p+2)%numPts].localMax!=1)
                ptAry[p].corner=9;
            else
                ptAry[p].corner=1;
        }
    }
    void print_chordAry(PrintWriter outfile){
        outfile.println("chordAry:");
        for (int i=0; i< chordLength; i++){
            outfile.println("#"+i+":"+chordAry[i]);
        }
    }
    void print2Output1(){
        outfile1.println(numRows+" "+numCols+" "+minVal+" "+maxVal);
        outfile1.println(label);
        outfile1.println(numPts);
        for (int i=0; i< numPts; i++)
            outfile1.println(ptAry[i].y+" "+ptAry[i].x+" "+ptAry[i].corner);
    }



}
