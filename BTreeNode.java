public class BTreeNode {

    //Track location
    private long location;

    //# of keys
    private int n;

    //check if node is a leaf
    private boolean leaf;

    private TreeObject[] keys;

    private long[] children;

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

    public void setLeaf(boolean value) {
        leaf = value;
    }

    public void setChild(long fileLocation, int index) {
        children[index] = fileLocation;
    }
}
