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

    public ShortestPathServiceGraph(DiGraph<String, AdjacentServices, ArcServices> graph, String ini, String weightCriteria){
        initializeMappings(graph);
        path = new int[graph.numVertices()];
        distances = new double[graph.numVertices()];
        minHeap = new IndexMinHeap<>(graph.numVertices());
        this.weightCriteria = weightCriteria;

        // initialize distances:
        for (int i = 0; i < distances.length; i++) {
            distances[i] = Double.POSITIVE_INFINITY;
        }

        // initialize starting vertex and iterate over priority queue
        try {
            int start = mapToInteger.get(ini);
            minHeap.insert(start, 0.0);
            while (!minHeap.isEmpty()){
                evaluateEdges(graph, minHeap.removeMin(), weightCriteria);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void evaluateEdges(DiGraph<String,AdjacentServices,ArcServices> graph, int v, String weightCriteria) throws Exception {
        String graphKey = mapToString[v];
        Vertex<String,AdjacentServices,ArcServices> vertex = graph.getvertex(graphKey);
        ListIterator<String> adj = new ListIterator<>(vertex.getAdj().toList());

        for (String k: adj) {
            int w = mapToInteger.get(k);
            double weight = getWeight(vertex, k, weightCriteria);
            if(distances[w] > (distances[v] + weight)){
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

    private double getWeight(Vertex<String,AdjacentServices,ArcServices> ini, String end, String weightCriteria) {
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
        for(String k: keys){
            mapToString[count] = k;
        }
    }
}
