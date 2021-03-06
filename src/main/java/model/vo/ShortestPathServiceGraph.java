package model.vo;

import model.data_structures.*;

/**
 * Class in charged of executing Dijkstra´s shortest path algorithm.
 * Based on a Robert Sedgewick implementation:
 * Sedgewick, R., & Wayne, K. (2011). Algorithms Fourth Edition (Addison-Wesley). Boston: Addison-Wesley.
 */
public class ShortestPathServiceGraph {

    /**
     * Indicates if the weight value to be used is time
     */
    public static final String  TIME = "TIME";

    /**
     * Indicate if the weight value to be used is distance
     */
    public static final String  DISTANCE = "DISTANCE";

    /**
     * Mapping from string keys to integer keys
     */
    private IHashMap<String, Integer> mapToInteger;

    /**
     * Mapping from integer keys to string keys
     */
    private String[] mapToString;

    /**
     * Indicates which weight value is going to be used
     */
    private String weightCriteria;

    private int[] path;

    private double[] distances;

    private IndexMinHeap<Double> minHeap;

    private int iniVertex;

    private DiGraph<String, AdjacentServices, ArcServices> graph;

    private boolean[] marked;

    public ShortestPathServiceGraph(DiGraph<String, AdjacentServices, ArcServices> graph, String ini, String weightCriteria){
        initializeMappings(graph);
        path = new int[graph.numVertices()];
        distances = new double[graph.numVertices()];
        marked = new boolean[graph.numVertices()];
        minHeap = new IndexMinHeap<>(graph.numVertices());
        this.weightCriteria = weightCriteria;
        this.graph = graph;

        // initialize path
        for (int i = 0; i < path.length; i++) {
            path[i] = -1;
        }

        // initialize distances:
        for (int i = 0; i < distances.length; i++) {
            distances[i] = Double.POSITIVE_INFINITY;
        }
        try {
            distances[mapToInteger.get(ini)] = 0.0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        // initialize starting vertex and iterate over priority queue
        try {
            iniVertex = mapToInteger.get(ini);
            minHeap.insert(iniVertex, 0.0);
            while (!minHeap.isEmpty()){
                evaluateEdges(minHeap.removeMin());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Dijkstra Terminated");

    }

    private void evaluateEdges(int v) throws Exception {
        marked[v] = true;
        String graphKey = mapToString[v];
        Vertex<String,AdjacentServices,ArcServices> vertex = graph.getVertex(graphKey);
        ListIterator<String> adj = new ListIterator<>(vertex.getAdj().toList());

        for (String k: adj) {
            int w = mapToInteger.get(k);
            double weight = getWeight(vertex, k);
            if(!marked[w] && (distances[w] > (distances[v] + weight))){

                distances[w] = distances[v] + weight;
                path[w] = v;
                if(minHeap.contains(w)){
                    minHeap.changeKey(w, distances[w]);
                }else {
                    minHeap.insert(w, distances[w]);
                }
            }
        }
    }

    private double getWeight(Vertex<String,AdjacentServices,ArcServices> ini, String end) {
        double weight = 0.0;
        ArcServices edge = ini.getEdge(end);

        if (weightCriteria.equals(TIME)){
            weight = edge.getMeanTimeStamp();
        }else if(weightCriteria.equals(DISTANCE)){
            weight = edge.getMeanDistance();
        }

        return weight;
    }


    private void initializeMappings(DiGraph<String, AdjacentServices, ArcServices> graph){
        mapToInteger = new SeparateChainingHashMap<>();
        mapToString = new String[graph.numVertices()];
        ListIterator<String> keys = new ListIterator<>(graph.getAdjList().toList());

        // fill hash map
        int count = 0;
        for (String k: keys) {
            try {
                mapToInteger.put(k, count);
            } catch (Exception e) {
                e.printStackTrace();
            }
            count ++;
        }

        // fill array
        count = 0;
        keys = new ListIterator<>(graph.getAdjList().toList());
        for(String k: keys){
            mapToString[count] = k;
            count++;
        }
    }

    public int[] getPath() {
        return path;
    }

    public double[] getDistances() {
        return distances;
    }

    public LinkedList<ArcServices> reconstructPath(String endVertex){
        LinkedList<ArcServices> pathTo = new List<>();
        try {
            reconstructPath(mapToInteger.get(endVertex), iniVertex, pathTo);
        }catch (Exception e){
            e.printStackTrace();
        }

//        LinkedList<ArcServices> result = new List<>();
//        for (int i = 0; i < pathTo.size(); i++) {
//            result.add(pathTo.pop());
//        }
        return pathTo;
    }

    public void reconstructPath(int endVertex, int iniVertex, LinkedList<ArcServices> pathTo) throws Exception {


        int currentVertex = endVertex;


        if(path[currentVertex] == -1){
            return;
        }

        while (currentVertex != iniVertex){

            String current = mapToString[currentVertex];
            String prev = mapToString[path[currentVertex]];
            ArcServices edge = graph.getInfoEdge(prev, current);
            pathTo.add(edge);
            currentVertex = mapToInteger.get(prev);
        }

    }



}
