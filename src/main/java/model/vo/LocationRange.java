package model.vo;

import model.data_structures.LinkedList;

public class LocationRange {

    private static double longitude1;

    private static double latitude1;

    private static double longitude2;

    private static double latitude2;

    private double distance;

    private LinkedList<Service> services;

    public LocationRange(double longitude1, double latitude1, double longitude2, double latitude2, double distance, LinkedList<Service> services) {
        this.longitude1 = longitude1;
        this.latitude1 = latitude1;
        this.longitude2 = longitude2;
        this.latitude2 = latitude2;
        this.distance = distance;
        this.services = services;
    }

    public double calculateDistance(){
        return calculateDistance(latitude1, longitude1, latitude2, longitude2);
    }

    public static double calculateDistance (double lat1, double lon1, double lat2, double lon2) {

        final int R = 6371*1000; // Radious of the earth in meters
        Double latDistance = toRad(lat2-lat1);
        Double lonDistance = toRad(lon2-lon1);
        Double a = Math.sin(latDistance/2) * Math.sin(latDistance/2) + Math.cos(toRad(lat1))
                * Math.cos(toRad(lat2)) * Math.sin(lonDistance/2) * Math.sin(lonDistance/2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        Double distance = R * c;
        return  convertMetersToMiles(distance);
    }

    public void addService(Service s){
        services.add(s);
    }

    private static double toRad(double val){
        return (val * Math.PI)/ 180 ;
    }

    public double getLongitude1() {
        return longitude1;
    }

    public void setLongitude1(double longitude1) {
        this.longitude1 = longitude1;
    }

    public double getLatitude1() {
        return latitude1;
    }

    public void setLatitude1(double latitude1) {
        this.latitude1 = latitude1;
    }

    public double getLongitude2() {
        return longitude2;
    }

    public void setLongitude2(double longitude2) {
        this.longitude2 = longitude2;
    }

    public double getLatitude2() {
        return latitude2;
    }

    public void setLatitude2(double latitude2) {
        this.latitude2 = latitude2;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public LinkedList<Service> getServices() {
        return services;
    }

    public void setServices(LinkedList<Service> services) {
        this.services = services;
    }

    public static int getGroup (double val){
        double division = Math.floor(val/0.1) + 1;
        float result = Math.round(division);
        return (int) result;
    }

    public static double convertMetersToMiles(double meters){
        return meters / 1609.34;
    }
}
