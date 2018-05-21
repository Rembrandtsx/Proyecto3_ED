package model.logic;
import java.io.*;
import java.util.Comparator;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.data_structures.*;
import model.vo.*;
import org.joda.time.DateTime;
import serializers.*;
import api.ITaxiTripsManager;
import mapsDraw.MapsDrawer;

public class TaxiTripsManager implements ITaxiTripsManager
{
	public static final String DIRECCION_SMALL_JSON = "/taxi-trips-wrvz-psew-subset-small-01-02-2017.json";
	public static final String DIRECCION_MEDIUM_JSON = "/taxi-trips-wrvz-psew-subset-medium-01-02-2017.json";
	public static final String[] DIRECCION_LARGE_JSON = 
			{"/large/taxi-trips-wrvz-psew-subset-02-02-2017.json", "/large/taxi-trips-wrvz-psew-subset-03-02-2017.json", "/large/taxi-trips-wrvz-psew-subset-04-02-2017.json",
					"/large/taxi-trips-wrvz-psew-subset-05-02-2017.json", "/large/taxi-trips-wrvz-psew-subset-06-02-2017.json", "/large/taxi-trips-wrvz-psew-subset-07-02-2017.json",
					"/large/taxi-trips-wrvz-psew-subset-08-02-2017.json"};
	public static final String DIRECCION_STREETS="src/main/resources/Streets.csv";

	
	/**
	 * The instance of Maps Drawer so the GUI gets refrshed
	 */
	private MapsDrawer dubijante = new MapsDrawer();
	
	/**
	 * The date range of the given files
	 */
	private DateRange filesDateRange;

	/**
	 * Graph representing Chicago streets using the services
	 */
	private DiGraph<String, AdjacentServices, ArcServices> serviceGraph;

	/**
	 * Reference distance to create the vertices
	 */
	private double dx;
	
	/**
	 * Linked list of Streets
	 */
	private ArrayList<Entry<String, ArrayList<String>>> streets;
	/**
	 * All the services organized by time ranges, distributed in 30 buckets (ranges) of time
	 */
	private TimeRangeBucket servicesByDateRange;

	public TaxiTripsManager() {
		initializeManager();
	}


	/**
	 * Loads the services given by the JSON files and construct the necessary
	 * objects to fulfill the requirements
	 * @param files, location of the files
	 * @return true if the services where loaded, false otherwise
	 */
	@Override
	public IDiGraph cargarSistema(String[] files, double refDistance) throws Exception {
		initializeManager();
		
		dx = refDistance;
		MultipleFileIterator iter = new MultipleFileIterator(files);
		
		while(iter.hasNext()) {
			Service s = iter.next();
			if(isServiceValid(s)){
				addVertex(s);
			}
		}
		
		return serviceGraph;
	}

