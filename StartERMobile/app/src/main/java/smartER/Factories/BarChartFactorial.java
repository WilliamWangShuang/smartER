package smartER.Factories;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.example.william.starter_mobile.Constant;
import com.example.william.starter_mobile.SmartERMobileUtility;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import smartER.webservice.ChartWebservice;
import smartER.webservice.SmartERUsageWebservice;

public class BarChartFactorial extends AsyncTask<Void, Void, List<ChartWebservice.ChartUsageEntity>> {

    private BarChart chart;
    private String viewType;
    // usages entity, used when view type is hourly
    private List<ChartWebservice.ChartUsageEntity> usageEntities;
    // context
    private Context context;

    public BarChartFactorial(Context context, BarChart chart, String viewType) {
        this.context = context;
        this.chart = chart;
        this.viewType = viewType;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected List<ChartWebservice.ChartUsageEntity> doInBackground(Void... params) {
        Log.d("SmartERDebug", "****Set bar chart****");
        // get data by ws
        try {
            // get current user resid
            int resid = SmartERMobileUtility.getResId();
            // TODO: get date of yesterday. For demo purpose, set to 2018-3-3
            //Calendar cal = Calendar.getInstance();
            //cal.add(Calendar.DATE, -1);
            //Date yesterday = cal.getTime();
            Calendar cal = new GregorianCalendar(2018,2,3);
            Date yesterday = cal.getTime();

            // TODO: get month of last month. For demo purpose, set to march
            //Calendar cal = Calendar.getInstance();
            //cal.add(Calendar.MONTH, -1);
            //int lastMonth = cal.get(Calendar.MONTH);
            int lastMonth = 3;

            // construct usage entities according to view type
            if (Constant.MAP_VIEW_HOURLY.equals(viewType)) {
                // construct data entities to provide resource for line chart.
                constructHoulyUsageEntities(resid, yesterday);
            } else if (Constant.MAP_VIEW_DAILY.equals(viewType)) {
                constructDailyUsageEntities(resid, lastMonth);
            } else {
                // construct data entities to provide resource for line chart.
                constructDailyUsageEntities(resid, lastMonth);
            }

        } catch (Exception ex) {
            Log.e("SmartERDebug", SmartERMobileUtility.getExceptionInfo(ex));
        }

        return usageEntities;
    }

    @Override
    protected void onPostExecute(List<ChartWebservice.ChartUsageEntity> result) {
        h.sendEmptyMessage(0);
    }


    private void constructDailyUsageEntities(int resid, int month) throws IOException, JSONException {
        // get hourly usages
        List<JSONObject> allMonthlyUsage = SmartERUsageWebservice.getMonthlyUsageByResIdAndMonth(resid, month);
        usageEntities = new ArrayList<>();

        for (JSONObject jsonObj : allMonthlyUsage) {
            int hour = -1;
            double totalUsage = jsonObj.getDouble(Constant.WS_KEY_MAP_TOTAL_USAGE);
            int temperature = jsonObj.getInt(Constant.WS_KEY_TEMPERATURE);
            String date = jsonObj.getString(Constant.WS_KEY_DATE);
            ChartWebservice.ChartUsageEntity entity = new ChartWebservice.ChartUsageEntity(date, hour, totalUsage, temperature);
            usageEntities.add(entity);
        }
    }

    // construct data entities to provide resource for line chart.
    private void constructHoulyUsageEntities(int resid, Date yesterday) throws IOException, JSONException {
        // get hourly usages
        List<JSONObject> allHourlyUsage = SmartERUsageWebservice.getHourlyUsageByResIdAndDate(resid, yesterday);
        usageEntities = new ArrayList<>();

        for (JSONObject jsonObj : allHourlyUsage) {
            int hour = jsonObj.getInt(Constant.WS_KEY_MAP_TIME);
            double totalUsage = jsonObj.getDouble(Constant.WS_KEY_MAP_TOTAL_USAGE);
            int temperature = jsonObj.getInt(Constant.WS_KEY_TEMPERATURE);
            String date = jsonObj.getString(Constant.WS_KEY_DATE);
            ChartWebservice.ChartUsageEntity entity = new ChartWebservice.ChartUsageEntity(date, hour, totalUsage, temperature);
            usageEntities.add(entity);
        }
    }

    // draw line chart
    private void drawBarChart(List<ChartWebservice.ChartUsageEntity> usageEntities) {
        ArrayList<BarEntry> entries1 = new ArrayList<BarEntry>();

        // set x-axis size
        int[] xAxis = new int[usageEntities.size()];
        // set y-axis size
        double[] yAxis1 = new double[usageEntities.size()];

        // create x,y-axis values
        for (int i = 0; i < usageEntities.size(); i++) {
            xAxis[i] = Constant.MAP_VIEW_HOURLY.equals(viewType) ? usageEntities.get(i).getHour() : Integer.parseInt(usageEntities.get(i).getDate());
            yAxis1[i] = usageEntities.get(i).getTotalUsage();
        }

        //set x-axis values
        final String[] xValues = new String[xAxis.length];

        // set each node on chart
        for (int i = 0; i < xAxis.length; i++){
            //set x-axis values
            xValues[i] = "" + xAxis[i];
            //set node
            entries1.add(new BarEntry(xAxis[i], (float)yAxis1[i]));
        }

        BarData d  = null;
        if (xAxis.length > 0) {
            //implementing IAxisValueFormatter interface to show hour values not as float/decimal
            IAxisValueFormatter formatter = new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    int intValue = (int)value;
                    if (xValues.length > intValue && intValue > 0)
                        return xValues[intValue];
                    else
                        return "";
                }
            };

            // set x-axis values
            XAxis xAxisFromChart = chart.getXAxis();
            xAxisFromChart.setDrawAxisLine(true);
            xAxisFromChart.setValueFormatter(formatter);
            // minimum axis-step (interval) is 1,if no, the same value will be displayed multiple times
            xAxisFromChart.setGranularity(1);
            xAxisFromChart.setPosition(XAxis.XAxisPosition.BOTTOM);

            // initialize left
            BarDataSet set1 = new BarDataSet(entries1, "This is left Y");
            set1.setColor(Color.rgb(60, 220, 78));
            set1.setValueTextColor(Color.rgb(60, 220, 78));
            set1.setValueTextSize(10f);
            set1.setAxisDependency(chart.getAxisLeft().getAxisDependency().LEFT);


            float groupSpace = 0.06f;
            float barSpace = 0.02f;
            float barWidth = 0.15f;
            d = new BarData(set1);
            d.setBarWidth(barWidth);
        }

        // set dataset
        chart.setData(d);
        chart.setNoDataText("No enough data to generate Line chart. Please try tomorrow for daily view, or next hour for houly view.");
        chart.notifyDataSetChanged();
        chart.postInvalidate();
    }

    // create a handler to toast message on main thread according to the post result
    @SuppressLint("HandlerLeak")
    Handler h = new Handler() {
        public void handleMessage(Message msg){
            if(msg.what == 0) {
                // draw the line chart
                drawBarChart(usageEntities);
            } else {

            }
        }
    };
}
