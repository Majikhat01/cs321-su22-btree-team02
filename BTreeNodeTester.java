import java.io.IOException;
import java.nio.ByteBuffer;



public class BTreeNodeTester {

    public static void main(String[] args) {
        BTreeNode n = new BTreeNode(1000);
        n.keys[1] = TreeObject(1, 1);
        n.keys[2] = TreeObject(2, 2);
        n.keys[3] = TreeObject(3, 3);
        n.children[1] = 1000;
        n.children[2] = 2000;
        n.children[3] = 3000;
        n.children[4] = 4000;

        byte[] node = n.serialize();
        ByteBuffer bb = ByteBuffer.allocate(0);
        bb.array() = node;
        BTreeNode n2 = new BTreeNode(bb);

        Compare(n, n2);
    }

    public static boolean Compare(BTreeNode n, BTreeNode n2) {

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

        public byte[] serialize() throws IOException {
            ByteBuffer bb = ByteBuffer.allocate(4 + 8 + (12 * ((2 * degree) - 1) + (2 * degree)));//pass in the amount of bytes each BTreeNode is gonna be
            bb.putInt(numKeys);
            if (leaf == true) {
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
            if (leafStatus == 15) {
                leaf = true;
            } else {
                leaf = false;
            }
            keys = new TreeObject[(2 * degree)];
            for (int i = 1; i < numKeys; i++) {
                keys[i] = new TreeObject(bb.getLong(), bb.getInt());
            }
            children = new long[2 * degree];
            for (int i = 1; i <= numKeys + 1; i++) {
                children[i] = bb.getLong();
            }
        }
    }


}
