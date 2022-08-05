import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GeneBankSearchBTree {
    public static void main(String args[]) {

        //todo: check # and accuracy of arguments

        //assign arguments to variables
        int cache = Integer.parseInt(args[0]);
        String BTreeFile = new String(args[1]);
        File queryFile = new File(args[2]);
        int cacheSize = 0;
        int debugLevel = 0;

        if (cache == 1)
        {
            cacheSize = Integer.parseInt(args[3]);
            if(args.length == 4)
            {
                debugLevel = Integer.parseInt(args[4]);
            }
        }
        else if (cache == 0 && args.length == 3)
        {
            debugLevel = Integer.parseInt(args[4]);
        }



        //todo: initialize a btree with commandline parameters
        //BTree newTree = null;

        //todo: write debug level support


        //try to parse file
        try
        {
            Scanner scan = new Scanner(queryFile);
            while(scan.hasNextLine())
            {
                String query = scan.nextLine();
                
                long searchKey = GeneBankCreateBTree.stringToLong(query);
                //call btree search(search key)
            }

        }
        catch(FileNotFoundException e)
        {

        }

    }

    public static void printUsageAndExit(String message, int i) {
        System.out.println(message);
        System.out.println("java GeneBankSearch <0/1(no/with Cache)> <btree file> <query file> [<cache size>] [<debug level>]");
        System.exit(i);
    }

    public static void printUsageAndExit(int i) {
        System.out.println("java GeneBankSearch <0/1(no/with Cache)> <btree file> <query file> [<cache size>] [<debug level>]");
        System.exit(i);
    }

}
