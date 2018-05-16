package model.data_structures;

/**
 * Implementation of a vertex of a graph
 * @param <K> key of the vertex
 * @param <V> value of the vertex
 * @param <W> weight of an edge
 */
public class Vertex <K extends Comparable<K>, V, W>{

    /**
     * The set of all adjacent vertices id (K) with their edges (W)
     */
    private IHashMap<K, W> adj;

    /**
     * Identifier of the vertex
     */
    private K id;

    /**
     * Value of the vertex
     */
    private V val;

    public Vertex(K id, V val){
        this.id = id;
        this.val = val;
        adj = new SeparateChainingHashMap<>();
    }

    public Vertex(){
        adj = new SeparateChainingHashMap<>();
    }

    public K getId() {
        return id;
    }

    public void setId(K id) {
        this.id = id;
    }

    public IHashMap<K, W> getAdj() {
        return adj;
    }

    public V getVal() {
        return val;
    }

    public void setVal(V val) {
        this.val = val;
    }

    public void addEdge(K id, W weight) throws Exception {
        adj.put(id, weight);
    }

    public W getEdge(K id){
        try {
            return adj.get(id);
        } catch (Exception e) {
            return null;
        }
    }

    public void setEdge(K id, W weight) throws Exception {
        adj.put(id, weight);
    }

    public void setAdj(IHashMap<K, W> adj){
        this.adj = adj;
    }
    
    public boolean contains(K id){
    	return adj.containsKey(id);
    }
}
