package model.vo;

import model.data_structures.*;

import java.util.Comparator;
import java.util.Iterator;

/**
 * Class in charged of executing bfs, specialized in finding paths with no tolls
 * Based on a Robert Sedgewick implementation:
 * Sedgewick, R., & Wayne, K. (2011). Algorithms Fourth Edition (Addison-Wesley). Boston: Addison-Wesley.
 */
public class BreadthSearchServiceGraph {

    /**
     * Indicates if the weight value to be used is distance
     */
    public static final String DISTANCE = "DISTANCE";

    /**
     * Indicates if the weight value to be used is time
     */
    public static final String TIME = "TIME";

    /**
     * Paths with no tolls between two vertices
     */
    private IHeap<Path> pathsToWithNoTolls;

    /**
     * Paths with no tolls from a vertex
     */
    private IHashMap<String, LinkedList<ArcServices>> pathsWithNoTolls;

    private IHashMap<String, Boolean> marked;

    private DiGraph<String, AdjacentServices, ArcServices> graph;

    private String iniVertex;

    public BreadthSearchServiceGraph(DiGraph<String, AdjacentServices, ArcServices> graph, String iniVertex){
        this.graph = graph;
        this.iniVertex = iniVertex;
        pathsToWithNoTolls = new MaxHeap<>();
        pathsWithNoTolls = new SeparateChainingHashMap<>();
        initializeMarking();
        try {
            bfs(iniVertex);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bfs(String s) throws Exception{
        IQueue<String> queue = new List<>();
        marked.put(s, true);
        queue.enqueue(s);

        while (!queue.isEmpty()){
            String v = queue.dequeue();
            Vertex<String, AdjacentServices, ArcServices> vertex = graph.getVertex(v);
            ListIterator<String> adj = new ListIterator<>(vertex.getAdj().toList());
            for(String w : adj){
                ArcServices edge = vertex.getEdge(w);
                int toll = (int)edge.getNumberOfTolls();
                if (!marked.get(w) && toll == 0){
                    LinkedList<ArcServices> p;
                    try {
                        p = pathsWithNoTolls.get(w);
                        p.add(edge);

                    }catch (Exception e){
                        p = new List<>();
                        pathsWithNoTolls.put(w, p);
                    }

                    marked.put(w, true);
                    queue.enqueue(w);
                }
            }
        }
    }

    public IHeap<Path> reconstructPath(String end, String weightCriteria){

        IQueue<ImmutableStack<ArcServices>> queue = new List<>();
        Comparator c;
        if (weightCriteria.equals(DISTANCE)){
            c = new Path.PathDistanceComparator();
        }else {
            c = new Path.PathTimeComparator();
        }

        try {
            ArcServices endEdge = pathsWithNoTolls.get(end).getCurrent();
            ImmutableStack<ArcServices> endStack = new ImmutableStack<>();
            endStack.push(endEdge);
            while (!queue.isEmpty()){
                evaluatePath(queue, queue.dequeue(), c);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return pathsToWithNoTolls;
    }

    private void evaluatePath(IQueue<ImmutableStack<ArcServices>> queue, ImmutableStack<ArcServices> s, Comparator c) throws Exception{

        String v = s.peek().getEndVertex();
        ListIterator<ArcServices> edges = new ListIterator<>(pathsWithNoTolls.get(v));
        for(ArcServices e : edges){
            String w = e.getIniVertex();
            if(w != iniVertex){
                s.push(e);
                queue.enqueue(s);
            }else{
                Path p = new Path(s.toList());
                pathsToWithNoTolls.add(p, c);
            }
        }


    }

    private void initializeMarking(){
        Iterator<String> iter = graph.getAdjList().keys();
        while (iter.hasNext()){
            String key = iter.next();
            try {
                marked.put(key, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
