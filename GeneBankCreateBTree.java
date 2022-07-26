public class GeneBankCreateBTree {
    public static void main(String args[]) {

        System.out.println("The hardest part of any journey is the first step!");

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

}
