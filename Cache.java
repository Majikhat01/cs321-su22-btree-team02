import java.util.LinkedList;

public class Cache<T extends Comparable<Long>> {

    //Creates new linked list to act as cache
    private LinkedList<T> list;

    int maxSize;


    public Cache(int size){


        //Set max size of linked list
        maxSize = size;

        //Create linked list to be used as cache
        list = new LinkedList<T>();
        
    }

    public T getObject(long searchKey){
        for (T object : list) {
            if (object.compareTo(searchKey) == 0) {
                return object;
            }
        }
        return null;
    }

    public T addObject(T object) {
        T retVal = null;
        if (list.size() == maxSize) {
           retVal = list.removeLast();
        }
        list.addFirst(object);
        return retVal;
    }

    public void removeObject(T object) {

        list.remove(object);
    }

    public void clearCache() {

        list.clear();
    }

    public void moveObject(T object) {
        removeObject(object);
        addObject(object);
    }
}