package smartER.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.william.starter_mobile.Constant;
import com.example.william.starter_mobile.SmartERMobileUtility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SmartERDbUtility {
    // get db helper
    private SmartERDbHelper dbHelper;
    //appliance usage contract
    SmartERContract.ApplianceUsage applianceUsage;

    // constructor
    public SmartERDbUtility(){}

    // constructor with param Context
    public SmartERDbUtility(Context context) {
        dbHelper = new SmartERDbHelper(context);
        applianceUsage = dbHelper.getContract();
    }

    // delete all data in electricity table
    public void truncateElectricityTable() {
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int deletedRows = db.delete(applianceUsage.TABLE_NAME, null, null);
    }

    // insert appliance usage
    public long insertAppUsage(String usageDate, int usageHour, double fridgeUsage, double wsUsage, double acUsage, int temperature) {
        // row id of new usage data in SQLite Table
        long newRowId = -1;
        // Gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(applianceUsage.COLUMN_NAME_RESID, SmartERMobileUtility.getResId());
            values.put(applianceUsage.COLUMN_NAME_USAGEDATE, usageDate);
            values.put(applianceUsage.COLUMN_NAME_USAGEHOUR, usageHour);
            values.put(applianceUsage.COLUMN_NAME_FRIDGEUSAGE, fridgeUsage);
            values.put(applianceUsage.COLUMN_NAME_ACUSAGE, acUsage);
            values.put(applianceUsage.COLUMN_NAME_WMUSAGE, wsUsage);
            values.put(applianceUsage.COLUMN_NAME_TEMPERATURE, temperature);

            // Insert the new row, returning the primary key value of the new row
            newRowId = db.insertOrThrow(applianceUsage.TABLE_NAME, null, values);
        } catch (SQLException ex) {
            Log.e("SmartERDebug", "SQLException when inserting hourly usage.\n" + SmartERMobileUtility.getExceptionInfo(ex));

        } catch (Exception ex) {
            Log.e("SmartERDebug", "Error occurred when inserting hourly usage.\n" + SmartERMobileUtility.getExceptionInfo(ex));
        } finally {
            db.close();
            return newRowId;
        }
    }

    // query current hour appliance usage by date, hour and resId
    public AppUsageEntity getCurrentHourAppUsage(int currentHour, int resId) {
        // get SQLite db
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // create table contract
        SmartERContract.ApplianceUsage applianceUsage = new SmartERContract.ApplianceUsage();
        // date formatter to covert current date to string so that can be used in SQLite query
        DateFormat df = new SimpleDateFormat(Constant.DATE_FORMAT);
        // SQL string
        String queryString = "SELECT " + applianceUsage.COLUMN_NAME_FRIDGEUSAGE  + "," +
                applianceUsage.COLUMN_NAME_ACUSAGE + "," +
                applianceUsage.COLUMN_NAME_WMUSAGE +
                " FROM " + applianceUsage.TABLE_NAME +
                " WHERE " +
                applianceUsage.COLUMN_NAME_RESID + " = " +
                SmartERMobileUtility.getResId() + " AND " +
                applianceUsage.COLUMN_NAME_USAGEDATE + " = '" +
                df.format(new Date()) + "' AND " +
                applianceUsage.COLUMN_NAME_USAGEHOUR + " = " +
                currentHour;
        Log.d("SmartERDebug", "sql:" + queryString);

        // execute query
        Cursor c = db.rawQuery(queryString, null);
        // get the first met query record
        c.moveToFirst();
        // get usage data
        double fridgeData = c.getDouble(0);
        double acData = c.getDouble(1);
        double wmData = c.getDouble(2);

        db.close();
        return new AppUsageEntity(fridgeData, wmData, acData);
    }

    // entity class to transfer appliances usage for current hour
    public class AppUsageEntity {
        // fridge usage
        private double firdgeUsage;
        // washing machine usage
        private double wmUsage;
        // air conditioner usage
        private double acUsage;

        public AppUsageEntity(){}

        public  AppUsageEntity(double fridgeData, double wmData, double acData) {
            this.firdgeUsage = fridgeData;
            this.wmUsage = wmData;
            this.acUsage = acData;
        }

        // getters and setters
        public double getFirdgeUsage() {
            return firdgeUsage;
        }

        public double getWmUsage() {
            return wmUsage;
        }

        public double getAcUsage() {
            return acUsage;
        }

        public void setFirdgeUsage(double firdgeUsage) {
            this.firdgeUsage = firdgeUsage;
        }

        public void setWmUsage(double wmUsage) {
            this.wmUsage = wmUsage;
        }

        public void setAcUsage(double acUsage) {
            this.acUsage = acUsage;
        }
    }
}
