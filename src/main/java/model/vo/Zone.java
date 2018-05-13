package model.vo;

import model.data_structures.LinkedList;

public class Zone implements Comparable<Zone> {

    private int id;

    private LinkedList<Service> services;
    
    public Zone(int id, LinkedList<Service> services){
        this.id = id;
        this.services = services;
    }


    public int getId(){
        return id;
    }

    public LinkedList<Service> getServices(){
        return services;
    }

    public void addService(Service s){
        services.add(s);
    }

    @Override
    public boolean equals(Object zone){
        Zone toCompare = (Zone) zone;
        return this.id == toCompare.getId();
    }

    @Override
    public int compareTo(Zone o) {
        return 0;
    }

    @Override
    public String toString(){
        return "";
    }
}
