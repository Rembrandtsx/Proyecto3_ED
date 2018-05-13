package model.data_structures;

import java.util.Iterator;

public interface IRedBlackTrees <K extends Comparable<K>, V> {

    /**
     * Returns the size of the tree
     * @return
     */
    int size();

    /**
     * Checks if the tree is empty
     * @return true if its empty, false otherwise
     */
    boolean isEmpty();

    /**
     * Returns the root.
     * @return
     */
    V get(K key);

    /**
     * Calculates the height of the tree from the root to the
     * specified key
     * @param key
     * @return height
     */
    int getHeight(K key);

    /**
     * Checks if the specified key is in the tree
     * @param key
     * @return true if the key exists, false otherwise
     */
    boolean contains(K key);

    /**
     * Adds a new element (key, value pair) to the tree.
     * @param key
     * @param val
     */
    void put(K key, V val);

    /**
     * Retruns the height of the tree
     * @return
     */
    int height();

    /**
     * Returns the smallest key in the tree
     * @return smallest key
     */
    K min();

    /**
     * Returns the greatest key in the tree
     * @return greatest key
     */
    K max();

    /**
     * Checks if the tree is balanced to the left
     * @return true if its balanced, false otherwise
     */
    boolean check();

    /**
     * @return an Iterator for all the keys
     */
    Iterator keys();

    /**
     * Returns an Iterator of all the values within the specified range
     * @param init lower bound
     * @param end higher bound
     * @return Iterator
     */
    Iterator valuesInRange(K init, K end);

    /**
     * Returns an Iterator of all the values within the specified range
     * @param init lower bound
     * @param end higher bound
     * @return Iterator
     */
    Iterator keysInRange(K init, K end);

    /**
     * Rotates a whole node to the left
     * @param n node
     * @return node
     */
    NodeBST rotateLeft(NodeBST n);

    /**
     * Rotates a whole node to the right
     * @param n node
     * @return node
     */
    NodeBST rotateRight(NodeBST n);


    int getHighestTheoricReBlackTreeHeight();

    int getHighestTheoric2_3TreeHeight();

    int getLowestTheoric2_3TreeHeight();

    double getMeanSearchHeight();
}
