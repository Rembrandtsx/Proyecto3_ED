package model.data_structures;

import java.util.Comparator;

public class MinHeap <T extends  Comparable<T>> implements IHeap<T>{

    /**
     * Default size of the array if the user doesn´t specifies it
     */
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * Constant to determine if the heapSort() is to be executed in ascending order
     */
    public static final String ASCENDING = "ASCENDING";

    /**
     * Constant to determine if the heapSort() is to be executed in descending order
     */
    public static final String DESCENDING = "DESCENDING";

    /**
     * Array that represents the Binary MaxHeap
     */
    private T[] arr;

    /**
     * Maximum size of the array
     */
    private int maxSize;

    /**
     * Current size of the array
     */
    private int size;

    /**
     * Constructs a new priority queue with the default size
     */
    public MinHeap(){
        this(DEFAULT_CAPACITY);
    }

    /**
     * Constructs a new priority queue with the given size
     */
    public MinHeap(int capacity){
        arr = (T[])new Comparable[capacity ];
        maxSize = capacity;
        size = 0;
    }

    /**
     * Adds a new element to the priority queue
     * @param e element
     */
    @Override
    public void add(T e) {
        size++;
        if (size >= maxSize) {
            resize();
        }
        arr[size] = e;
        swim(size);
    }

    /**
     * Adds a new element to the priority queue
     * @param e element
     * @param c comparator
     */
    @Override
    public void add(T e, Comparator c) {
        size++;
        if (size >= maxSize) {
            resize();
        }
        arr[size] = e;
        swim(size, c);
    }

    /**
     * Performs a resize on the array if the capacity is exceeded
     */
    private void resize() {
        maxSize *= 2;
        T[] copy = (T[])new Comparable[size];
        for (int i = 0; i < size; i++) {
            copy[i] = arr[i];
        }

        arr = (T[])new Comparable[maxSize];
        for (int i = 0; i < copy.length; i++) {
            arr[i] = copy[i];
        }
    }

    /**
     * Removes and returns the maximum element of the heap (the root)
     * @return maximum element
     */
    public T removeMin() {
        T element = arr[1];

        arr[1] = arr[size];
        arr[size] = null;
        size --;
        sink(1, size);

        return element;
    }

    /**
     * Removes and returns the maximum element of the heap (the root)
     * @return maximum element
     * @param c comparator
     */
    public T removeMin( Comparator c) {
        T element = arr[1];

        arr[1] = arr[size];
        arr[size] = null;
        size --;
        sink(1, size, c);

        return element;
    }

    /**
     * Exchanges a given element (k) with the parent node (k/2) until the heap order is satisfied
     * @param index position of the element
     */
    @Override
    public void swim(int index) {
        while (index > 1 && getParent(index).compareTo(arr[index]) > 0){
            exchange(index, index/2);
            index /= 2;
        }
    }

    /**
     * Exchanges a given element (k) with the parent node (k/2) until the heap order is satisfied
     * @param index position of the element
     * @param c comparator
     */
    @Override
    public void swim(int index, Comparator c) {
        while (index > 1 && c.compare(getParent(index), arr[index]) > 0){
            exchange(index, index/2);
            index /= 2;
        }
    }

    /**
     * Exchanges a given element (k) with its children nodes (k*2 and k*2 + 1)
     * until the heap order is satisfied
     * @param index position of the element
     */
    @Override
    public void sink(int index, int limit) {

        while (index * 2 <= limit){
            int childIndex = index * 2;
            if (childIndex < limit &&
                    arr[childIndex].compareTo(arr[childIndex + 1]) > 0){
                childIndex++;
            }
            if (arr[index].compareTo(arr[childIndex]) < 0){
                break;
            }
            exchange(index, childIndex);
            index = childIndex;
        }
    }

    /**
     * Exchanges a given element (k) with its children nodes (k*2 and k*2 + 1)
     * until the heap order is satisfied
     * @param index position of the element
     */
    @Override
    public void sink(int index, int limit, Comparator c) {

        while (index * 2 <= limit){
            int childIndex = index * 2;
            if (childIndex < limit &&
                    c.compare(arr[childIndex], arr[childIndex + 1]) > 0){
                childIndex++;
            }
            if (c.compare(arr[index], arr[childIndex]) < 0){
                break;
            }
            exchange(index, childIndex);
            index = childIndex;
        }
    }

    /**
     * Returns the size of the priority queue
     * @return size
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Checks if the array is empty
     * @return true if it´s empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Sorts the current array representation in ascending or descending order.
     * @param order parameter that indicates if the user wants to
     *              sort in ascending or descending order
     */
    @Override
    public void heapSort(String order) {
        int n = size;

        while (n > 1){
            exchange(1, n--);
            sink(1, n);
        }
        if(order.equals(DESCENDING)){
            reverse();
        }
    }

    /**
     * Sorts the current array representation in ascending or descending order.
     * @param order parameter that indicates if the user wants to
     *              sort in ascending or descending order
     * @param c comparator
     */
    @Override
    public void heapSort(String order, Comparator c) {
        int n = size;

        while (n > 1){
            exchange(1, n--);
            sink(1, n, c);
        }
        if(order.equals(DESCENDING)){
            reverse();
        }
    }

    /**
     * Searches for the given position in the heap
     * @param index position of the element
     * @return the element
     */
    @Override
    public T get(int index){
        if (index > size) {
            throw new IndexOutOfBoundsException();
        }
        return arr[index];
    }

    /**
     * Searches for the given position element the heap
     * @param e element
     * @return the element
     */
    @Override
    public T get(T e){
        int n = size;
        for (int i = n/2; i >= 1; i--){
            int parent = i;
            int child1 = parent*2;
            int child2 = child1 + 1;
            if(arr[parent].equals(e)){
                return arr[parent];
            }else if( arr[child1] != null && arr[child1].equals(e)){
                return arr[child1];
            }else if(arr[child2] != null && arr[child2].equals(e)){
                return arr[child2];
            }
        }
        return null;
    }

    /**
     * Returns the array that represents the Binary MaxHeap
     * @return
     */
    @Override
    public Object[] getArray(){
        Object[] newArr = new Object[size];
        for (int i = 0; i < size; i++) {
            newArr[i] = arr[i + 1];
        }
        return newArr;
    }

    private T getParent(int index){
        return arr[index/2];
    }

    private void reverse(){
        for (int i = 1; i <= size/2; i++){
            exchange(i, size + 1 - i);
        }
    }

    public void exchange(int i, int j){
        T temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }


}
