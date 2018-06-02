//Project 8.2 Maximum arc-Chord distance edge detector
//Shawn Yang

public class Main {
    public static void main(String[] args) {

        if (args.length<4){
            System.out.println("Wrong arguments, try again");
            System.exit(1);
        }

        ArcChord arcChord = new ArcChord(args[0], args[1], args[2], args[3]);
        arcChord.run();
    }
}
