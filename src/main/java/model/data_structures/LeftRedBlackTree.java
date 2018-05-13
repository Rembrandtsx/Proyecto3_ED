package model.data_structures;

import java.util.Iterator;

/**
 * Implementation based on Sedgewick´s notes about 2-3-5 trees: https://www.cs.princeton.edu/~rs/talks/LLRB/RedBlack.pdf
 * @param <K> Generic type of the key
 * @param <V> Generic type of the value
 */
public class LeftRedBlackTree <K extends Comparable<K>, V extends Comparable<V>> implements IRedBlackTrees<K, V> {

    /**
     * Root of the tree
     */
    private NodeBST root;

    /**
     * Number of nodes in the tree
     */
    private int size;

    /**
     * Total height of the tree from the root
     */
    public int height;

    /**
     * A list of all the node keys
     */
    private LinkedList<K> nodeKeys;

    /**
     * Constructs a tree by the given key and value.
     * @param key
     * @param val
     */
    public LeftRedBlackTree(K key, V val) {
        put(key, val);
        size = 1;
        nodeKeys = new List<>();
        nodeKeys.add(key);
    }

    /**
     * Constructs an empty tree.
     */
    public LeftRedBlackTree() {
        nodeKeys = new List<>();
        size = 0;
        this.root = null;
    }

    /**
     * Returns the root.
     * @return
     */
    public NodeBST getRoot() {
        return root;
    }

    /**
     * Returns the size of the tree
     *
     * @return
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Checks if the tree is empty
     * @return true if its empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Searches for the given key. Returns null if it does not exists.
     * @param key
     * @return value
     */
    @Override
    public V get(K key){
        NodeBST<K,V> n = root;
        while (n != null){
            int comparison = n.getKey().compareTo(key);
            if (comparison < 0){
                n = n.getRightNode();
            }else if (comparison > 0){
                n = n.getLeftNode();
            }else {
                return n.getValue();
            }
        }
        return null;
    }

    /**
     * Calculates the height of the tree from the root to the
     * specified key
     *
     * @param key
     * @return height
     */
    @Override
    public int getHeight(K key) {

        int count = 0;
        NodeBST<K,V> n = root;
        while (n != null){
            int comparison = n.getKey().compareTo(key);
            if (comparison < 0){
                count ++;
                n = n.getRightNode();
            }else if (comparison > 0){
                count ++;
                n = n.getLeftNode();
            }else {
                return count;
            }
        }
        return -1;
    }

    /**
     * Checks if the specified key is in the tree
     *
     * @param key
     * @return true if the key exists, false otherwise
     */
    @Override
    public boolean contains(K key) {
        return get(key) != null;
    }

    /**
     * Adds a new element (key, value pair) to the tree.
     * @param key
     * @param val
     */
    @Override
    public void put(K key, V val){
        root = put(root, key, val);

        root.setColor(NodeBST.BLACK);
    }

    /**
     * Helper method for the add functionality. Adds a key-value pair
     * maintaining a well-formed red-black tree
     * @param n node
     * @param key
     * @param val
     * @return node
     */
    public NodeBST put(NodeBST n, K key, V val){

        if(n == null){
            size++;
            nodeKeys.add(key);
            return new NodeBST(key, val);
        }

        int comparison = n.getKey().compareTo(key);
        if (comparison < 0){
            n.setRightNode(put(n.getRightNode(), key, val));
        }else if (comparison > 0){
            n.setLeftNode(put(n.getLeftNode(), key, val));
        }else {
            n.setValue(val);
        }

        // fix nodes to balance tree


        // case: Right child is red
        if( isRed(n.getRightNode())){
            n = rotateLeft(n);
        }
        // case: Two consecutive  left children are red
        if( isRed(n.getLeftNode()) && isRed(n.getLeftNode().getLeftNode())){
            n = rotateRight(n);
        }
        // case: Both children are red
        if (isRed(n.getLeftNode()) && isRed(n.getRightNode())){
            alternateColors(n);
        }

        return n;

    }

    /**
     * Retruns the height of the tree
     *
     * @return
     */
    @Override
    public int height() {
        height = height(root);
        return height;
    }

    /**
     * Returns the maximum depth from given node
     * @param n node
     * @return relative height
     */
    public int height(NodeBST n){
        if(n == null){
            return -1;
        }else{
            int leftHeight = height(n.getLeftNode());
            int rightHeight = height(n.getRightNode());

            if( leftHeight > rightHeight){
                return leftHeight + 1;
            }else {
                return rightHeight + 1;
            }
        }
    }


    /**
     * Returns the smallest key in the tree
     * @return smallest key
     */
    @Override
    public K min() {
        NodeBST<K, V> n = root;
        while (n != null){
            n = n.getLeftNode();
            if(n.getLeftNode() == null){
                return n.getKey();
            }
        }
        return null;
    }

    /**
     * Returns the greatest key in the tree
     * @return greatest key
     */
    @Override
    public K max() {
        NodeBST<K, V> n = root;
        while (n != null){
            n = n.getRightNode();
            if(n.getRightNode() == null){
                return n.getKey();
            }
        }
        return null;
    }

    /**
     * Checks if the tree is balanced to the left
     * @return true if its balanced, false otherwise
     */
    @Override
    public boolean check() {
        return check(root);
    }

    /**
     * Checks if a subtree given by the node is balanced to the left
     * @param n node
     * @return true if its balanced, false otherwise
     */
    public boolean check(NodeBST n){
        if(n == null){
            return true;
        }
        NodeBST left = n.getLeftNode();
        NodeBST right = n.getRightNode();
        if (isRed(right) || (isRed(right) && isRed(left))){
            return false;
        }
        return check(left) && check(right);
    }


