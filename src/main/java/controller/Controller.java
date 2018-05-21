package controller;

import api.ITaxiTripsManager;
import model.data_structures.*;
import model.logic.TaxiTripsManager;
import model.vo.*;

import java.util.Iterator;

public class Controller 
{
	/**
	 * modela el manejador de la clase l�gica
	 */
	private static ITaxiTripsManager manager = new TaxiTripsManager();

	//Parte 1, Requerimiento 4
	/**
	 * Loads the services given by the JSON files and construct the necessary
	 * objects to fulfill the requirements
	 * @param files, location of the files
	 * @return true if the services where loaded, false otherwise
	 */
	public static IDiGraph cargarSistema(String[] files, double refDistance) throws Exception {
		return manager.cargarSistema(files, refDistance);
	}

	public static boolean saveJson() {
		return manager.saveJson();
	}

	public static DiGraph<String, AdjacentServices, ArcServices> loadJson(){
		return manager.loadJson();
	}

	public static AdjacentServices mostCongestedVertex(){
		return manager.mostCongestedVertex();
	}
	public static LinkedList getStrongComponents() {
		return manager.getStrongComponents();
	}

	public static IHashMap<String, Double> getVerticesRadius() {
		// TODO Auto-generated method stub
		return manager.getVerticesRadius();
	}

	public static String[] getRandomStreets() {
		// TODO Auto-generated method stub
		return manager.getRandomStreets();
	}
	public static AdjacentServices getClusterNear(double lat, double lon) {
		return manager.getClusterNear(lat,lon);
	}

	public static String[] getRandomVertices(String[] callesIni, String[] callesFin) {
		// TODO Auto-generated method stub
		return manager.getRandomInitialAndFinalVertices(callesIni,callesFin);
	}
	public static LinkedList<ArcServices> getShortestPathByDistance(String ini, String end){
		return manager.getShortestPathByDistance(ini,end);
	}

}
