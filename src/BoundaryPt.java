//Project 8.2 Maximum arc-Chord distance edge detector
//Shawn Yang
public class BoundaryPt {
    int x;
    int y;
    int maxVotes;
    double maxDist;
    int localMax;
    int corner;

    BoundaryPt(){
        this.x=-1;
        this.y=-1;
        maxVotes=0;
        maxDist=0.0;
        corner=1;
        localMax=0;
    }

    BoundaryPt(int x, int y){
        this.x=x;
        this.y=y;
        maxVotes=0;
        maxDist=0.0;
        corner=1;
        localMax=0;
    }

}
