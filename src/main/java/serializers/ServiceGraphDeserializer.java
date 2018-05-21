package serializers;

import com.google.gson.stream.JsonReader;
import model.data_structures.*;
import model.vo.AdjacentServices;
import model.vo.ArcServices;

import java.io.IOException;
import java.io.InputStreamReader;

public class ServiceGraphDeserializer {

    public static DiGraph<String, AdjacentServices, ArcServices> readGraph(InputStreamReader in){
        JsonReader reader = new JsonReader(in);
        return new DiGraph<>(readAdjacencyList(reader));
    }

    private static IHashMap<String, Vertex<String, AdjacentServices, ArcServices>> readAdjacencyList(JsonReader reader){
        IHashMap<String, Vertex<String, AdjacentServices, ArcServices>> adjList = new SeparateChainingHashMap<>();

        try {
            reader.beginObject();

            if (reader.hasNext()){
                String adjListName = reader.nextName();
                if (adjListName.equals("adjList")){
                    reader.beginObject();
                    while (reader.hasNext()){
                        String vertexId = reader.nextName();
                        Vertex<String, AdjacentServices, ArcServices> vertex = readVertex(reader ,vertexId);
                        adjList.put(vertexId, vertex);
                    }
                    reader.endObject();
                }else {
                    throw new Exception("Invalid json file format while reading");
                }

            }else{
                throw new Exception("Invalid json file format while reading");
            }
            reader.endObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return adjList;
    }

    private static Vertex<String, AdjacentServices, ArcServices> readVertex(JsonReader reader, String vertexId){

        Vertex<String, AdjacentServices, ArcServices> vertex = new Vertex<>();
        vertex.setId(vertexId);

        try {
            reader.beginObject();
            while (reader.hasNext()){
                String n = reader.nextName();
                if(n.equals("val")){
                    AdjacentServices as = readValue(reader, vertexId);
                    vertex.setVal(as);
                }else if(n.equals("adj")){
                    IHashMap<String, ArcServices> edges = readEdges(reader);
                    vertex.setAdj(edges);
                }
            }
            reader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return vertex;
    }

    private static AdjacentServices readValue(JsonReader reader, String vertexId){
        AdjacentServices as = new AdjacentServices();
        String[] coordenates = vertexId.split("\\|");
        as.setLatRef(Double.parseDouble(coordenates[0]));
        as.setLonRef(Double.parseDouble(coordenates[1]));
        try {
            reader.beginObject();
            while (reader.hasNext()){
                String n = reader.nextName();
                if(n.equals("serviceRefId")){
                    as.setServiceRefId(reader.nextString());
                }else if(n.equals("type")){
                    as.setType(reader.nextString());
                }else if(n.equals("services")){
                    as.setServices(readServices(reader));
                }
            }
            reader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return as;
    }

    private static IArrayList<String> readServices(JsonReader reader){

        IArrayList<String> services = new ArrayList<>();

        try {
            reader.beginArray();
            while (reader.hasNext()){
                services.add(reader.nextString());
            }
            reader.endArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return services;
    }

    private static IHashMap<String, ArcServices> readEdges(JsonReader reader){

        IHashMap<String, ArcServices> adj = new SeparateChainingHashMap<>();

        try {
            reader.beginObject();
            while (reader.hasNext()){
                String adjVertexId = reader.nextName();
                ArcServices edge = readEdge(reader);
                adj.put(adjVertexId, edge);
            }
            reader.endObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return adj;
    }

    private static ArcServices readEdge(JsonReader reader){
        ArcServices edge = new ArcServices();

        try {
            reader.beginObject();
            while (reader.hasNext()){
                String n = reader.nextName();
                if(n.equals("iniVertex")){
                    edge.setIniVertex(reader.nextString());
                }
                else if(n.equals("endVertex")){
                    edge.setEndVertex(reader.nextString());
                }
                else if(n.equals("meanDistance")){
                    edge.setMeanDistance(reader.nextDouble());
                }
                else if(n.equals("meanEarnings")){
                    edge.setMeanEarnings(reader.nextDouble());
                }
                else if(n.equals("meanTimeStamp")){
                    edge.setMeanTimeStamp(reader.nextDouble());
                }
                else if(n.equals("numberOfTolls")){
                    edge.setNumberOfTolls(reader.nextDouble());
                }
                else if(n.equals("numberOfServices")){
                    edge.setMeanEarnings(reader.nextInt());
                }
            }
            reader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return edge;
    }

}
