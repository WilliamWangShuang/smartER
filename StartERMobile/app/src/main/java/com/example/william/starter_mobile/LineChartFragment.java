package com.example.william.starter_mobile;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.utils.Utils;
import smartER.Factories.LineChartFactoiral;

public class LineChartFragment extends Fragment implements AdapterView.OnItemSelectedListener  {
    private View vLineChartFragment;
    private Spinner viewType;
    private LineChart chart;

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vLineChartFragment = inflater.inflate(R.layout.line_chart_fragment, container, false);
        Utils.init(vLineChartFragment.getContext());
        // get chart from page
        chart =(LineChart) vLineChartFragment.findViewById(R.id.linechart);

        // get spinner of view type from page
        viewType = (Spinner)vLineChartFragment.findViewById(R.id.line_chart_spinner);
        // set listener of spinner
        viewType.setOnItemSelectedListener(this);
        return vLineChartFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        LineChartFactoiral lineChartFactoiral = new LineChartFactoiral(getActivity(), chart, Constant.MAP_VIEW_HOURLY);
        lineChartFactoiral.execute();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
        // get view type from view
        String viewType = adapter.getItemAtPosition(position).toString();
        //MapFragmentFactorial mapFragmentFactorial = null;
        // change map view according to the view type selected
        if (Constant.MAP_VIEW_HOURLY.equals(viewType)) {
            LineChartFactoiral lineChartFactoiral = new LineChartFactoiral(getActivity(), chart, Constant.MAP_VIEW_HOURLY);
            lineChartFactoiral.execute();
        } else if(Constant.MAP_VIEW_DAILY.equals(viewType)) {

        } else {

        }
        //mapFragmentFactorial.execute();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }
}
