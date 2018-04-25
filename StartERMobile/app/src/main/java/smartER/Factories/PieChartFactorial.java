package smartER.Factories;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;

import com.example.william.starter_mobile.Constant;
import com.example.william.starter_mobile.R;
import com.example.william.starter_mobile.SmartERMobileUtility;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import smartER.webservice.ChartWebservice;
import smartER.webservice.SmartERUsageWebservice;
import smartER.webservice.SmartERUserWebservice;

public class PieChartFactorial extends AsyncTask<Void, Void, Void> {

    private PieChart chart;
    // context
    private Context context;
    // date selected
    private String dateSelected;
    // server request result
    private JSONObject json;
    private Typeface mTfLight;

    public PieChartFactorial(Context context, PieChart chart, String dateSelected) {
        this.context = context;
        this.chart = chart;
        this.dateSelected = dateSelected;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        Log.d("SmartERDebug", "****Set bar chart****");
        // get server request result
        try {
            json = SmartERUsageWebservice.getAppUsageByResIdAndDate(SmartERMobileUtility.getResId(), dateSelected);
        } catch (Exception ex) {
            Log.e("SmartERDebug", SmartERMobileUtility.getExceptionInfo(ex));
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        h.sendEmptyMessage(0);
    }

    // draw pie chart
    private void drawPieChart(JSONObject json) throws JSONException {
        mTfLight = Typeface.createFromAsset(context.getAssets(), "OpenSans-Light.ttf");
        chart.getDescription().setEnabled(false);
        chart.setExtraOffsets(5, 10, 5, 5);
        chart.setDragDecelerationFrictionCoef(0.95f);
        chart.setCenterTextTypeface(mTfLight);
        //chart.setCenterText(generateCenterSpannableText());
        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);
        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);
        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);
        chart.setDrawCenterText(true);
        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);
        // set data
        setData(json,100);

        chart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
    }

    private void setData(JSONObject json, float range) throws JSONException {
        float mult = range;
        ArrayList<PieEntry> entries = new ArrayList<>();
        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        double[] appUsage = new double[3];
        appUsage[0] = json.getDouble(Constant.WS_KEY_FRIDGE_DAY_SUM);
        appUsage[1] = json.getDouble(Constant.WS_KEY_AC_DAY_SUM);
        appUsage[2] = json.getDouble(Constant.WS_KEY_WM_DAY_SUM);
        double total = appUsage[0] + appUsage[1] + appUsage[2];

        for (int i = 0; i < appUsage.length ; i++) {
            // set label for partition
            String label = "";
            switch (i) {
                case 0:
                    label = Constant.LABEL_FRIDGE;
                    break;
                case 1:
                    label = Constant.LABEL_AC;
                    break;
                case 2:
                    label = Constant.LABEL_WM;
                    break;
                default:
                    break;
            }
            entries.add(new PieEntry((float)(mult * (appUsage[i] / total)), label, appUsage[i]));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        //dataSet.setSelectionShift(0f);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(mTfLight);
        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);
        chart.invalidate();
    }

    // create a handler to toast message on main thread according to the post result
    @SuppressLint("HandlerLeak")
    Handler h = new Handler() {
        public void handleMessage(Message msg){
            if(msg.what == 0) {
                // draw the line chart
                try {
                    drawPieChart(json);
                } catch (Exception ex) {
                    Log.e("SmartERDebug", SmartERMobileUtility.getExceptionInfo(ex));
                }
            } else {

            }
        }
    };

}
