import java.util.LinkedList;

public class Cache<T>{

    //Creates new linked list to act as cache
    private LinkedList<T> list;

    int maxSize;


    public Cache(int size){

        //Set max size of linked list
        maxSize = size;

        //Create linked list to be used as cache
        list = new LinkedList<T>();
        
    } 

    public T getObject(T object){
        if (list.indexOf(object) != -1) {
            return object;
        } else {
            return null;
        }
    }

    public void addObject(T object) {
        if (list.size() == maxSize) {
            list.removeLast();
        }
        list.addFirst(object);
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