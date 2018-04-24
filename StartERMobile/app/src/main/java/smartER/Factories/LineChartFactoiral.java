package smartER.Factories;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.william.starter_mobile.Constant;
import com.example.william.starter_mobile.SmartERMobileUtility;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.renderer.YAxisRenderer;

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

public class LineChartFactoiral extends AsyncTask<Void, Void, List<ChartWebservice.ChartUsageEntity>> {
    private LineChart chart;
    private String viewType;
    // usages entity, used when view type is hourly
    private List<ChartWebservice.ChartUsageEntity> usageEntities;
    // context
    private Context context;

    public LineChartFactoiral(Context context, LineChart chart, String viewType) {
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
        Log.d("SmartERDebug", "****Set line chart****");
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
            Calendar calMonth = new GregorianCalendar(2018,2,3);
            int lastMonth = calMonth.get(Calendar.MONTH);

            // construct usage entities according to view type
            if (Constant.MAP_VIEW_HOURLY.equals(viewType)) {
                // construct data entities to provide resource for line chart.
                constructHoulyUsageEntities(resid, yesterday);
            } else if (Constant.MAP_VIEW_DAILY.equals(viewType)) {

            } else {

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
    private void drawLineChart(List<ChartWebservice.ChartUsageEntity> usageEntities) {
        List<Entry> entries1 = new ArrayList<>();
        List<Entry> entries2 = new ArrayList<>();
        // set x-axis size
        int[] xAxis = new int[usageEntities.size()];
        // set y-axis size
        double[] yAxis1 = new double[usageEntities.size()];
        double[] yAxis2 = new double[usageEntities.size()];

        // create x,y-axis values
        for (int i = 0; i < usageEntities.size(); i++) {
            xAxis[i] = usageEntities.get(i).getHour();
            yAxis1[i] = usageEntities.get(i).getTotalUsage();
            yAxis2[i] = usageEntities.get(i).getTemperature();
        }

        //set x-axis values
        final String[] hours = new String[xAxis.length];

        // set each node on chart
        for (int i = 0; i < xAxis.length; i++){
            //set x-axis values
            hours[i] = "" + xAxis[i];
            //set node
            entries1.add(new Entry(xAxis[i], (float)yAxis1[i]));
            entries2.add(new Entry(xAxis[i], (float)yAxis2[i]));
        }

        if(entries1.size() >= 3 && entries2.size() >= 3) {
            //implementing IAxisValueFormatter interface to show hour values not as float/decimal
            IAxisValueFormatter formatter = new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return hours[(int)value];
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
            LineDataSet dataSet1 = new LineDataSet(entries1, "This is left Y");
            dataSet1.setColor(Color.rgb(240, 238, 70));
            dataSet1.setLineWidth(2.5f);
            dataSet1.setCircleColor(Color.rgb(240, 238, 70));
            dataSet1.setCircleRadius(5f);
            dataSet1.setFillColor(Color.rgb(240, 238, 70));
            dataSet1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            dataSet1.setDrawValues(true);
            dataSet1.setValueTextSize(10f);
            dataSet1.setValueTextColor(Color.rgb(240, 238, 70));

            // initialize right
            LineDataSet dataSet2 = new LineDataSet(entries2, "This is right Y");
            dataSet1.setColor(Color.rgb(100, 129, 35));
            dataSet1.setLineWidth(2.5f);
            dataSet1.setCircleColor(Color.rgb(120, 128, 35));
            dataSet1.setCircleRadius(5f);
            dataSet1.setFillColor(Color.rgb(120, 129, 35));
            dataSet1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            dataSet1.setDrawValues(true);
            dataSet1.setValueTextSize(10f);
            dataSet1.setValueTextColor(Color.rgb(120, 129, 35));

            dataSet1.setAxisDependency(chart.getAxisLeft().getAxisDependency());
            dataSet2.setAxisDependency(chart.getAxisRight().getAxisDependency());
            // add left and right Y-axis dataset
            LineData lineData = new LineData(dataSet1, dataSet2);
            // set dataset
            chart.setData(lineData);

            chart.notifyDataSetChanged();
            chart.postInvalidate();
        } else {
            Toast.makeText(context, "No enough data to generate Line chart. Please try tomorrow.", Toast.LENGTH_LONG);
        }
    }

    // create a handler to toast message on main thread according to the post result
    @SuppressLint("HandlerLeak")
    Handler h = new Handler() {
        public void handleMessage(Message msg){
            if(msg.what == 0) {
                // draw the line chart
                drawLineChart(usageEntities);
            } else {
                ;
            }
        }
    };
}
