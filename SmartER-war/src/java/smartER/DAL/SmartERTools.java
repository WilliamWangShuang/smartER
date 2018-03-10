package smartER.DAL;

import java.util.List;
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
}
