package smartER.webservice;

public class ChartWebservice {

    public static class ChartUsageEntity {
        private String date;
        private int hour;
        private double totalUsage;
        private int temperature;

        public ChartUsageEntity(String date, int hour, double totalUsage, int temperature) {
            this.date = date;
            this.hour = hour;
            this.totalUsage = totalUsage;
            this.temperature = temperature;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getHour() {
            return hour;
        }

        public double getTotalUsage() {
            return totalUsage;
        }

        public int getTemperature() {
            return temperature;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }

        public void setTotalUsage(double totalUsage) {
            this.totalUsage = totalUsage;
        }

        public void setTemperature(int temperature) {
            this.temperature = temperature;
        }
    }
}
