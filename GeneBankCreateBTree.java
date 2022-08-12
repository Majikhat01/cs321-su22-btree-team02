import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.lang.model.util.ElementScanner6;

public class GeneBankCreateBTree 
{
    public static void main(String args[])
    {

        //System.out.println("The hardest part of any journey is the first step!");

        //This will need to contain the command line arguments
        //todo: check # and accuracy of arguments

        if(args.length < 4)
        {
            printUsageAndExit("Not enough arguments", 1);
        }

        //assign arguments to variables
        int cache = Integer.parseInt(args[0]);
        int degree = Integer.parseInt(args[1]);
        File gbkFile = new File(args[2]);
        int sequenceLength = Integer.parseInt(args[3]);
        int cacheSize = 0;
        int debugLevel = 0;
        boolean useCache = false;

        if (cache == 1)
        {
            useCache = true;
            cacheSize = Integer.parseInt(args[4]);
            if (cacheSize <= 0) {
                printUsageAndExit("Cache size must be greater than 0.", 1);
            }
            if(args.length == 6)
            {
                debugLevel = Integer.parseInt(args[5]);
            }
        }
        else if (cache == 0 && args.length == 5)
        {
            debugLevel = Integer.parseInt(args[4]);
        }
        else if (cache !=0 && cache != 1)
        {
            printUsageAndExit("cache argument should be 0 or 1", 1);
        }

        //todo: calculate degree

        //error checks
        if (cacheSize < 0)
        {
            printUsageAndExit("cache size should be positive", 1);
        }
        if (degree != 0 && degree < 2)
        {
            printUsageAndExit("degree argument should be >= 2", 1);
        }
        if (sequenceLength < 1 || sequenceLength > 31)
        {
            String message = ("sequence length argument should be greater than or equal to 1"
            + "and less than or equal to 31");
            printUsageAndExit(message, 1);
        }
        if (!(debugLevel ==0 || debugLevel ==1))
        {
            printUsageAndExit("debug level argument should be 0 or 1", 1);
        }



        //todo: initialize a btree with commandline parameters
        try
        {
            long startTime = System.currentTimeMillis();
            String fileName = args[2] + ".btree.data." + sequenceLength + "." + degree;
            System.out.print(fileName);
            BTree newTree = new BTree(fileName, sequenceLength, degree, cacheSize, useCache);

        //todo: write debug level support


        //try to parse file and insert long dna into the btree
            scanDNA(newTree, gbkFile, sequenceLength); //todo: add btree as a paramater
            newTree.writeMD();
            if (debugLevel == 1) {
                newTree.BTreeDump("dump");
            }
            long endTime = System.currentTimeMillis();
            System.out.println("\nTime:" + (endTime - startTime));
        }
        catch (IOException e)
        {
            e.printStackTrace(); //might want to chance that depending on debug support
        }

        if (debugLevel == 1)
        {
            String dumpName = (args[2] + "btree.dump." + sequenceLength);
            //newTree.dump(dumpName);
        }




    }

    public static void printUsageAndExit(String message, int i) {
        System.out.println(message);
        System.out.println("java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]");
        System.exit(i); //0 means no error, another number (1) means error
    }

    public static void printUsageAndExit(int i) {
        System.out.println("java GeneBankCreateBTree <0/1(no/with Cache)> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]");
        System.exit(i);
    }


    //scan file, discard text before origin, parse dna, insert long dna into btree
    public static void scanDNA(BTree T, File file, int sequenceLength) throws IOException //add btree as a parameter
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
            if (scan.hasNext()) {

            String messyDNAChunck = scan.next();
            String DNAChunk = messyDNAChunck.replaceAll("ORIGIN|\\n|\\s|[0-9]", "");
//            String[] DNAArray = DNAChunk.split("n+"); //one or more ns
//            for (int i = 0; i< DNAArray.length; i++)
//            {
//                String currentString = DNAArray[i];
                for (int j = 0; j <= DNAChunk.length() - sequenceLength; j++)
                {
                    //make sub a long and insert it into the btree
                    String sub = DNAChunk.substring(j, j + sequenceLength); //ceiling is exclusive
//                    currentString = currentString.substring(1);
                    if (!sub.contains("n")) {
                        long DNALong = stringToLong(sub);
                        T.BTreeInsert(DNALong);
//                        System.out.println("sub: " + sub);
                    }
                    //subLenK[index] = sub;
                    //index++;
                    //tree.BTreeInsert(DNALong);
//                }
            }

            }
            scan.useDelimiter("ORIGIN");


        }
        scan.close();
       // tree.writeMD();
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

        //System.out.println("retval: " + retval);
        return retval;
    }

    public static long charToNum(char letter)
    {
        long num = 0;
        //letter will be uppercase

        if(letter == 'C' || letter == 'c')
        {
            num = 1;
        }
        if(letter == 'G' || letter == 'g')
        {
            num = 2;
        }
        if(letter == 'T' || letter == 't')
        {
            num = 3;
        }

        return num;
    }
}
