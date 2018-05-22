package api;


import model.data_structures.*;
import model.vo.*;

import java.util.Comparator;
import java.util.Iterator;


/**
 * API para la clase de logica principal  
 */
public interface ITaxiTripsManager 
{

	/**
	 * Loads the services given by the JSON files and construct the necessary
	 * objects to fulfill the requirements
	 * @param files, location of the files
	 * @return true if the services where loaded, false otherwise
	 */
	IDiGraph cargarSistema(String[] files, double refDistance) throws Exception;

	boolean saveJson();

	DiGraph<String, AdjacentServices, ArcServices> loadJson();

	AdjacentServices mostCongestedVertex();

	LinkedList getStrongComponents();

	IHashMap<String, Double> getVerticesRadius();

	String[] getRandomStreets();

	AdjacentServices getClusterNear(double lat, double lon);

	String[] getRandomInitialAndFinalVertices(String[] callesIni, String[] callesFin);
	LinkedList<ArcServices> getShortestPathByDistance(String ini, String end);

	ArrayList<LinkedList<ArcServices>> getShortestPathByTime(String string, String string2);

	IHeap<Path> getSortedPathsWithNoTollsByDistance(String ini, String end);

	IHeap<Path> getSortedPathsWithNoTollsByTime(String ini, String end);

}
