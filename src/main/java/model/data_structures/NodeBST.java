package model.data_structures;

public class NodeBST <K extends Comparable<K>, V> implements Comparable<NodeBST>{

    public static final boolean RED = true;
    public static final boolean BLACK = false;

    private K key;
    private V value;
    private NodeBST leftNode;
    private NodeBST rightNode;
    boolean color;

    public NodeBST(K key, V value) {
        this.key = key;
        this.value = value;
        this.color = RED;
    }

    public NodeBST(K key, V value, NodeBST leftNode, NodeBST rightNode) {
        this.key = key;
        this.value = value;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
        this.color = RED;
    }


    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public NodeBST getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(NodeBST leftNode) {
        this.leftNode = leftNode;
    }

    public NodeBST getRightNode() {
        return rightNode;
    }

    public void setRightNode(NodeBST rightNode) {
        this.rightNode = rightNode;
    }

    public boolean getColor() {
        return color;
    }

    public void setColor(boolean color) {
        this.color = color;
    }

    public boolean isRed(){
        return this.color == RED;
    }

    @Override
    public boolean equals(Object o) {
        NodeBST e = (NodeBST)o;
        return this.key.equals(e.getKey());
    }

    @Override
    public int compareTo(NodeBST o) {
        return o.getKey().compareTo(key);
    }
}
