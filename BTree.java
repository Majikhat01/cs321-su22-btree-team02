import java.io.*;
import java.nio.ByteBuffer;

public class BTree {

    //Offset of BTreeNode in the byteArray
    private long rootOffSet = 16;

    //Need root which is reference to byte address
    private BTreeNode root;

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




    private int nodeSize = 4 + 1 + 12 * (2 * degree - 1) + 8 * (2 * degree);

    // maintains the end of file
    private long EOFPointer;

    //Need to serialize Btree

    // class constructor
    public BTree(String fileName, int k, int t, int cacheSize) throws FileNotFoundException {
        byteFile = new RandomAccessFile(fileName, "rw");
        root = new BTreeNode(rootOffSet);
        seqLength = k;
        degree = t;
        this.cacheSize = cacheSize;
        //Cache<BTreeNode> bTreeCache = new Cache<>();//Need to find max size
    }


    public int BTreeSearch(BTreeNode x, long k) throws IOException {
        int i = 1;

        while (i <= x.getKeys() && k > x.keys[i].getDNA()) {
            i++;
        }

        if (i <= x.getKeys() && k == x.keys[i].getDNA()) {
            return x.keys[i].getFrequency();
        } else if (x.leaf) {
            return -1;
        } else {
            BTreeNode child = diskRead(x.children[i]);
            return BTreeSearch(child, k);
        }
    }

    // be careful not to add duplicate keys for Insert and Nonfull
    // if key to be inserted is a duplicate, just increment frequency
    public void BTreeInsert(long k) {


    }

    public void BTreeInsertNonfull(BTreeNode x, int k) {

    }

    public void BTreeSplitRoot(BTree T) {
    //We will call write root in this to update the root
        /*
            s = allocat-node()
            s.leaf = false
            s.n = 0
            s.c1 = T.root
            T.root = s
            B-Tree-Split-Child(s, 1)
            return s
         */
    }

    public void BTreeSplitChild(BTreeNode x, int i, int y) {

        /*
            y = ci
            z = allocat-node()
            z.leaf = y.leaf
            z.n = t-1
            for j = 1 to t -1
                z.key = y.keyj+t
            if not y.leaf
                for j = 1 to t
                    z.cj = y.cj+t
            y.n = t-1
            for j = x.n + 1 downto i + 1
                x.cj+1 = x.cj
            x.keyi+1 = x.keyj
            x.n = x.n +1
            DiskWrite(y)
            DiskWrite(z)
            DiskWrite(x)
         */
    }

    public void DumpTree(BTreeNode x, PrintStream ps) throws IOException {
        if (x.isLeaf()) {
            for (int i = 1; i < x.numKeys; i++) {
                ps.append(x.keys[i].toString() + "\n");
            }
        } else {
            for (int i = 1; i < x.numKeys + 1; i++) {
                BTreeNode child = new BTreeNode(x.children[i], degree);
                DumpTree(child, ps);
                ps.append(x.keys[i].toString() + "\n");
                child = diskRead(x.children[x.numKeys + 1]);
                DumpTree(child, ps);
            }
        }
    }

    public void BTreeDump(String fileName, BTree T) throws FileNotFoundException {
        PrintStream ps = new PrintStream(new File(fileName));
        PrintStream Stdout = System.out;
        DumpTree(T.root, ps);
        System.setOut(ps);
        System.setOut(Stdout);
    }

    public void diskWrite(BTreeNode node) throws IOException{
        //Take a BTreeNode and serialize it and then write that information to the RAF
        byteFile.seek(node.getLocation());
        byteFile.write(node.serialize());
    }

    public BTreeNode diskRead(long nodeAddress) throws IOException {
        //Read information from the RAF and return a BTreeNode
        byteFile.seek(nodeAddress);
        ByteBuffer bb = ByteBuffer.allocate(nodeSize);
        byteFile.read(bb.array());
        return new BTreeNode(bb);
    }

    public void writeMD() throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(16);
        byteFile.seek((long)0);
        bb.putLong(rootOffSet);
        bb.putInt(seqLength);
        bb.putInt(degree);
        try {
            byteFile.write(bb.array());
        } catch(IOException e) {
            System.out.println(e.toString());
        }
    }

    public void setRoot(long rootOffSet) {
        ByteBuffer bb = ByteBuffer.allocate(8);
        bb.putLong(rootOffSet);
        try {
            byteFile.seek(0);
            byteFile.write(bb.array());
        } catch(IOException e) {
            System.out.print(e.toString());
        }
    }

    public int calculateOptimumDegree() {
        //NodeSize <= 4096

        int optimalDegree = (int)Math.floor(4103/40);

        return optimalDegree;
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
        public BTreeNode(long location) {
            this.location = location;
            numKeys = 0;
            leaf = false;

            keys = new TreeObject[(2 * degree)];
            children = new long[(2 * degree) + 1];
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
            ByteBuffer bb = ByteBuffer.allocate(4 + 1 + 12 * (2 * degree - 1) + 8 * (2 * degree));
            bb.putInt(numKeys);

            //this inputs '1111' if true and '0000' if false into bb so that
            // leaf can be either toggled on or off when reading bb.
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

        public BTreeNode(ByteBuffer bb) {
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
