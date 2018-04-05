package smartER.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.william.starter_mobile.SmartERMobileUtility;

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

        try {
            // Gets the data repository in write mode
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(applianceUsage.COLUMN_NAME_RESID, SmartERMobileUtility.getResId());
            values.put(applianceUsage.COLUMN_NAME_USAGEDATE, usageDate);
            values.put(applianceUsage.COLUMN_NAME_USAGEHOUR, usageHour);
            values.put(applianceUsage.COLUMN_NAME_FRIDGEUSAGE, fridgeUsage);
            values.put(applianceUsage.COLUMN_NAME_ACUSAGE, wsUsage);
            values.put(applianceUsage.COLUMN_NAME_WMUSAGE, acUsage);
            values.put(applianceUsage.COLUMN_NAME_TEMPERATURE, temperature);

            // Insert the new row, returning the primary key value of the new row
            newRowId = db.insertOrThrow(applianceUsage.TABLE_NAME, null, values);
        } catch (SQLException ex) {
            Log.e("SmartERDebug", "SQLException when inserting hourly usage.\n" + SmartERMobileUtility.getExceptionInfo(ex));

        } catch (Exception ex) {
            Log.e("SmartERDebug", "Error occurred when inserting hourly usage.\n" + SmartERMobileUtility.getExceptionInfo(ex));
        } finally {
            return newRowId;
        }
    }
}
