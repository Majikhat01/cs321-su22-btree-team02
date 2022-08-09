import java.io.*;
import java.nio.ByteBuffer;
import java.util.LinkedList;

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

    private long nextAddress;

    //RAF to create new file to store all BTree information
    private RandomAccessFile byteFile;

    private int nodeSize = 4 + 1 + 12 * (2 * degree - 1) + 8 * (2 * degree);

    // maintains the end of file
    private long EOFPointer;

    //Need to serialize Btree

    // class constructor
    public BTree(String fileName, int k, int t, int cacheSize, boolean useCache) throws FileNotFoundException {
        byteFile = new RandomAccessFile(fileName, "rw");
        degree = t;
        root = new BTreeNode(rootOffSet);
        root.leaf = true;
        seqLength = k;

        if (degree == 0) {
            degree = calculateOptimumDegree();
        }

        nodeSize = 4 + 1 + 12 * (2 * degree - 1) + 8 * (2 * degree);
        nextAddress = rootOffSet + nodeSize;

        if (useCache) {
            Cache<BTreeNode> BTreeCache = new Cache<>(cacheSize);
        }

        //Cache<BTreeNode> bTreeCache = new Cache<>();//Need to find max size
    }

//    public BTree(int cacheSize, string filePath)
//    {
//
//    }

    public String getNodeAtIndex(int index) throws IOException {
        if (index < 1) {
            return null;
        }

        LinkedList<BTreeNode> Q = new LinkedList<>();
        Q.addFirst(root);

//        if (root.children[1] != 0) {
//            for (int i = 1; i <= root.numKeys + 1; i++) {
//                BTreeNode child = diskRead(root.children[i]);
//                Q.addFirst(child);
//            }
//
//            // this works for the test up to 3 levels, but doesn't work for 4.
//            // probably need to do some recursive trickery
//            for (int i = 1; i <= root.numKeys + 1; i++) {
//                BTreeNode child = diskRead(root.children[i]);
//                for (int j = 1; j <= child.numKeys + 1; j++) {
//                    BTreeNode childsChild = diskRead(child.children[j]);
//                    Q.addFirst(childsChild);
//                }
//            }
//        }


        for (int j= 1; j <= index - 1; j++) {
            BTreeNode node = Q.removeLast();

            if (!node.leaf) {
                for (int k = 1; k <= node.numKeys + 1; k++) {
                    BTreeNode child = diskRead(node.children[k]);
                    Q.addFirst(child);
                }
            }
        }

        BTreeNode node = Q.removeLast();
        return node.toString();
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

    public int searchStart(long k)
    {
       return BTreeSearch(root, k);
    }

    // be careful not to add duplicate keys for Insert and Nonfull
    // if key to be inserted is a duplicate, just increment frequency
    public void BTreeInsert(long k) throws IOException {

        BTreeNode r = root;
        if (r.getKeys() == ((2 * degree) - 1)) {
            BTreeNode s = new BTreeNode(nextAddress);
            nextAddress = nextAddress + nodeSize; //allocate node
            this.root = s;
            s.setLeaf(false);
            s.setKeys(0);
            s.children[1] = r.getLocation();


            BTreeSplitChild(s, 1, r);
            BTreeInsertNonfull(s, k);
        } else {
            BTreeInsertNonfull(r, k);
        }
    }

    public void BTreeInsertNonfull(BTreeNode x, long k) throws IOException {
        int i = x.numKeys;

        if (x.leaf) {

            //checks for duplicate keys before inserting
            for (int j = 1; j <= x.numKeys; j++) {
                if (k == x.keys[j].getDNA()) {
                    x.keys[j].setFrequency(x.keys[j].getFrequency() + 1);
                    diskWrite(x);
                    return;
                }
            }
            while (i >= 1 && k < x.keys[i].getDNA()) {
                x.keys[i + 1] = x.keys[i];
                i--;
            }

            x.keys[i + 1] = new TreeObject(k, 1);
            x.numKeys = x.numKeys + 1;
            diskWrite(x);
        } else {
            while (i >= 1 && k < x.keys[i].getDNA()) {
                i--;
            }

            i++;
            BTreeNode child = diskRead(x.children[i]);

            if (child.numKeys == (2 * degree) - 1) {
                BTreeSplitChild(x, i, child);

                if (k > x.keys[i].getDNA()) {
                    i++;
                }

                //update the child node to be inserted
                child = diskRead(x.children[i]);
            }

            //checks for duplicate keys before inserting
            for (int j = 1; j <= x.numKeys; j++) {
                if (k == x.keys[j].getDNA()) {
                    x.keys[j].setFrequency(x.keys[j].getFrequency() + 1);
                    diskWrite(x);
                    return;
                }
            }
            BTreeInsertNonfull(child, k);
        }
    }

    public BTreeNode BTreeSplitRoot(BTree T) throws IOException {
    //We will call write root in this to update the root
        BTreeNode s = new BTreeNode(nextAddress);
        nextAddress = nextAddress + nodeSize;
        BTreeNode r = new BTreeNode(nextAddress);
        nextAddress = nextAddress + nodeSize;
        s.leaf = false;
        s.numKeys = 0;
        s.children[1] = T.rootOffSet;
        T.root = s;
        BTreeSplitChild(s, 1, r);
        return s;
    }

    public void BTreeSplitChild(BTreeNode x, int i, BTreeNode y) throws IOException {

        BTreeNode z = new BTreeNode(nextAddress);
        nextAddress = nextAddress + nodeSize;
        z.leaf = y.leaf;
        z.setKeys(degree - 1);

        for (int j = 1; j <= degree - 1; j++) {
            z.keys[j] = y.keys[j + degree];
        }

        if (!y.leaf) {
            for (int j = 1; j <= degree; j++) {
                z.children[j] = y.children[j + degree];
            }
        }

        y.numKeys = degree - 1;

        for (int j = x.numKeys + 1; j >= i + 1; j--) {
            x.children[j + 1] = x.children[j];
        }

        x.children[i + 1] = z.location;

        for (int j = x.numKeys; j >= i; j--) {
            x.keys[j + 1] = x.keys[j];
        }

        x.keys[i] = y.keys[degree];
        x.numKeys = x.numKeys + 1;
        diskWrite(x);
        diskWrite(y);
        diskWrite(z);
    }

    public void DumpTree(BTreeNode x, PrintStream ps) throws IOException {
        if (x.isLeaf()) {
            for (int i = 1; i <= x.numKeys; i++) {
                ps.append(x.keys[i].toString(seqLength) + "\n");
            }
        } else {
            for (int i = 1; i < x.numKeys + 1; i++) {
                BTreeNode child = diskRead(x.children[i]);
                DumpTree(child, ps);
                ps.append(x.keys[i].toString(seqLength) + "\n");
            }
            DumpTree(diskRead(x.children[x.numKeys + 1]), ps);
        }
    }

    public void BTreeDump(String fileName) throws IOException {
        PrintStream ps = new PrintStream(new File(fileName));
        PrintStream Stdout = System.out;
        DumpTree(this.root, ps);
        System.setOut(ps);
        System.setOut(Stdout);
    }

    public void diskWrite(BTreeNode node) throws IOException{
        //Take a BTreeNode and serialize it and then write that information to the RAF
        long address = node.getLocation();
        byteFile.seek(node.getLocation());
        byteFile.write(node.serialize());
    }

    public BTreeNode diskRead(long nodeAddress) throws IOException {
        //Read information from the RAF and return a BTreeNode
        byteFile.seek(nodeAddress);
        ByteBuffer bb = ByteBuffer.allocate(nodeSize);
        byteFile.read(bb.array());
        return new BTreeNode(bb, nodeAddress);
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

    public BTreeNode getRoot() {
        return root;
    }

    public void setRoot() {
        ByteBuffer bb = ByteBuffer.allocate(8);
        bb.putLong(root.location);
        try {
            byteFile.seek(0);
            byteFile.write(bb.array());
        } catch(IOException e) {
            System.out.print(e.toString());
        }

        //Call setRoot to update the final root value of where it's at one time finally in GeneBankCreateBTree
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

        public void setKeys(int keySize) {
             numKeys = keySize;
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

        public boolean isEmpty() {
            return (this.keys.length == 0);
        }

        @Override
        public String toString() {
            String retVal = "";
            for (int i = 1; i <= this.getKeys(); i++) {
                retVal += this.keys[i].getDNA() + ", ";
            }
            return retVal;
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

        public BTreeNode(ByteBuffer bb, long location) {
            this.location=location;
            numKeys = bb.getInt();
            byte leafStatus = bb.get();
            leaf = leafStatus == 15;
            keys = new TreeObject[(2 * degree)];

            for (int i = 1; i <= numKeys; i++) {
                keys[i] = new TreeObject(bb.getLong(), bb.getInt());
            }

            children = new long[(2 * degree) +1];

            for (int i = 1; i <= numKeys + 1; i++) {
                children[i] = bb.getLong();
            }
        }
    }
}
