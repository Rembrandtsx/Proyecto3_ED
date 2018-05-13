package model.logic;
import java.io.*;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import model.data_structures.*;
import model.vo.*;
import org.joda.time.DateTime;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import serializers.*;
import api.ITaxiTripsManager;

public class TaxiTripsManager implements ITaxiTripsManager
{
	public static final String DIRECCION_SMALL_JSON = "/taxi-trips-wrvz-psew-subset-small-01-02-2017.json";
	public static final String DIRECCION_MEDIUM_JSON = "/taxi-trips-wrvz-psew-subset-medium-01-02-2017.json";
	public static final String[] DIRECCION_LARGE_JSON = 
			{"/large/taxi-trips-wrvz-psew-subset-02-02-2017.json", "/large/taxi-trips-wrvz-psew-subset-03-02-2017.json", "/large/taxi-trips-wrvz-psew-subset-04-02-2017.json",
					"/large/taxi-trips-wrvz-psew-subset-05-02-2017.json", "/large/taxi-trips-wrvz-psew-subset-06-02-2017.json", "/large/taxi-trips-wrvz-psew-subset-07-02-2017.json",
					"/large/taxi-trips-wrvz-psew-subset-08-02-2017.json"};


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
			edge.updateNumberOfTolls(s.getTolls());
		}
	}

	private AdjacentServices getClosestServiceCluster(double serviceLatitude, double serviceLongitude){

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
//			Gson gson = new GsonBuilder()
//					.registerTypeAdapter(IHashMap.class, new IHashMapAdapter())
//					.registerTypeAdapter(IArrayList.class, new IArrayListAdapter())
//					.registerTypeAdapter(DateTime.class, new DateTimeAdapter())
//					.create();
//			gson.toJson(serviceGraph, writer);

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

    public DiGraph<String, AdjacentServices, ArcServices> loadJson() {

		try (InputStreamReader reader = new FileReader("fileGraph/Output.json")) {
			serviceGraph = ServiceGraphDeserializer.readGraph(reader);
			return serviceGraph;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}
}
