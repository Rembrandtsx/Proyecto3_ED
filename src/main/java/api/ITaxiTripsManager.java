package api;


import model.data_structures.IArrayList;
import model.data_structures.IDiGraph;
import model.data_structures.IRedBlackTrees;
import model.data_structures.LinkedList;
import model.vo.Service;
import model.vo.Taxi;

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


}
