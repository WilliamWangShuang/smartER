package smartEREntities;

import java.io.Serializable;
import java.util.Date;
import smartER.DAL.SmartERTools;
/**
 *
 * @author William
 */
public class DailySumInfo implements Serializable  {
    private Date usagedate;
    private double totalFridgeUsage;
    private double totalACUsage;
    private double totalWMUsage;
    private int temperature;
    private int noOfDays;

    public DailySumInfo() {
    }

    public DailySumInfo(Date usagedate, double totalFridgeUsage, double totalACUsage, double totalWMUsage, int temperature) {
        this.usagedate = usagedate;
        this.totalFridgeUsage = totalFridgeUsage;
        this.totalACUsage = totalACUsage;
        this.totalWMUsage = totalWMUsage;
        this.temperature = temperature;
        this.noOfDays = noOfDays;
    }

    // Get total usage for at this hour on this day for this resident
    public double getTotalUsage(){
        return SmartERTools.round(getTotalFridgeUsage() + getTotalACUsage() + getTotalWMUsage(), 2);
    }
    
    public Date getUsagedate() {
        return usagedate;
    }

    public void setUsagedate(Date usagedate) {
        this.usagedate = usagedate;
    }

    public double getTotalFridgeUsage() {
        return totalFridgeUsage;
    }

    public void setTotalFridgeUsage(double totalFridgeUsage) {
        this.totalFridgeUsage = this.totalFridgeUsage + totalFridgeUsage;
    }

    public double getTotalACUsage() {
        return totalACUsage;
    }

    public void setTotalACUsage(double totalACUsage) {
        this.totalACUsage = this.totalACUsage + totalACUsage;
    }

    public double getTotalWMUsage() {
        return totalWMUsage;
    }

    public void setTotalWMUsage(double totalWMUsage) {
        this.totalWMUsage = this.totalWMUsage + totalWMUsage;
    }

    public int getTemperature() {
        return temperature / 24;
    }

    public void setTemperature(int temperature) {
        this.temperature = this.temperature + temperature;
    }

    public int getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(int noOfDays) {
        this.noOfDays = noOfDays;
    }
    
    
}
