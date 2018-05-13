package model.vo;

import model.data_structures.LinkedList;

public class Company implements Comparable<Company> {

    public static final String INDEPENDENT = "Independent Owner";

    private String name;

    private LinkedList<Taxi> taxis;
    
    public Company(String name, LinkedList<Taxi> taxis){
        this.name = name;
        this.taxis = taxis;
    }

    public String getName() {
        return name;
    }

    public LinkedList<Taxi> getTaxis() {
        return taxis;
    }

    public void setTaxis(LinkedList<Taxi> taxis){
        this.taxis = taxis;
    }

    public void addTaxi(Taxi t){
        taxis.add(t);
    }

    public int getNumberOfServices(){
        int count = 0;
        taxis.listing();
        for (int i = 0; i < taxis.size(); i++){
            Taxi curr = taxis.getCurrent();
            count += curr.getNumberOfServices();
            taxis.next();
        }
        return count;
    }

    public boolean isTaxiInCompany(String t){
        taxis.listing();
        for (int i = 0; i < taxis.size(); i++) {
            if (t.equals(taxis.getCurrent().getTaxiId())){
                return true;
            }
            taxis.next();
        }
        return false;
    }

    public int getNumberOfTaxis(){
        return taxis.size();
    }

    @Override
    public String toString(){
        return "Company: " + name + " ; Number of Taxis: " + getNumberOfTaxis() + " ; Number of Services: "+ getNumberOfServices();
    }

    @Override
    public int compareTo(Company o) {
        return this.getNumberOfServices() - o.getNumberOfServices();
    }

    @Override
    public boolean equals(Object c){
        Company toCompare = (Company) c;
        return this.name.equals(toCompare.getName());
    }


}
