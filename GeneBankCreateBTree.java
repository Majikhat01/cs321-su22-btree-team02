import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class GeneBankCreateBTree {
    public static void main(String args[]) {

        System.out.println("The hardest part of any journey is the first step!");

        //This will need to contain the command line arguments
        //todo: check # and accuracy of arguments

        //assign arguments to variables
        int cache = Integer.parseInt(args[0]);
        int degree = Integer.parseInt(args[1]);
        File gbkFile = new File(args[2]);
        int sequenceLength = Integer.parseInt(args[3]);
        int cacheSize = 0;
        int debugLevel = 0;

        if (cache == 1)
        {
            cacheSize = Integer.parseInt(args[4]);
            if(args.length == 6)
            {
                debugLevel = Integer.parseInt(args[5]);
            }
        }
        else if (cache == 0 && args.length == 5)
        {
            debugLevel = Integer.parseInt(args[4]);
        }

        //todo: write debug level support
        try
        {
            long scannedDNA = scanDNA(gbkFile);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace(); //might want to chance that depending on debug support
        }


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
    public static long scanDNA(File file) throws FileNotFoundException //change return type
    {
        long dna = 0;
        String[] subLenK = new String[100];
        int index = 0;

        Scanner scan = new Scanner(file);
        scan.useDelimiter("ORIGIN");

        while (scan.hasNext())
        {
            String unwanted = scan.next();
            scan.useDelimiter("//");
            String messyDNAChunck = scan.next();
            String DNAChunk = messyDNAChunck.replaceAll("(\\n)(\\s)[0-9]", "");
            String[] DNAArray = DNAChunk.split("n+"); //one or more ns
            for (int i = 0; i< DNAArray.length; i++)
            {
                String currentString = DNAArray[i];
                while(currentString.length() >= sequenceLength)
                {
                    String sub = currentString.substring(0, sequenceLength-1);
                    currentString = currentString.substring(1);
                    subLenK[index] = sub;
                    index++;
                }

            }
            scan.useDelimiter("ORIGIN");

        }






        return dna;
    }
}
