package model.data_structures;

import java.util.Iterator;

public class ListIterator<T extends Comparable<T>> implements Iterable<T>, Iterator<T>{

    private LinkedList<T> list;

    public ListIterator(LinkedList<T> list) {
        this.list = list;
    }

    @Override
    public boolean hasNext() {
        return list.hasNext();
    }

    @Override
    public T next() {
        if( hasNext()){
            T curr = list.getCurrent();
            list.next();
            return curr;
        }
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return this;
    }
}
