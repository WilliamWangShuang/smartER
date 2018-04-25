package com.example.william.starter_mobile;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.utils.Utils;

import smartER.Factories.BarChartFactorial;
import smartER.Factories.LineChartFactoiral;

public class BarChartFragment  extends Fragment implements AdapterView.OnItemSelectedListener {
    private View vBarChartFragment;
    private Spinner viewType;
    private BarChart chart;

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vBarChartFragment = inflater.inflate(R.layout.bar_chart_fragement, container, false);
        Utils.init(vBarChartFragment.getContext());
        // get chart from page
        chart =(BarChart) vBarChartFragment.findViewById(R.id.barchart);

        // get spinner of view type from page
        viewType = (Spinner)vBarChartFragment.findViewById(R.id.bar_chart_spinner);
        // set listener of spinner
        viewType.setOnItemSelectedListener(this);
        return vBarChartFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        BarChartFactorial barChartFactoiral = new BarChartFactorial(getActivity(), chart, Constant.MAP_VIEW_DAILY);
        barChartFactoiral.execute();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
        // get view type from view
        String viewType = adapter.getItemAtPosition(position).toString();
        //MapFragmentFactorial mapFragmentFactorial = null;
        // change map view according to the view type selected
        if (Constant.MAP_VIEW_HOURLY.equals(viewType)) {
            BarChartFactorial barChartFactoiral = new BarChartFactorial(getActivity(), chart, Constant.MAP_VIEW_HOURLY);
            barChartFactoiral.execute();
        } else if(Constant.MAP_VIEW_DAILY.equals(viewType)) {
            BarChartFactorial barChartFactoiral = new BarChartFactorial(getActivity(), chart, Constant.MAP_VIEW_DAILY);
            barChartFactoiral.execute();
        } else {
            BarChartFactorial barChartFactoiral = new BarChartFactorial(getActivity(), chart, Constant.MAP_VIEW_DAILY);
            barChartFactoiral.execute();
        }
        //mapFragmentFactorial.execute();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }
}
