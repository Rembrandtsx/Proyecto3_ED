package model.data_structures;

import java.util.Comparator;

public interface IHeap <T extends Comparable<T>> {

    /**
     * Adds a new element to the priority queue
     * @param e element
     */
    void add(T e);

    void add(T e, Comparator c);


    /**
     * Removes and returns the maximum element of the heap (the root)
     * @return maximum element
     */
    T removeMax();

    T removeMax(Comparator c);

    /**
     * Exchanges a given element (k) with the parent node (k/2) until the heap order is satisfied
     * @param index position of the element
     */
    void swim(int index);

    void swim(int index, Comparator c);


    /**
     * Exchanges a given element (k) with its children nodes (k*2 and k*2 + 1)
     * until the heap order is satisfied
     * @param index position of the element
     */
    void sink(int index, int limit);

    void sink(int index, int limit, Comparator c);


    /**
     * Returns the size of the priority queue
     * @return size
     */
    int size();

    /**
     * Checks if the array is empty
     * @return true if it´s empty, false otherwise
     */
    boolean isEmpty();

    /**
     * Sorts the current array representation in ascending or descending order.
     * @param order parameter that indicates if the user wants to
     *              sort in ascending or descending order
     */
    void heapSort(String order);

    void heapSort(String order, Comparator c);

    /**
     * Searches for the given position in the heap
     * @param index position of the element
     * @return the element
     */
    T get(int index);

    /**
     * Searches for the given position element the heap
     * @param e element
     * @return the element
     */
    T get(T e);

    /**
     * Returns the array that represents the Binary Heap
     * @return
     */
    Object[] getArray();
}
