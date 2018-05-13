package model.vo;

import model.data_structures.ArrayList;

public class ZoneRange {

    private int beginZone;

    private int endZone;

    private ArrayList<Service> services;
    
    public ZoneRange(int beginZone, int endZone, ArrayList<Service> services) {
        this.beginZone = beginZone;
        this.endZone = endZone;
        this.services = services;
    }

    public int getBeginZone() {
        return beginZone;
    }

    public void setBeginZone(int beginZone) {
        this.beginZone = beginZone;
    }

    public int getEndZone() {
        return endZone;
    }

    public void setEndZone(int endZone) {
        this.endZone = endZone;
    }

    public ArrayList<Service> getServices() {
        return services;
    }

    public void setServices(ArrayList<Service> services) {
        this.services = services;
    }

    public boolean isInRange(Service s){
        return beginZone == s.getPickupZone() && endZone == s.getDropOffZone();
    }
}
