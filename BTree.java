import java.io.FileNotFoundException;
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

    //Offset of BTreeNode in the byteArray
    private int offSetValue;

    //Need to serialize Btree
    public BTree(int k, int t, String fileName) throws FileNotFoundException {
        seqLength = k;
        degree = t;
        RandomAccessFile byteFile = new RandomAccessFile(fileName,"rw");

        offSetValue = 1000; //calculate by hand the size of each BTreeNode by terms of the degree
        // (2t-1)
        //allocate the first address to 0 then we will allocate by the offSetValue for each new BTreeNode

        //Specify location in the RAF for next address


    }

    public BTree(int k, int t, int cacheSize) {
        seqLength = k;
        degree = t;
        this.cacheSize = cacheSize;
        Cache<BTreeNode> bTreeCache = new Cache<>();//Need to find max size
    }



}
