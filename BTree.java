import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

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

    //BTreeNode Start
    public class BTreeNode {

        //Track location
        private long location;

        //# of keys
        private int numKeys;

        //check if node is a leaf
        private boolean leaf;

        private TreeObject[] keys;

        private long[] children;

        // t = degree
        public BTreeNode(long location, int t) {
            this.location = location;
            n = 0;
            leaf = false;

            keys = new TreeObject[(2*t) -1];
            children = new long[(2*t)];
        }

        public int getKeys() {
            return n;
        }

        public long getLocation() {
            return location;
        }

        public boolean isLeaf() {
            return leaf;
        }

        public int setDegree(int n) {
            return degree = n;
        }

        public void setLeaf(boolean value) {
            leaf = value;
        }

        public void setChild(long fileLocation, int index) {
            children[index] = fileLocation;
        }

        public byte[] serialize() throws IOException {
            ByteBuffer bb = ByteBuffer.allocate(4 + 8 + (12 * ((2 * degree) - 1) + (2 * degree)));//pass in the amount of bytes each BTreeNode is gonna be
            bb.putInt(n);
            bb.put((byte)15); //put into if (leaf = 15 otherwise 0)
            for (int i = 1; i <= n; i++) {
                bb.putLong(keys[i].getDNA());
                bb.putInt(keys[i].getFrequency());
            }
            for (int i = 1; i <= n + 1; i++) {
                bb.putLong(children[i]);
            }

            return bb.array();
        }

        public BTreeNode(ByteBuffer bb) {
            degree = bb.getInt();
            byte leafStatus = bb.get();
            if (leafStatus == 15) {
                leaf = true;
            } else {
                leaf = false;
            }
            numKeys = bb.getInt();
        }
    }
}
