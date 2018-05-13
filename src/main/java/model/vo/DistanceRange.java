package model.vo;

import model.data_structures.ArrayList;
import model.data_structures.IArrayList;

public class DistanceRange {

    private double lowerDistance;

    private double higherDistance;

    private IArrayList<Service> services;


    public DistanceRange(double lowerDistance, double higherDistance, IArrayList<Service> services) {
        this.lowerDistance = lowerDistance == 0? 0: lowerDistance + 1;
        this.higherDistance = higherDistance;
        this.services = services;
    }

    public static Integer getGroup(double distance){
        Integer roundedDistance = ((Double)distance).intValue();
        return roundedDistance + 1;
    }

    public IArrayList<Service> getServicesByDistance(double distance){
        IArrayList arr = new ArrayList();
        for (int i = 0; i < services.size(); i++){
            if(services.get(i).getTripMiles() == distance){
                arr.add(distance);
            }
        }
        return arr;
    }

    public void addService(Service s){
        services.add(s);
    }

    public double getLowerDistance() {
        return lowerDistance;
    }

    public void setLowerDistance(double lowerDistance) {
        this.lowerDistance = lowerDistance;
    }

    public double getHigherDistance() {
        return higherDistance;
    }

    public void setHigherDistance(double higherDistance) {
        this.higherDistance = higherDistance;
    }

    public IArrayList<Service> getServices() {
        return services;
    }

    public void setServices(ArrayList<Service> services) {
        this.services = services;
    }
}

