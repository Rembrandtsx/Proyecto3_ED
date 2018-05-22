package model.vo;

import model.data_structures.LinkedList;
import model.data_structures.List;
import model.data_structures.ListIterator;

import java.util.Comparator;

/**
 * A class that represents a collection of edges of a graph
 */
public class Path implements Comparable<Path> {

    private LinkedList<ArcServices> edges;

    private double totalDistance;

    private double totalTime;

    public Path(){
        edges = new List<>();
    }

    public Path(LinkedList<ArcServices> edges) {
        this.edges = edges;
    }

    public LinkedList<ArcServices> getEdges() {
        return edges;
    }


    public void setEdges(LinkedList<ArcServices> edges) {
        this.edges = edges;
    }

    public void addEdge(ArcServices edge){
        edges.add(edge);
    }

    public double getTotalDistance(){
        LinkedList<ArcServices> copy = edges.getCopy();
        ListIterator<ArcServices> path = new ListIterator<>(copy);
        totalDistance = 0.0;
        for (ArcServices e: path) {
            totalDistance += e.getMeanDistance();
        }

        return  totalDistance;
    }

    public double getTotalTime(){
        LinkedList<ArcServices> copy = edges.getCopy();
        ListIterator<ArcServices> path = new ListIterator<>(copy);
        totalTime = 0.0;
        for (ArcServices e: path) {
            totalTime += e.getMeanTimeStamp();
        }
        return  totalTime;
    }


    @Override
    public int compareTo(Path o) {
        return 0;
    }

    public static class PathDistanceComparator implements Comparator<Path>{

        @Override
        public int compare(Path p1, Path p2) {
            double diff = p1.getTotalDistance() - p2.getTotalDistance();
            double e = 0.0000001;
            if(diff < -e){
                return -1;
            }else if (diff > -e && diff < e){
                return 0;
            } else{
                return 1;
            }
        }
    }

    public static class PathTimeComparator implements Comparator<Path>{

        @Override
        public int compare(Path p1, Path p2) {
            double diff = p1.getTotalTime() - p2.getTotalTime();
            double e = 0.0000001;
            if(diff < -e){
                return -1;
            }else if (diff > -e && diff < e){
                return 0;
            } else{
                return 1;
            }
        }
    }

}
