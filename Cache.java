import java.util.LinkedList;

public class Cache<BTreeNode> extends BTree{

    //Creates new linked list to act as cache
    private LinkedList<BTreeNode> list;

    int maxSize;


    public Cache(int size){
        super();

        //Set max size of linked list
        maxSize = size;

        //Create linked list to be used as cache
        list = new LinkedList<BTreeNode>();
        
    }

    public BTreeNode getObject(BTreeNode object){
        if (list.indexOf(object) != -1) {
            return object;
        } else {
            return null;
        }
    }

    public void addObject(BTreeNode object) {
        if (list.size() == maxSize) {
            list.removeLast();
        }
        list.addFirst(object);
    }

    public void removeObject(BTreeNode object) {

        list.remove(object);
    }

    public void clearCache() {

        list.clear();
    }

    public void moveObject(BTreeNode object) {
        removeObject(object);
        addObject(object);
    }
}