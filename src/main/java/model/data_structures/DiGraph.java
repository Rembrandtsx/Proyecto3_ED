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

    //Depth-First search orders:
    private IQueue<K> preorderDfs;

    private IQueue<K> postorderDfs;

    private IStack<K> reversePostorderDfs;

    //Strongly connected components:
    private IHashMap<K, Integer> strongComponents;

    private int countStrongComponents;

    public DiGraph(){
        adjList = new SeparateChainingHashMap<>();
        strongComponents = new SeparateChainingHashMap<>();
        countStrongComponents = 0;
        initializeDfsOrders();
    }

    public DiGraph(IHashMap<K, Vertex<K, V, W>> hashMap){
        this.adjList = hashMap;
        strongComponents = new SeparateChainingHashMap<>();
        countStrongComponents = 0;
        initializeDfsOrders();
    }

    private void initializeDfsOrders(){
        preorderDfs = new List<>();
        postorderDfs = new List<>();
        reversePostorderDfs = new List<>();
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

    public IQueue<K> getPreorderDfs() {
        return preorderDfs;
    }

    public IQueue<K> getPostorderDfs() {
        return postorderDfs;
    }

    public IStack<K> getReversePostorderDfs() {
        return reversePostorderDfs;
    }

    public IHashMap<K, Integer> getStrongComponents() {
        return strongComponents;
    }

    public int getCountStrongComponents() {
        return countStrongComponents;
    }

    public K getFirstKey(){
        return adjList.keys().next();
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

    public int outdegreeOfVertex(K id){
        try {
            return adjList.get(id).getAdj().size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int indegreeOfVertex(K id){
        int indegree = 0;
        Iterator<K> iter = adjList.keys();
        while (iter.hasNext()){
            try {
                Vertex<K, V, W> v = adjList.get(iter.next());
                W edge = v.getEdge(id);
                if (edge != null){
                    indegree ++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return indegree;
    }

    /**
     * Calculates a given vertex "density" by getting the
     * percentage of the indegree and outdegree with respect to the total vertices
     * @param id
     * @return
     */
    public double getVertexDensity(K id){
        return (indegreeOfVertex(id) + outdegreeOfVertex(id)) / numVertices();
    }


    public void dfs(K id){
        initializeDfsOrders();
        dfsHelper(id);
    }

    private void dfsHelper(K id){
        IHashMap<K, Boolean> marked = new SeparateChainingHashMap<>();
        initializeMarking(marked);
        try {
            marked.put(id, true);
            preorderDfs.enqueue(id);
            LinkedList<K> adj = adjList.get(id).getAdj().toList();
            adj.listing();
            for (int i = 0; i < adj.size(); i++) {
                K key = adj.getCurrent();
                if(!marked.get(key)){
                    dfsHelper(key);
                }
                adj.next();
            }
            postorderDfs.enqueue(id);
            reversePostorderDfs.push(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DiGraph<K, V, W> reverseGraph(){
        DiGraph<K, V, W> reversedGraph = new DiGraph<>();
        Iterator<K> iterVertices = adjList.keys();

        try {
            while (iterVertices.hasNext()){
                Vertex<K, V, W> ini = adjList.get(iterVertices.next());
                reversedGraph.addVertex(ini.getId(), ini.getVal());
                Iterator<K> iterAdjacent = ini.getAdj().keys();
                while (iterAdjacent.hasNext()){
                    Vertex<K ,V, W> end = adjList.get(iterAdjacent.next());
                    W edge = ini.getEdge(end.getId());
                    reversedGraph.addVertex(end.getId(), end.getVal());
                    reversedGraph.addEdge(end.getId(), ini.getId(), edge);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return reversedGraph;
    }

    public void calculateStronglyConnectedComponents(){
        DiGraph<K, V, W> reversedGraph = reverseGraph();
        reversedGraph.dfs(reversedGraph.getFirstKey());
        IStack<K> reversePost = reversedGraph.getReversePostorderDfs();
        IHashMap<K, Boolean> marked = new SeparateChainingHashMap<>();
        initializeMarking(marked);
        countStrongComponents = 0;

        reversePost.listing();
        try {
            for (int i = 0; i < reversePost.size(); i++) {
                if(!marked.get(reversePost.getCurrent())){
                    dfsStrongComponents(reversePost.getCurrent(), marked);
                    countStrongComponents++;
                }
                reversePost.next();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void dfsStrongComponents(K id, IHashMap<K, Boolean> marked){
        try {
            marked.put(id, true);
            strongComponents.put(id, countStrongComponents);
            LinkedList<K> adj = adjList.get(id).getAdj().toList();
            adj.listing();
            for (int i = 0; i < adj.size(); i++) {
                K key = adj.getCurrent();
                if(!marked.get(key)){
                    dfsStrongComponents(key, marked);
                }
                adj.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeMarking(IHashMap<K, Boolean> marked){
        Iterator<K> iter = adjList.keys();
        while (iter.hasNext()){
            K key = iter.next();
            try {
                marked.put(key, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public int compareTo(DiGraph<K, V, W> o) {
        return 0;
    }
}
