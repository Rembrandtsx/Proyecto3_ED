package model.vo;

import model.data_structures.DiGraph;
import model.data_structures.MinHeap;

public class ShortestPathServiceGraph {


    private MinHeap<Double> minHeap;

    public ShortestPathServiceGraph(DiGraph<String, AdjacentServices, ArcServices> graph, String ini){

        minHeap = new MinHeap<>();
    }
}