    /**
     * Rotates a whole node to the left
     * @param n node
     * @return node
     */
    @Override
    public NodeBST rotateLeft(NodeBST n) {

        NodeBST temp = n.getRightNode();
        n.setRightNode(temp.getLeftNode());
        temp.setLeftNode(n);
        temp.setColor(temp.getLeftNode().getColor());
        temp.getLeftNode().setColor(NodeBST.RED);
        return temp;
    }

    /**
     * Rotates a whole node to the right
     * @param n node
     * @return node
     */
    @Override
    public NodeBST rotateRight(NodeBST n) {

        NodeBST temp = n.getLeftNode();
        n.setLeftNode(temp.getRightNode());
        temp.setRightNode(n);
        temp.setColor(temp.getRightNode().getColor());
        temp.getRightNode().setColor(NodeBST.RED);
        return temp;
    }

    /**
     * Flips the color of the whole node
     * @param n node
     */
    public void alternateColors(NodeBST n){
        n.setColor(!n.getColor());
        n.getLeftNode().setColor(!n.getLeftNode().getColor());
        n.getRightNode().setColor(!n.getRightNode().getColor());
    }

    /**
     * Checks if a node is red. If the node is null, returns false
     * @param n node
     * @return true if the node is red, false otherwise
     */
    public boolean isRed(NodeBST n ){
        return n != null && n.getColor() == NodeBST.RED;
    }

    /**
     * @return an iterator for all the keys
     */
    @Override
    public Iterator keys(){
        return new ListIterator(nodeKeys);
    }

    /**
     * Returns an Iterator of all the values within the specified range
     * @param init lower bound
     * @param end  higher bound
     * @return Iterator
     */
    @Override
    public Iterator valuesInRange(K init, K end) {
        LeftRedBlackTree<K, V> treeNodesInRange = new LeftRedBlackTree<>();

        if(init.compareTo(end) > 0){
            K temp = init;
            init = end;
            end = temp;
        }

        nodesInRange(init, end, root, treeNodesInRange);
        LinkedList<K> keys = treeNodesInRange.getNodeKeys();
        LinkedList<V> values = new List<>();

        keys.listing();
        for (int i = 0; i < keys.size(); i++) {
            K key = keys.getCurrent();
            values.add(treeNodesInRange.get(key));
            keys.next();
        }

        return new ListIterator(values);
    }

    /**
     * Returns a new tree with the elements which are within the given range
     * @param init lower bound
     * @param end higher bound
     * @param n node
     * @return tree
     */
    public static <K extends Comparable<K>,V>void nodesInRange(K init, K end, NodeBST<K, V> n, IRedBlackTrees tree){
        if(n == null){
            return;
        }
        K key = n.getKey();
        NodeBST<K, V> right = n.getRightNode();
        NodeBST<K, V> left = n.getLeftNode();

        // if the key is out of bounds
        if(key.compareTo(end) > 0){
            nodesInRange(init, end, left, tree);
        }
        // if the key is below range
        else if(key.compareTo(init) < 0){
            nodesInRange(init, end, right, tree);
        }
        // if its within range
        else {
            // new tree
            tree.put(n.getKey(), n.getValue());
            nodesInRange(init, end, left, tree);
            nodesInRange(init, end, right, tree);
        }
    }

    /**
     * Returns an Iterator of all the values within the specified range
     * @param init lower bound
     * @param end  higher bound
     * @return Iterator
     */
    @Override
    public Iterator keysInRange(K init, K end) {

        if(init.compareTo(end) > 0){
            K temp = init;
            init = end;
            end = temp;
        }

        LeftRedBlackTree<K, V> treeNodesInRange = new LeftRedBlackTree<>();
        nodesInRange(init, end, root, treeNodesInRange);
        return new ListIterator(treeNodesInRange.getNodeKeys());
    }

    /**
     * Returns all of the keys in the tree
     * @return nodeKeys
     */
    private LinkedList<K> getNodeKeys(){
        return nodeKeys;
    }

    /**
     * Calculates the theoretical height of the red black tree based on its
     * current size
     * @return height
     */
    public int getHighestTheoricReBlackTreeHeight(){
        return 2 * (int)Math.floor(log(size, 2));
    }

    /**
     * Calculates the theoretical height of the 2-3 tree represented by the
     * red black tree based on its current size
     * @return height
     */
    public int getHighestTheoric2_3TreeHeight(){
        return (int)Math.floor(log(size, 2));
    }

    /**
     * Calculates the theoretical height of the 2-3 tree represented by the
     * red black tree based on its current size
     * @return height
     */
    public int getLowestTheoric2_3TreeHeight(){
        return (int)Math.floor(log(size, 3));
    }

    /**
     * Calculates the mean search height for the tree, doing the sum of the
     * heights and dividing it by the total number of nodes
     * @return mean search height
     */
    public double getMeanSearchHeight(){
        int sumOfHeights = sumHeights(root, 0);
        return (sumOfHeights + 0.0) / size;
    }

    private int sumHeights(NodeBST n, int currentHeight) {
        int total = currentHeight;
        if (n == null){
            return 0;
        }
        total += sumHeights(n.getRightNode(), currentHeight + 1);
        total += sumHeights(n.getLeftNode(), currentHeight + 1);
        return total;
    }

    public double log(double n, double base){
        return Math.log(n) / Math.log(base);
    }
}
