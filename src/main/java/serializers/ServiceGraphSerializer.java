package serializers;

import com.google.gson.stream.JsonWriter;
import model.data_structures.*;
import model.vo.AdjacentServices;
import model.vo.ArcServices;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;

public class ServiceGraphSerializer {

    public static boolean saveGraph(OutputStreamWriter stream, DiGraph<String, AdjacentServices, ArcServices> graph){
        JsonWriter writer = new JsonWriter(stream);
        try {
            boolean successfulWrite = writeAdjacencyList(
                    writer.beginObject().name("adjList"), graph);
            writer.endObject();
            writer.close();
            return successfulWrite;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean writeAdjacencyList(JsonWriter writer, DiGraph<String, AdjacentServices, ArcServices> graph){
        try {
            writer.beginObject();
            IHashMap<String, Vertex<String, AdjacentServices, ArcServices>> map = graph.getAdjList();
            Iterator<String> keys = map.keys();
            boolean successful = false;
            while(keys.hasNext()){
                String key = keys.next();
                writer.name(key).beginObject();
                successful = writeValue(writer, graph.getInfoVertex(key));
                successful &= writeAdjacentVertices(writer, graph.getvertex(key));
                if(!successful){
                    return false;
                }
                writer.endObject();
            }
            writer.endObject();
            return successful;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean writeValue(JsonWriter writer, AdjacentServices as){
        try {
            writer.name("val").beginObject();
            writer.name("serviceRefId").value(as.getServiceRefId());
            writer.name("type").value(as.getType());
            writer.name("services").beginArray();
            IArrayList<String> services = as.getServices();
            for (int i = 0; i < services.size(); i++) {
                writer.value(services.get(i));
            }
            writer.endArray();
            writer.endObject();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean writeAdjacentVertices(JsonWriter writer, Vertex<String, AdjacentServices, ArcServices> vertex){

        try {
            writer.name("adj").beginObject();
            IHashMap<String, ArcServices> adj = vertex.getAdj();
            Iterator<String> iter = adj.keys();
            while (iter.hasNext()){
                String key = iter.next();
                ArcServices edge = adj.get(key);
                writer.name(key).beginObject();
                writer.name("iniVertex").value(edge.getIniVertex());
                writer.name("endVertex").value(edge.getEndVertex());
                writer.name("meanDistance").value(edge.getMeanDistance());
                writer.name("meanEarnings").value(edge.getMeanEarnings());
                writer.name("meanTimeStamp").value(edge.getMeanTimeStamp());
                writer.name("numberOfTolls").value(edge.getNumberOfTolls());
                writer.name("numberOfServices").value(edge.getNumberOfServices());
                writer.endObject();
            }
            writer.endObject();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
