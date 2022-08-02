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
        //Cache<BTreeNode> bTreeCache = new Cache<>();//Need to find max size
    }

    BTreeSearch(x,k) {

    }

    BTreeInsert(long DNAStrand) {

    }

    BTreeInsertNonfull(x,k) {

    }

    BTreeSplitChild(x,i,y) {

    }


    //**************** BTreeNode Start *****************
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
            numKeys = 0;
            leaf = false;

            keys = new TreeObject[(2*t) -1];
            children = new long[(2*t)];
        }

        public int getKeys() {
            return numKeys;
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

        public byte[] serialize() {
            int localVar = 4 + 8 + (12 * ((2 * degree) - 1) + (2 * degree));
            ByteBuffer bb = ByteBuffer.allocate(4 + 1 + 12 * (2 * degree - 1) + 8 * (2 * degree));
            bb.putInt(numKeys);
            if (leaf) {
                bb.put((byte)15);
            } else {
                bb.put((byte)0);
            }
            for (int i = 1; i <= numKeys; i++) {
                bb.putLong(keys[i].getDNA());
                bb.putInt(keys[i].getFrequency());
            }
            for (int i = 1; i <= numKeys + 1; i++) {
                bb.putLong(children[i]);
            }

            return bb.array();
        }

        public BTreeNode(ByteBuffer bb, int t) {
            degree = t;
            numKeys = bb.getInt();
            byte leafStatus = bb.get();
            leaf = leafStatus == 15;
            keys = new TreeObject[(2 * degree)];

            for (int i = 1; i <= numKeys; i++) {
                keys[i] = new TreeObject(bb.getLong(), bb.getInt());
            }

            children = new long[2 * degree];

            for (int i = 1; i <= numKeys + 1; i++) {
                children[i] = bb.getLong();
            }
        }
    }
}
