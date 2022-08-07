import java.io.IOException;
import java.nio.ByteBuffer;

public class BTreeNodeTester {

    public static void main(String[] args) throws IOException {
        BTree newTree = new BTree("test", 2, 2, 0);
        newTree.BTreeInsert(1);
        newTree.BTreeInsert(2);
        newTree.BTreeInsert(3);
        System.out.print(newTree.getNodeAtIndex(1) + "\n");
        newTree.BTreeInsert(4);
        System.out.print(newTree.getNodeAtIndex(1) + "\n");
        System.out.print(newTree.getNodeAtIndex(2) + "\n");
        System.out.print(newTree.getNodeAtIndex(3) + "\n");
        newTree.BTreeInsert(5);
        newTree.BTreeInsert(6);
        System.out.print(newTree.getNodeAtIndex(1) + "\n");
        System.out.print(newTree.getNodeAtIndex(2) + "\n");
        System.out.print(newTree.getNodeAtIndex(3) + "\n");
        System.out.print(newTree.getNodeAtIndex(4) + "\n");
    }

    public static boolean Compare(BTreeNode n, BTreeNode n2) {

        boolean nodesMatch = false;

        nodesMatch = n.location == n2.location;

        nodesMatch = n.numKeys == n2.numKeys;

        for (int i = 1; i <= n.getKeys(); i++) {
            if (n.keys[i] == n2.keys[i]) {
                nodesMatch = true;
            } else {
                nodesMatch = false;
                break;
            }
        }

        for (int i = 1; i <= n.getKeys() + 1; i++) {
            if (n.children[i] == n2.children[i]) {
                nodesMatch = true;
            } else {
                nodesMatch = false;
                break;
            }
        }

        return nodesMatch;
    }


    //BTreeNode Start
    public static class BTreeNode {

        int degree;

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
            degree = t;
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
