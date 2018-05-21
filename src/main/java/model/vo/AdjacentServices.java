package model.vo;

import java.io.Serializable;
import java.util.Objects;

import model.data_structures.ArrayList;
import model.data_structures.IArrayList;

/**
 * Class in charge of grouping services given a distance from a reference point
 * @author s.lemus
 *
 */
public class AdjacentServices implements Comparable<AdjacentServices>, Serializable {
	
	public static final String INI_SERVICE_VERTEX = "INI";
	
	public static final String END_SERVICE_VERTEX = "END";
	
	private double latRef;
	
	private double lonRef;
	
	private String serviceRefId;

	private String type;
	
	IArrayList<String> services;

	public AdjacentServices(double latRef, double lonRef, String serviceRefId, ArrayList<String> services, String type) {
		this.latRef = latRef;
		this.lonRef = lonRef;
		this.serviceRefId = serviceRefId;
		this.services = services;
		this.type = type;
	}
	
	public AdjacentServices(double latRef, double lonRef, String serviceRefId, String type) {
		this.latRef = latRef;
		this.lonRef = lonRef;
		this.serviceRefId = serviceRefId;
		this.services = new ArrayList<>();
		this.type = type;
	}

	public AdjacentServices() {
		this.services = new ArrayList<>();
	}


    public static double calculateDistance (double lat1, double lon1, double lat2, double lon2) {

        final int R = 6371*1000; // Radious of the earth in meters
        Double latDistance = toRad(lat2-lat1);
        Double lonDistance = toRad(lon2-lon1);
        Double a = Math.sin(latDistance/2) * Math.sin(latDistance/2) + Math.cos(toRad(lat1))
                * Math.cos(toRad(lat2)) * Math.sin(lonDistance/2) * Math.sin(lonDistance/2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        Double distance = R * c;
        return  distance;
    }
    
    private static double toRad(double val){
        return (val * Math.PI)/ 180 ;
    }
    
    public static double convertMetersToMiles(double meters){
        return meters / 1609.34;
    }

    public void addService(String s){
        services.add(s);
    }

	public double getLatRef() {
		return latRef;
	}

	public void setLatRef(double latRef) {
		this.latRef = latRef;
	}

	public double getLonRef() {
		return lonRef;
	}
	public String getType() {
		return type;
		
	}

	public void setLonRef(double lonRef) {
		this.lonRef = lonRef;
	}

	public String getServiceRefId() {
		return serviceRefId;
	}

	public IArrayList<String> getServices(){ return services; }

	public void setServiceRefId(String serviceRefId) {
		this.serviceRefId = serviceRefId;
	}
	
	public boolean containsService(String id){
		for (int i = 0; i < services.size(); i++) {
			if(services.get(i).equals(id)){
				return true;
			}
		}
		return false;
	}


	public void setType(String type) {
		this.type = type;
	}

	public void setServices(IArrayList<String> services) {
		this.services = services;
	}

	@Override
	public int compareTo(AdjacentServices o) {
		return this.serviceRefId.compareTo(o.getServiceRefId());
	}

	@Override
	public String toString() {
		return latRef + "|" + lonRef;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		AdjacentServices that = (AdjacentServices) o;
		return Double.compare(that.latRef, latRef) == 0 &&
				Double.compare(that.lonRef, lonRef) == 0;
	}

}
