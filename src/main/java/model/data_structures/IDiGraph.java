package model.data_structures;

import java.io.Serializable;
import java.util.Iterator;

/**
 * Interface for directed graph implementation
 * @param <K> key of a vertex
 * @param <V> value of a vertex
 * @param <W> weight of an edge
 */
public interface IDiGraph <K extends Comparable<K>, V, W> extends Serializable{

    /**
     * Returns the number of vertices
     * @return number vertices
     */
    int numVertices();
    public IHashMap getAdjList();

    /**
     * Returns the number of edges
     * @return number edges
     */
    int numEdges();

    /**
     * Adds a new vertex/node to the graph
     * @param id key of the node
     * @param val value of the node
     */
    void addVertex(K id, V val);

    /**
     * Adds a new edge between two vertices in the graph
     * @param id1 key of the first node
     * @param id2 key of the second node
     * @param e edge
     */
    void addEdge(K id1, K id2, W e);

    /**
     * Returns the value of a given vertex/node
     * @param id key of the node
     * @return the value of the node
     */
    V getInfoVertex(K id);

    /**
     * Sets the value of a given vertex/node
     * @param id key of the node
     * @param val value
     */
    void setInfoVertex(K id, V val);

    /**
     * Returns the information of a given edge
     * @param id1 key of the first adjacent vertex
     * @param id2 key of the second adjacent vertex
     * @return edge info
     */
    W getInfoEdge(K id1, K id2);

    /**
     * Sets the information of an edge given the two adjacent vertices
     * @param id1 key of the first adjacent vertex
     * @param id2 key of the second adjacent vertex
     * @param e edge info
     */
    void setInfoEdge(K id1, K id2, W e);

    /**
     * Returns a collection with all the identifiers of the adjacent vertices
     * for a given vertex/node
     * @param id key of the node
     * @return collection of keys
     */
    Iterable <K> adj(K id);
    
    boolean contains(K id);
    
    Iterator<K> keys();

}
