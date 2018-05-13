package model.vo;

import java.io.Serializable;

import model.data_structures.ArrayList;
import model.data_structures.IArrayList;

public class ArcServices implements Serializable{


    private double meanDistance;

    private double meanEarnings;

    private double meanTimeStamp;

    private double numberOfTolls;

    private int numberOfServices;

    public ArcServices(){

        meanDistance = 0.0;
        meanEarnings = 0.0;
        meanTimeStamp = 0.0;
        numberOfTolls = 0.0;
        numberOfServices = 0;
    }

    // Arc´s weight is calculated on demand

    public void updateMeanDistance(double d){
        meanDistance = (meanDistance + d)/numberOfServices;
    }

    public void updateMeanEarnings(double e){
        meanEarnings = (meanEarnings + e)/numberOfServices;
}

    public void updateMeanTimeStamp(double t){
        meanTimeStamp = (meanTimeStamp + t)/numberOfServices;
    }

    public void updateNumberOfTolls(double t){

        numberOfTolls = (numberOfTolls + t);
    }

    public void addService(){
        numberOfServices++;
    }

    public void setMeanDistance(double meanDistance) {
        this.meanDistance = meanDistance;
    }

    public void setMeanEarnings(double meanEarnings) {
        this.meanEarnings = meanEarnings;
    }

    public void setMeanTimeStamp(double meanTimeStamp) {
        this.meanTimeStamp = meanTimeStamp;
    }

    public void setNumberOfTolls(double numberOfTolls) {
        this.numberOfTolls = numberOfTolls;
    }

    public void setNumberOfServices(int numberOfServices) {
        this.numberOfServices = numberOfServices;
    }
}
