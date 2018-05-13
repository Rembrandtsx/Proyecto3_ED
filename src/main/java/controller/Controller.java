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

}
