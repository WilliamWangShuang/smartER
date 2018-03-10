package smartEREntities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is to store hours of peak usage of one resident. This class is used to serve web service ElectricityFacadeREST - method findPeakHourByResid
 * 
 * @author William
 */
public class PeakUsageHourInfo implements Serializable  {
    Date peakDate;
    int peakHour;
    double peakUsage;

    public PeakUsageHourInfo() {
        peakDate = null;
        peakHour = 0;
        peakUsage = 0.0;
    }

    public PeakUsageHourInfo(Date peakDate, int peakHour, double peakUsage) {
        this.peakDate = peakDate;
        this.peakHour = peakHour;
        this.peakUsage = peakUsage;
    }

    public Date getPeakDate() {
        return peakDate;
    }

    public void setPeakDate(Date peakDate) {
        this.peakDate = peakDate;
    }

    public int getPeakHour() {
        return peakHour;
    }

    public void setPeakHour(int peakHour) {
        this.peakHour = peakHour;
    }

    public double getPeakUsage() {
        return peakUsage;
    }

    public void setPeakUsage(double peakUsage) {
        this.peakUsage = peakUsage;
    }
    
    
}
