import java.io.File;
import java.util.Scanner;

public class GeneBankCreateBTree {
    public static void main(String args[]) {

        System.out.println("The hardest part of any journey is the first step!");

        //This will need to contain the command line arguments
    }

    public static void printUsageAndExit(String message, int i) {
        System.out.println(message);
        System.out.println("java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]");
        System.exit(i);
    }

    public static void printUsageAndExit(int i) {
        System.out.println("java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]");
        System.exit(i);
    }

    //Need to scan and avoid all patterns until we reach ORIGIN
    //Then scan line by line
    public static long scanDNA(File file) {
        long dna = 0;

        Scanner scan = new Scanner();
        return dna;
    }
}
