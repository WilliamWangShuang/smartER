package smartER.DAL;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
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
        return result;
    }
    
    // Calculate total fridge usage in a Electricity list.
    public static double getACTotalUsage(List<Electricity> list) {
        double result = 0.0;
        for (Electricity el : list) {
            result += el.getAcusage().doubleValue();
        }
        return result;
    }
    
    // Calculate total washing machine usage in a Electricity list.
    public static double getWMTotalUsage(List<Electricity> list) {
        double result = 0.0;
        for (Electricity el : list) {
            result += el.getWmusage().doubleValue();
        }
        return result;
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
}
