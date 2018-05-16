package model.data_structures;

import java.util.NoSuchElementException;

/**
 * Class that represents a minimal binary heap in which every element
 * in the queue is associated with an index.
 * Based on a Robert Sedgewick implementation: https://algs4.cs.princeton.edu/24pq
 */
public class IndexMinHeap<K extends Comparable<K>> {

    private int maxSize;

    /**
     * Number of elements in the heap
     */
    private int size;

    /**
     * Array of elements in the priority queue
     */
    private K[] elements;

    /**
     * Array in which we organize the indexes of the queue
     * in the elements array
     */
    private int[] pq;

    /**
     * Array in which we organize the indexes of the elements in pq array
     * "pq´s inverse: pq[qp[i]] = qp[pq[i]] = i" and elements[i] = priority of i
     */
    private int[] qp;

    public IndexMinHeap(int maxSize){
        this.maxSize = maxSize;
        size = 0;
        elements = (K[]) new Comparable[maxSize + 1];
        pq = new int[maxSize + 1];
        qp = new int[maxSize + 1];
        initializeQp();
    }

    private void initializeQp() {
        for (int i = 0; i < maxSize; i++) {
            qp[i] = -1;
        }
    }

    public int size(){
        return size;
    }

    /**
     * Exchanges a given element (k) with its children nodes (k*2 and k*2 + 1)
     * until the heap order is satisfied
     * @param index position of the element
     */
    public  void sink(int index){
        while (2*index <= size){
            int child = 2*index;
            if(child < size && greater(child, child+1)){
                child ++;
            }
            if (!greater(index, child)){
                break;
            }
            exchange(index, child);
            index = child;
        }
    }

    /**
     * Exchanges a given element (k) with the parent node (k/2) until the heap order is satisfied
     * @param index position of the element
     */
    public void swim(int index){
        while (index > 1 && greater(index/2, index)){
            exchange(index, index/2);
            index /= 2;
        }
    }

    /**
     * Changes the element associated to a given index
     * @param index index
     * @param element element to insert
     */
    public void changeKey(int index, K element){
        checkValidIndex(index);
        elements[index] = element;
        swim(qp[index]);
        sink(qp[index]);
    }

    /**
     * Inserts and associates an element with a given index
     * @param index index
     * @param element element to insert
     */
    public void insert(int index, K element){
        checkValidIndex(index);
        size ++;
        pq[size] = index;
        qp[index] = size;
        elements[index] = element;
        swim(size);
    }

    /**
     * Removes the minimum element and returns its key
     * @return the key of the minimum
     */
    public int removeMin(){
        if(size == 0){
            return -1;
        }
        int minIndex = pq[1];
        exchange(1, size);
        size--;
        sink(1);
        if(minIndex == pq[size + 1]){
            return -1;
        }
        qp[minIndex] = -1;
        elements[minIndex] = null;
        pq[minIndex + 1] = -1;
        return minIndex;
    }

    /**
     * Exchanges the element in the i-th position with the element in
     * the j-th position.
     * @param i first index
     * @param j second index
     */
    private void exchange(int i, int j){
        // change references in the array of indexes of the elements
        int temp = pq[i];
        pq[i] = pq[j];
        pq[j] = temp;

        // correct references in the array of indexes of the indexes of elements
        qp[pq[i]] = i;
        qp[pq[j]] = j;
    }

    /**
     * Compares the element in the i-th position with the element in
     * the j-th position.
     * @param i first index
     * @param j second index
     * @return true if the first element is greater than the other, false otherwise
     */
    private boolean greater(int i, int j){
        return elements[pq[i]].compareTo(elements[qp[i]]) > 0;
    }

    public boolean contains(int index){
        return qp[index] != -1;
    }

    private void checkValidIndex(int index){
        if(index < 0 || index >= maxSize){
            throw new IllegalArgumentException();
        }
        if(!contains(index)){
            throw new NoSuchElementException();
        }
    }

    public boolean isEmpty(){
        return size == 0;
    }
}
