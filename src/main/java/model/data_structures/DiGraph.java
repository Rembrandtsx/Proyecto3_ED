package model.data_structures;

import java.util.Iterator;

/**
 * Implementation of a directed graph
 * @param <K> key of a vertex
 * @param <V> value of a vertex
 * @param <W> weight of an edge
 */
public class DiGraph <K extends Comparable<K>, V, W> implements IDiGraph<K, V, W>, Comparable<DiGraph<K, V, W>>{

    /**
     * A hash map with all the vertices in the graph
     */
    private IHashMap<K, Vertex<K, V, W>> adjList;

    public DiGraph(){
        adjList = new SeparateChainingHashMap<>();
    }

    private DiGraph(IHashMap<K, Vertex<K, V, W>> hashMap){
        this.adjList = hashMap;
    }


    /**
     * Returns the number of vertices
     * @return number vertices
     */
    @Override
    public int numVertices() {
        return adjList.size();
    }

    /**
     * Returns the number of edges
     * @return number edges
     */
    @Override
    public int numEdges() {
        int count = 0;
        Iterator<K> ids = adjList.keys();
        while (ids.hasNext()){
            try {
                count += adjList.get(ids.next()).getAdj().size();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return count;
    }
    
    public IHashMap getAdjList() {
    	return adjList;
    }

    /**
     * Adds a new vertex/node to the graph
     * @param id  key of the node
     * @param val value of the node
     */
    @Override
    public void addVertex(K id, V val) {
        Vertex v = new Vertex(id, val);
        try {
            adjList.put(id, v);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a new edge between two vertices in the graph
     * @param id1  key of the first node
     * @param id2  key of the second node
     * @param e edge
     */
    @Override
    public void addEdge(K id1, K id2, W e) {
        try {
            Vertex v1 = adjList.get(id1);
            v1.addEdge(id2, e);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Returns the value of a given vertex/node
     * @param id key of the node
     * @return the value of the node
     */
    @Override
    public V getInfoVertex(K id) {
        try {
            return adjList.get(id).getVal();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Vertex getvertex(K id) {
    	try {
            return adjList.get(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sets the value of a given vertex/node
     * @param id  key of the node
     * @param val value
     */
    @Override
    public void setInfoVertex(K id, V val) {
        try {
            adjList.get(id).setVal(val);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the information of a given edge
     * @param id1 key of the first adjacent vertex
     * @param id2 key of the second adjacent vertex
     * @return edge info
     */
    @Override
    public W getInfoEdge(K id1, K id2) {
        W edge = null;
        try {
            edge =  adjList.get(id1).getEdge(id2);
        } catch (Exception e) {
            return null;
        }
        return edge;
    }

    /**
     * Sets the information of an edge given the two adjacent vertices
     * @param id1 key of the first adjacent vertex
     * @param id2 key of the second adjacent vertex
     * @param e   edge info
     */
    @Override
    public void setInfoEdge(K id1, K id2, W e) {
        try {
            adjList.get(id1).setEdge(id2, e);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Returns a collection with all the identifiers of the adjacent vertices
     * for a given vertex/node
     * @param id key of the node
     * @return collection of keys
     */
    @Override
    public Iterable<K> adj(K id) {
        Iterator<K> ids = null;
        LinkedList<K> listIterable = new List<>();
        try {
            ids = adjList.get(id).getAdj().keys();
        } catch (Exception e) {
            e.printStackTrace();
        }
        while (ids.hasNext()){
            listIterable.add(ids.next());
        }
        return new ListIterator<>(listIterable);
    }
    
    public boolean contains(K id){
    	return adjList.containsKey(id);
    }
    
    public Iterator<K> keys(){
    	return adjList.keys();
 
    }

    public ArrayList<DiGraph<K, V, W>> partitionGraph(){
        ArrayList<DiGraph<K, V, W>> graphList = new ArrayList<>();
        int partitionSize = adjList.size() / 6;
        int counter = 0;
        IHashMap<K, Vertex<K, V, W>> hashMap = new SeparateChainingHashMap<>();
        Iterator<K> keys = adjList.keys();

        for (int i = 0; i < graphList.size(); i++) {
            graphList.add(new DiGraph<K, V, W>(new SeparateChainingHashMap<>()));
        }

        while (keys.hasNext()){
            K key = keys.next();
            graphList.get(counter/partitionSize).addVertex(key, this.getInfoVertex(key));
        }
        return graphList;
    }


    @Override
    public int compareTo(DiGraph<K, V, W> o) {
        return 0;
    }
}
