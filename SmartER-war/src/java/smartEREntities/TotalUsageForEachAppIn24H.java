package smartEREntities;

import java.io.Serializable;

/**
 * This class is to store total usage for each appliance within 24 hours for one particular resident.
 * This entity serves web service ElectricityFacadeREST - findTotalUsageForEachAppIn24H.
 * 
 * @author William
 */
public class TotalUsageForEachAppIn24H implements Serializable{
    int resid;
    double fridge;
    double aircon;
    double washingmachine;

    public TotalUsageForEachAppIn24H() {
        resid = -1;
        fridge = 0.0;
        aircon = 0.0;
        washingmachine = 0.0;
    }

    public TotalUsageForEachAppIn24H(int resid, double fridge, double aircon, double washingmachine) {
        this.resid = resid;
        this.fridge = fridge;
        this.aircon = aircon;
        this.washingmachine = washingmachine;
    }

    public int getResid() {
        return resid;
    }

    public void setResid(int resid) {
        this.resid = resid;
    }

    public double getFridge() {
        return fridge;
    }

    public void setFridge(double fridge) {
        this.fridge = fridge;
    }

    public double getAircon() {
        return aircon;
    }

    public void setAircon(double aircon) {
        this.aircon = aircon;
    }

    public double getWashingmachine() {
        return washingmachine;
    }

    public void setWashingmachine(double washingmachine) {
        this.washingmachine = washingmachine;
    }
    
}
