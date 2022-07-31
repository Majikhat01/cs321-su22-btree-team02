import java.io.RandomAccessFile;

public class BTree {

    //Need root which is reference to byte address
    private long root;

    //K = substring length
    private int seqLength;

    //Degree of the tree
    private int degree;

    //Potential Cache Size
    private int cacheSize;

    //Cache
    private Cache cache;

    //RAF to create new file to store all BTree information
    private RandomAccessFile byteFile;

    public BTree(int k, int t) {
        seqLength = k;
        degree = t;
    }

    public BTree(int k, int t, int cacheSize) {
        seqLength = k;
        degree = t;
        this.cacheSize = cacheSize;
    }



}
