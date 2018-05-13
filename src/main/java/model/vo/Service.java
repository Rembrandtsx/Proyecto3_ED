package model.vo;

import java.util.Comparator;

import org.joda.time.DateTime;

/**
 * Representation of a Service object
 */
public class Service implements Comparable<Service>
{	
		
	private String tripId;
	
	private String taxiId;
	
	private String company;
	
	private int tripSeconds;
	
	private double tripMiles;
	
	private double tripTotal;
	
	private DateRange timeStamp;
	
	private String beginDate;
	
	private String endDate;

	private int pickupZone;

	private int dropOffZone;

	private double pickupLatitude;

	private double pickupLongitude;

	private double dropOffLatitude;

	private double dropOffLongitude;

	private double tolls;

	/**
	 * Only used to store the distance between the pickup zone and the mean location of all services
	 */
	private double distanceFromRefLocation;
	
	public Service(String taxiId, String tripId, String company, int tripSeconds, double tripMiles, double tripTotal,
				   String beginDate, String endDate, int pickupZone, int dropOffZone, double pickupLatitude, double pickupLongitude, double dropOffLatitude, double dropOffLongitude, double tolls) {
		this.tripId = tripId;
		this.taxiId = taxiId;
		this.tripSeconds = tripSeconds;
		this.tripMiles = tripMiles;
		this.tripTotal = tripTotal;
		this.company = company;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.pickupZone = pickupZone;
		this.dropOffZone = dropOffZone;
		this.pickupLatitude = pickupLatitude;
		this.pickupLongitude = pickupLongitude;
		this.dropOffLatitude = dropOffLatitude;
		this.dropOffLongitude = dropOffLongitude;
		this.tolls = tolls;
		try{
			this.timeStamp = new DateRange(beginDate, endDate);
		} catch (IllegalArgumentException e){
			this.timeStamp = null;
		}

	}
	
	public Service(String taxiId, String tripId, int tripSeconds, double tripMiles, double tripTotal,
				   String beginDate, String endDate) {
		this.tripId = tripId;
		this.taxiId = taxiId;
		this.tripSeconds = tripSeconds;
		this.tripMiles = tripMiles;
		this.tripTotal = tripTotal;
		this.company = "";
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.timeStamp = new DateRange(beginDate, endDate);
	}

	public String getCompany() {
		return company;
	}
	
	public String getBeginDate() {
		return beginDate;
	}
	
	public String getEndDate() {
		return endDate;
	}

	/**
	 * @return id - Trip_id
	 */
	public String getTripId()
	{
		return tripId;
	}	
	
	/**
	 * @return id - Taxi_id
	 */
	public String getTaxiId() {
		return taxiId;
	}	
	
	/**
	 * @return time - Time of the trip in seconds.
	 */
	public int getTripSeconds() {
		return tripSeconds;
	}

	/**
	 * @return miles - Distance of the trip in miles.
	 */
	public double getTripMiles() {
		return tripMiles;
	}
	
	/**
	 * @return total - Total cost of the trip
	 */
	public double getTripTotal() {
		return tripTotal;
	}
	
	public DateRange getTimeStamp() {
		return timeStamp;
	}

	@Override
	public int compareTo(Service arg0) {
		return 0;
	}
	
	
	@Override
	public boolean equals(Object o) {

		if(o instanceof Service){
			Service otherService = (Service)o;
			return this.getTripId().equals(otherService.getTripId());
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "Trip Id: " + tripId  + "Date: " + beginDate + " - " + endDate + " Time of Arrival: " + tripSeconds + " (sec)";
	}

	public int getPickupZone() {
		return pickupZone;
	}

	public void setPickupZone(int pickupZone) {
		this.pickupZone = pickupZone;
	}

	public int getDropOffZone() {
		return dropOffZone;
	}

	public void setDropOffZone(int dropOffZone) {
		this.dropOffZone = dropOffZone;
	}

	public double getPickupLatitude() {
		return pickupLatitude;
	}

	public void setPickupLatitude(double pickupLatitude) {
		this.pickupLatitude = pickupLatitude;
	}

	public double getPickupLongitude() {
		return pickupLongitude;
	}

	public void setPickupLongitude(double pickupLongitude) {
		this.pickupLongitude = pickupLongitude;
	}

	public double getDistanceFromRefLocation() {
		return distanceFromRefLocation;
	}

	public void setDistanceFromRefLocation(double distanceFromRefLocation) {
		this.distanceFromRefLocation = distanceFromRefLocation;
	}

	public double getDropOffLongitude() {
		return dropOffLongitude;
	}

	public void setDropOffLongitude(double dropOffLongitude) {
		this.dropOffLongitude = dropOffLongitude;
	}

	public double getDropOffLatitude() {
		return dropOffLatitude;
	}

	public void setDropOffLatitude(double dropOffLatitude) {
		this.dropOffLatitude = dropOffLatitude;
	}

	public double getTolls() {
		return tolls;
	}

	public void setTolls(double tolls) {
		this.tolls = tolls;
	}

	public static class ServiceDateComparator implements Comparator<Service>
	{

		@Override
		public int compare(Service s1, Service s2) {
			DateTime dateS1 = DateRange.generateDate(s1.getBeginDate());
			DateTime dateS2 = DateRange.generateDate(s2.getBeginDate());
			return DateRange.compareDates(dateS1, dateS2);
		}


	}

	public static class ServiceDistanceComparator implements Comparator<Service>
	{

		@Override
		public int compare(Service s1, Service s2) {
			double diff = s1.getTripMiles() - s2.getTripMiles();
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
