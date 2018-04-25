package com.example.william.starter_mobile;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import smartER.Factories.PieChartFactorial;

public class PieChartFragment extends Fragment {
    private DatePickerDialog.OnDateSetListener date;
    private View vPieFragment;
    private Calendar myCalendar;
    private PieChart chart;
    private EditText edittext;

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vPieFragment = inflater.inflate(R.layout.pie_chart_fragment, container, false);
        // get chart from page
        chart =(PieChart) vPieFragment.findViewById(R.id.piechart);
        // initial calendar
        myCalendar = Calendar.getInstance();
        edittext = (EditText)vPieFragment.findViewById(R.id.piechart_selectdate);
        // my context
        final Context mContext = vPieFragment.getContext();

        // create a datepicker
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        // set datepicker to edit text on page
        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(v.getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String dateSelected = edittext.getText().toString();
                PieChartFactorial pieChartFactorial = new PieChartFactorial(mContext, chart, dateSelected);
                pieChartFactorial.execute();
            }
        });

        return vPieFragment;
    }

    private void updateLabel() {
        String myFormat = Constant.DATE_FORMAT; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        edittext.setText(sdf.format(myCalendar.getTime()));
    }
}
