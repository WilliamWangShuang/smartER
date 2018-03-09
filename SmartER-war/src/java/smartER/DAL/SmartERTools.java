package smartER.DAL;

/**
 * This class is for implementing all tool methods that can be used throughout the whole solution
 * @author William
 */
public class SmartERTools {
    public static double getTotalUsage(double fridgeUsage, double acUsage, double wmUsage) {
        return fridgeUsage + acUsage + wmUsage;
    }
}
