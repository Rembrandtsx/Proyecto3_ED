package model.vo;

import model.data_structures.LinkedList;

import java.util.Comparator;

/**
 * Representation of a taxi object
 */
public class Taxi implements Comparable<Taxi>
{

	private String taxiId;
	
	private String company;

	private LinkedList<Service> services;

	private double points;
	

	public Taxi(String taxiId, String company, LinkedList<Service> services) {
		this.taxiId = taxiId;
		this.company = company;
		this.services = services;
	}
	
	public void addService(Service s) {
		services.add(s);
	}

	public void setServices(LinkedList<Service> services)
	{
		this.services = services;
	}

	public int getNumberOfServices() {
		return services.size();
	}

	public double getTotalEarnings() {
		double total = 0.0;
		services.listing();
		for (int i = 0; i < services.size(); i++) {
			total = services.getCurrent().getTripTotal();
			services.next();
		}
		return total;
	}
	
	public int getTotalTimeInSeconds() {
		int total = 0;
		services.listing();
		for (int i = 0; i < services.size(); i++) {
			total = services.getCurrent().getTripSeconds();
			services.next();
		}
		return total;
	}
	
	public double getTotalDistanceInMiles() {
		double total = 0.0;
		services.listing();
		for (int i = 0; i < services.size(); i++) {
			total = services.getCurrent().getTripMiles();
			services.next();
		}
		return total;
	}
	
	/**
	 * @return id - taxi_id
	 */
	public String getTaxiId()
	{
		return taxiId;
	}

	/**
	 * @return company
	 */
	public String getCompany()
	{
		return company;
	}
	
	public LinkedList<Service> getServices()
	{
		return services;
	}
	
	

	public void setCompany(String company)
	{
		this.company = company;
	}
	
	@Override
	public int compareTo(Taxi o) 
	{
		return this.taxiId.compareTo(o.getTaxiId());
	}	
	
	@Override
	public String toString() {
		return "Taxi Id: " + taxiId + " Company: " + company + " Total Services: " + services.size() + " Total Distance: " + getTotalDistanceInMiles() +
				" Total Earnings: " + getTotalEarnings() + " Total Time: " + getTotalTimeInSeconds();
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Taxi){
			Taxi otherTaxi = (Taxi)o;
			return this.getTaxiId().equals(otherTaxi.getTaxiId());
		}
		return false;

	}

	public double calculatePoints(){
		double distance = getTotalDistanceInMiles();
		double earnings = getTotalEarnings();
		if(distance > 0 && earnings > 0){
			points = (getTotalEarnings()/getTotalDistanceInMiles()) * services.size();
			return points;
		}else {
			return -1;
		}
	}


	public double getPoints() {
		return points;
	}

	public void setPoints(double points) {
		this.points = points;
	}

	public static class TaxiPointsComparator implements Comparator<Taxi>{

		@Override
		public int compare(Taxi t1, Taxi t2) {

			double diff = t1.getPoints() - t2.getPoints();
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