import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class GeneBankCreateBTree 
{
    public static void main(String args[]) {

        //System.out.println("The hardest part of any journey is the first step!");

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



        //todo: initialize a btree with commandline parameters
        //BTree newTree = null;

        //todo: write debug level support


        //try to parse file and insert long dna into the btree
        try
        {
            scanDNA(gbkFile, sequenceLength); //add btree as a paramater
            if (debugLevel == 1)
            {
                //todo: call dump method
            }

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


    //scan file, discard text before origin, parse dna, insert long dna into btree
    public static void scanDNA(File file, int sequenceLength) throws FileNotFoundException //add btree as a parameter
    {
        long dna = 0;
       // String[] subLenK = new String[100];
       // int index = 0;

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
                    //make sub a long and insert it into the btree
                    String sub = currentString.substring(0, sequenceLength-1);
                    currentString = currentString.substring(1);
                    long DNALong = stringToLong(sub); //todo: insert dnalong into btree
                    System.out.println("\n\n sub: " + sub);

                   // subLenK[index] = sub; 
                   // index++;
                }

            }
            scan.useDelimiter("ORIGIN");

        }
        scan.close();

    }


    public static long stringToLong(String subSequence)
    {
        long retval = 0;
        for (int i=0; i<subSequence.length()-1; i++)
        {
            char letter = subSequence.charAt(i);
            retval += charToNum(letter);
            retval = retval << 2;
        }
        char letter = subSequence.charAt(subSequence.length()-1);
        retval += charToNum(letter);




        return retval;
    }

    public static long charToNum(char letter)
    {
        long num = 0;
        //letter will be uppercase

        if(letter == 'C')
        {
            num=1;
        }
        if(letter == 'G')
        {
            num = 2;
        }
        if(letter == 'T')
        {
            num = 3;
        }

        return num;
    }
}