	private void initializeManager(){
		serviceGraph = new DiGraph<>();
		servicesByDateRange = null;
		filesDateRange = null;
		dx = 0.0;
		loadStreets();
	}
	private void loadStreets() {
		 
	        String line = "";
	        String cvsSplitBy = ";";
	        streets = new ArrayList<Entry<String, ArrayList<String>>>();
	        try (BufferedReader br = new BufferedReader(new FileReader(DIRECCION_STREETS))) {

	            while ((line = br.readLine()) != null) {

	                // use comma as separator
	                String[] splitted = line.split(cvsSplitBy);
	                String key= splitted[0];
	                ArrayList value = new ArrayList<String>();
	                for (int i =6 ; i <splitted.length;i++) {
	                	value.add(splitted[i]);
	                }
	                
	                Entry entrada = new Entry(key, value);
	                streets.add(entrada);

	            }

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}

	private void addVertex(Service s) {
		String id = s.getTripId();
		Iterator<String> ids = serviceGraph.keys();
		boolean verticesCreated = false;

		AdjacentServices as1 = null;
		AdjacentServices as2 = null;



		double servicePLatitude = s.getPickupLatitude();
		double servicePLongitude = s.getPickupLongitude();
		double serviceDLatitude = s.getDropOffLatitude();
		double serviceDLongitude = s.getDropOffLongitude();

		// create new vertex from initial point
		// check if the service belongs to some vertex and if the coordinates are valid
		if(!(servicePLatitude == 0.0 && servicePLongitude == 0.0) && !(serviceDLatitude == 0.0 && serviceDLongitude == 0.0)){
			String graphKeyIni = servicePLatitude + "|" + servicePLongitude;
			as1 = getClosestServiceCluster(servicePLatitude, servicePLongitude);
			if(as1 == null){
				as1 = new AdjacentServices(servicePLatitude, servicePLongitude,
						id, AdjacentServices.INI_SERVICE_VERTEX);
				serviceGraph.addVertex(graphKeyIni, as1);
			}
			as1.addService(s.getTripId());

			// create new vertex form ending point
			String graphKeyEnd = serviceDLatitude + "|" + serviceDLongitude;
			as2 = getClosestServiceCluster(serviceDLatitude, serviceDLongitude);
			if(as2 == null){
				as2 = new AdjacentServices(serviceDLatitude, serviceDLongitude,
						id, AdjacentServices.END_SERVICE_VERTEX);
				serviceGraph.addVertex(graphKeyEnd, as2);
			}
			as2.addService(s.getTripId());

			// add edge
			ArcServices edge = serviceGraph.getInfoEdge(as1.toString(), as2.toString());
			// verify if edge exists
			if(edge != null){
				// add one to the number of services
				edge.addService();
			}else {
				edge = new ArcServices();
				edge.addService();
				serviceGraph.addEdge(as1.toString(), as2.toString(), edge);
			}
			// update weight values
			edge.updateMeanDistance(s.getTripMiles());
			edge.updateMeanEarnings(s.getTripTotal());
			edge.updateMeanTimeStamp(s.getTripSeconds());
			if(s.getTolls() > 0){
				edge.updateNumberOfTolls();
			}

			// maintain reference to the incident vertices
			edge.setIniVertex(graphKeyIni);
			edge.setEndVertex(graphKeyEnd);
		}
	}
	
	public AdjacentServices getClosestServiceCluster(double serviceLatitude, double serviceLongitude){

		Iterator<String> ids = serviceGraph.keys();
		LinkedList<AdjacentServices> srvList = new List<>();
		LinkedList<Double> distances = new List<>();

		// get all clusters to which the service could belong
		while (ids.hasNext()){
			AdjacentServices srv = serviceGraph.getInfoVertex(ids.next());
			double distance = AdjacentServices.calculateDistance(srv.getLatRef(), srv.getLonRef(),
					serviceLatitude,serviceLongitude);

			if(distance <= dx){
				srvList.add(srv);
				distances.add(distance);
			}
		}

		// extract only the cluster with minimum distance
		double minDistance = Double.MAX_VALUE;
		AdjacentServices minAs = null;
		srvList.listing();
		distances.listing();
		for (int i = 0; i < srvList.size(); i++) {
			AdjacentServices as = srvList.getCurrent();
			double distance = distances.getCurrent();
			if (distance < minDistance){
				minDistance = distance;
				minAs = as;
			}
			srvList.next();
			distances.next();
		}

		// if there are clusters with equal distances, returns the first in the list
		return minAs;
	}

	/**
	 * Req1: Returns the most congested vertex in the graph;
	 * the one with most inward and outer edges.
	 * @return information of the most congested vertex.
	 */
	@Override
	public AdjacentServices mostCongestedVertex(){

		int maxSum = 0;
		AdjacentServices maxAs = null;
		Iterator<String> iterKeys = serviceGraph.keys();
		while (iterKeys.hasNext()){
			String key = iterKeys.next();
			int sum = serviceGraph.indegreeOfVertex(key) + serviceGraph.outdegreeOfVertex(key);
			if(sum > maxSum){
				maxSum = sum;
				maxAs = serviceGraph.getInfoVertex(key);
			}
		}

		return maxAs;
	}
	
	
	

	/**
	 * Req2: Returns a list with the strongly connected components of the graph
	 * and their information.
	 * @return strongly connected components
	 */
	public LinkedList<StrongComponent> getStrongComponents(){
		serviceGraph.calculateStronglyConnectedComponents();
		IHashMap<String, Integer> sc = serviceGraph.getStrongComponents();
		LinkedList<StrongComponent> strongComponents = new List<>();
		initializeStrongComponents(strongComponents, serviceGraph.getCountStrongComponents());
		ListIterator<String> componentsIds = new ListIterator<>(sc.toList());

		try {
			for (String id: componentsIds) {
				int numID = sc.get(id);	
				AdjacentServices infoVertex = serviceGraph.getInfoVertex(id);
				strongComponents.listing();
				for (int i = 0; i < strongComponents.size(); i++) {
					StrongComponent component = strongComponents.getCurrent();
					
					if (numID == component.getNumID()){
						component.addVertex(infoVertex);
						break;
					}
					strongComponents.next();
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		return strongComponents;
	}

	private void initializeStrongComponents(LinkedList<StrongComponent> strongComponents, int size) {
		strongComponents.listing();
		for (int i = 0; i < size; i++) {
			strongComponents.add(new StrongComponent(StrongComponent.makeRandomColor(), i));
		}
	}

	/**
	 * Req3: Returns a map with the vertices keys and their corresponding densities (percentage of
	 * inward and outer services ).
	 * @return Hash map with densities
	 */
	public IHashMap<String, Double> getVerticesRadius(){

		ListIterator<String> keys = new ListIterator<>(serviceGraph.getAdjList().toList());
		IHashMap<String, Double> verticesDensity = new SeparateChainingHashMap<>();
		for (String id: keys) {
			try {
				verticesDensity.put(id, serviceGraph.getVertexDensity(id));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return verticesDensity;
	}

	//TODO: Get random coordinates from Streets.csv for Req4, Req5, and Req6
	/**
	 * csv randomizer
	 * @return String[] where String[1] = lng & String[2] = lat
	 */
	public String[] getRandomStreets() {
		String [] puntosRetorno = new String[2];
			int valorAcceso1= (int) (Math.random()*streets.size());
			int valorAcceso2= (int)(Math.random()*streets.get(valorAcceso1).getValue().size());
			puntosRetorno = streets.get(valorAcceso1).getValue().get(valorAcceso2).split("\\s+");
		
		
		return puntosRetorno;
	}
	
	//TODO: Get the nearest Cluster (Adjacent Services) to a set of Lat & Lng

	/**
	 * Gets nearest cluster of services (AdjacentServices)
	 * @param lat reference latitude
	 * @param lon reference longitude
	 * @return AdjacentServices
	 */
	public AdjacentServices getClusterNear(double lat, double lon) {

		double minDistance = Double.MAX_VALUE;
		AdjacentServices nearestCluster = new AdjacentServices();
		ListIterator<String> keys = new ListIterator<>(serviceGraph.getAdjList().toList());

		try {
			for (String k : keys) {
				AdjacentServices as = serviceGraph.getInfoVertex(k);
				double distance = AdjacentServices.calculateDistance(lat, lon, as.getLatRef(), as.getLonRef());
				if (distance > minDistance){
					minDistance = distance;
					nearestCluster = as;
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}


		return nearestCluster;
	}

	/**
	 * Returns random pair of vertices in the graph.
	 * verticesIds[0] = initial vertex id
	 * verticesIds[1] = final vertex id
	 * @return an array with the ids of the pair of vertices
	 */
	public String[] getRandomInitialAndFinalVertices(){

		String[] verticesIds = new String[2];

		String[] iniCoordinates = getRandomStreets();
		String[] endCoordinates = getRandomStreets();

		AdjacentServices as1 = getClusterNear(Double.parseDouble(iniCoordinates[1]), Double.parseDouble(iniCoordinates[0]));
		AdjacentServices as2 = getClusterNear(Double.parseDouble(endCoordinates[1]), Double.parseDouble(endCoordinates[0]));

		verticesIds[0] = as1.toString();
		verticesIds[1] = as2.toString();

		if (as1.equals(as2)){
			verticesIds =  getRandomInitialAndFinalVertices();
		}
		return verticesIds;
	}

	/**
	 * Req4: Returns the information of the shortest distance path between two vertices.
	 * @param ini id of initial vertex
	 * @param end id of final vertex
	 * @return A collection of edges
	 */
	public LinkedList<ArcServices> getShortestPathByDistance(String ini, String end){
		ShortestPathServiceGraph sp = new ShortestPathServiceGraph(serviceGraph, ini, ShortestPathServiceGraph.DISTANCE);
		return sp.reconstructPath(end);
	}

	/**
	 * Req5: Returns the information of 2 shortest timed paths between two vertices:
	 * arrSp[0]: shortest path between the initial and the final vertices.
	 * arrSp[1]: shortest path between the final and the initial vertices.
	 * @param ini id of initial vertex
	 * @param end id of final vertex
	 * @return An array with the shortest paths
	 */
	public LinkedList<ArcServices>[] getShortestPathByTime(String ini, String end){

		LinkedList<ArcServices>[] arrSp = (LinkedList<ArcServices>[]) new Comparable[2];
		arrSp[0] = new List<>();
		arrSp[1] = new List<>();

		ShortestPathServiceGraph sp0 = new ShortestPathServiceGraph(serviceGraph, ini, ShortestPathServiceGraph.TIME);
		arrSp[0] = sp0.reconstructPath(end);
		ShortestPathServiceGraph sp1 = new ShortestPathServiceGraph(serviceGraph, end, ShortestPathServiceGraph.TIME);
		arrSp[1] = sp1.reconstructPath(ini);

		return arrSp;
	}

	/**
	 * Req6: Returns a descending sorted collection of paths between two vertices
	 * which doesn´t contain any tolls. The collection is sorted by distance.
	 * @param ini id of initial vertex
	 * @param end id of final vertex
	 * @return collection of paths
	 */
	public IHeap<Path> getSortedPathsWithNoTollsByDistance(String ini, String end){
		BreadthSearchServiceGraph bs = new BreadthSearchServiceGraph(serviceGraph, ini);
		IHeap<Path> paths = bs.reconstructPath(end, BreadthSearchServiceGraph.DISTANCE);
		Comparator c = new Path.PathDistanceComparator();
		paths.heapSort(MaxHeap.DESCENDING, c);
		return paths;
	}

	/**
	 * Req6: Returns an ascending sorted collection of paths between two vertices
	 * which doesn´t contain any tolls. The collection is sorted by time.
	 * @param ini id of initial vertex
	 * @param end id of final vertex
	 * @return collection of paths
	 */
	public IHeap<Path> getSortedPathsWithNoTollsByTime(String ini, String end){
		BreadthSearchServiceGraph bs = new BreadthSearchServiceGraph(serviceGraph, ini);
		IHeap<Path> paths = bs.reconstructPath(end, BreadthSearchServiceGraph.TIME);
		Comparator c = new Path.PathTimeComparator();
		paths.heapSort(MaxHeap.ASCENDING, c);
		return paths;
	}

	/**
	 * Fills an array with the services within the given time range
	 * @param range date range
	 * @param arr array to be filled
	 */
	public void getServicesByTimeRange(DateRange range, IArrayList<Service> arr) {
		int start = TimeRangeBucket.getBucketKey(range);
		int end = TimeRangeBucket.getBucketKey(range);

		IArrayList<LinkedList<Service>> entries = servicesByDateRange.getBuckets();

		for (int i = start; i <= end; i++) {
			LinkedList<Service> curr = entries.get(i);
			curr.listing();
			for (int j = 0; j < curr.size(); j++) {
				Service s = curr.getCurrent();
				if(s.getTimeStamp().isInRange(range)) {
					arr.add(s);
				}
				curr.next();
			}
		}
	}


	/**
	 * Checks if the given service contains the necessary data
	 * @param s service
	 * @return true if the service is valid, false otherwise
	 */
	private boolean isServiceValid(Service s) {
		return (s.getTripId() != null && !s.getTripId().isEmpty()) &&
				(s.getTaxiId() != null && !s.getTaxiId().isEmpty()) &&
				(s.getTaxiId() != null && !s.getTaxiId().isEmpty()) &&
				(s.getCompany() != null) &&
				(s.getBeginDate() != null && !s.getBeginDate().isEmpty()) &&
				(s.getEndDate() != null && !s.getEndDate().isEmpty());
	}



	/**
	 * Sets the date range given the loaded file
	 * @param f files location
	 * @return a time range that encompases all the loaded services
	 */
	public DateRange getRange(String[] f){
		DateTime low = null;
		DateTime hi = null;

		Pattern p = Pattern.compile("(\\d{2})-(\\d{2})-(\\d{4})");

		for (int i = 0; i < f.length; i++){
			Matcher m = p.matcher(f[i]);
			boolean found = m.find();
			int day = Integer.parseInt(f[i].substring(m.start(1), m.end(1)));
			int month = Integer.parseInt(f[i].substring(m.start(2), m.end(2)));
			int year = Integer.parseInt(f[i].substring(m.start(3), m.end(3)));
			DateTime d = new DateTime(year, month, day, 0,0  );
			if (low == null || d.isBefore(low)){
				low = d;
			}
			if (hi == null || d.isAfter(hi)){
				hi = d;
			}

		}
		hi = hi.plusDays(1);
		return  new DateRange(low, hi);
	}


    public boolean saveJson() {
  
		try(OutputStreamWriter writer = new FileWriter("fileGraph/Output.json")) {
			return ServiceGraphSerializer.saveGraph(writer, serviceGraph);

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

    public DiGraph<String, AdjacentServices, ArcServices> loadJson() {
    			loadStreets();
		try (InputStreamReader reader = new FileReader("fileGraph/Output.json")) {
			serviceGraph = ServiceGraphDeserializer.readGraph(reader);
			return serviceGraph;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}
    
    
}

