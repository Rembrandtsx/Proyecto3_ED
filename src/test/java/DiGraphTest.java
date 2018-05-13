import model.data_structures.DiGraph;
import model.data_structures.LinkedList;
import model.data_structures.List;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;

public class DiGraphTest {

    private DiGraph<Integer, String, Double> graph;

    public void setupScenario1(){
        graph = new DiGraph<>();
    }

    public void setupScenario2(){
        setupScenario1();
        graph.addVertex(1, "a");
        graph.addVertex(2, "b");
        graph.addVertex(3, "c");
        graph.addVertex(4, "d");
    }

    @Test
    public void addVertexTest(){
        setupScenario2();
        Assert.assertEquals("Vertices where not added correctly", 4, graph.numVertices());
        graph.setInfoVertex(1, "abcde");
        Assert.assertEquals("Vertex information was not updated correctly", "abcde", graph.getInfoVertex(1));
        Assert.assertTrue(graph.contains(3));
    }

    @Test
    public void addEdgeTest(){

        setupScenario2();
        graph.addEdge(1, 2, 5.0);
        graph.addEdge(3, 4, 10.0);

        Assert.assertEquals("Edges where not added correctly", 2, graph.numEdges());

        double result1 = graph.getInfoEdge(3,4);
        Assert.assertEquals("Edge value is not correct", 10.0, result1, 0.0000001);

        graph.setInfoEdge(1, 2, 100.0);
        double result2 = graph.getInfoEdge(1,2);
        Assert.assertEquals("Edge value was not correctly updated", 100.0, result2, 0.0000001);
    }

    @Test
    public void adj(){
        setupScenario2();
        graph.addEdge(4, 1, 1.0);
        graph.addEdge(4, 2, 1.0);
        graph.addEdge(4, 3, 1.0);

        Iterator<Integer> iter = graph.adj(4).iterator();
        LinkedList<String> vals = new List<>();

        while(iter.hasNext()){
            vals.add(graph.getInfoVertex(iter.next()));
        }

        Assert.assertEquals("Iterator is not correct", 3, vals.size());
    }

}
