package smartEREntities;

import java.io.Serializable;

/**
 * This class is to store hourly usage information including total usage of three appliances at a hour of a particular day, and
 * resident resident id, address, postcode. This entity class is designed for web service ElectricityFacadeREST method 
 * findTotalUsageOfAllResidentByDateAndHour
 * @author William
 */
public class HourlyUsageInfo implements Serializable {
    int resid;
    String address;
    Short postcode;
    double totalUsage;

    public HourlyUsageInfo() {
    }

    public HourlyUsageInfo(int resid, String address, Short postcode, double totalUsage) {
        this.resid = resid;
        this.address = address;
        this.postcode = postcode;
        this.totalUsage = totalUsage;
    }

    public int getResid() {
        return resid;
    }

    public void setResid(int resid) {
        this.resid = resid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Short getPostcode() {
        return postcode;
    }

    public void setPostcode(Short postcode) {
        this.postcode = postcode;
    }

    public double getTotalUsage() {
        return totalUsage;
    }

    public void setTotalUsage(double totalUsage) {
        this.totalUsage = totalUsage;
    }
    
}
