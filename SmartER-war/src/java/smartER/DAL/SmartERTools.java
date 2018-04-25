package smartER.DAL;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import smartEREntities.DailySumInfo;
import smartEREntities.Electricity;

/**
 * This class is for implementing all tool methods that can be used throughout the whole solution
 * @author William
 */
public class SmartERTools {
    // Calculate total fridge usage in a Electricity list.
    public static double getFridgeTotalUsage(List<Electricity> list) {
        double result = 0.0;
        for (Electricity el : list) {
            result += el.getFridgeusage().doubleValue();
        }
        return round(result, 2);
    }
    
    // Calculate total fridge usage in a Electricity list.
    public static double getACTotalUsage(List<Electricity> list) {
        double result = 0.0;
        for (Electricity el : list) {
            result += el.getAcusage().doubleValue();
        }
        return round(result, 2);
    }
    
    // Calculate total washing machine usage in a Electricity list.
    public static double getWMTotalUsage(List<Electricity> list) {
        double result = 0.0;
        for (Electricity el : list) {
            result += el.getWmusage().doubleValue();
        }
        return round(result, 2);
    }
    
    // Find usage by resid and date
    public static List<Electricity> getUsageByResidAndDate(int resid, String usagedate, EntityManager em) throws Exception{
        List<Electricity> usageList = null;
        try {
            // Convert param usagedate to Date Type
            Date paramDate = new SimpleDateFormat(Constant.DATE_FORMAT).parse(usagedate);
            // Create query to find all usage data on the specific date for this resident
            Query query = em.createNamedQuery(Electricity.GET_BY_RESID_DATE);
            // Set parameters for query
            query.setParameter("resId", resid);
            query.setParameter("usagedate", paramDate);
            // Execute query
            usageList = query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
        return usageList;
    }
    
    // Find monthly usage
    public static List<Electricity> getUsageByResidAndMonth (int resid, int month, EntityManager em) throws Exception {
        List<Electricity> usageList = null;
        try {
            // get min and max day
            Calendar cal = Calendar.getInstance();
            int currYear = cal.get(Calendar.YEAR);
            int realMonth = month - 1;
            cal.set(currYear, realMonth, 1);
            SimpleDateFormat f = new SimpleDateFormat(Constant.DATE_FORMAT);
            Date mindate = f.parse(f.format(cal.getTime()));
            int maxDay;
            if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
                maxDay = 31;
            } else if ( month == 4 || month == 6 || month == 9 || month == 11) {
                maxDay = 30;
            } else {
                if (currYear % 4 == 0)
                    maxDay = 29;
                else
                    maxDay = 28;
            }
            // get max date
            cal.set(currYear, realMonth, maxDay);
            Date maxdate = f.parse(f.format(cal.getTime()));
                        
            Query query = em.createNamedQuery(Electricity.GET_BY_RESID_MONTH);
            query.setParameter("resId", resid);
            query.setParameter("minday", mindate);
            query.setParameter("maxday", maxdate);
            usageList = query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
        
        return usageList;
    }

    // Find total usage for all residents by date
    public static List<Electricity> getUsageByDateForAllResident(String usagedate, EntityManager em) throws Exception {
        List<Electricity> usageList = null;
        try {
            // Convert param usagedate to Date Type
            Date paramDate = new SimpleDateFormat(Constant.DATE_FORMAT).parse(usagedate);
            // Create query to find all usage data on the specific date for this resident
            Query query = em.createNamedQuery(Electricity.GET_BY_DATE);
            // Set parameters for query
            query.setParameter("usagedate", paramDate);
            // Execute query
            usageList = query.getResultList();
        } catch (Exception ex) {
            throw ex;
        }
        return usageList;
    }
    
    // Round a double to 2 decimal.
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
