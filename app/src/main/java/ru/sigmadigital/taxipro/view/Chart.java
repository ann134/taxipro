package ru.sigmadigital.taxipro.view;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.sigmadigital.taxipro.R;
import ru.sigmadigital.taxipro.models.Driver;
import ru.sigmadigital.taxipro.util.DateFormatter;

public class Chart {

    private BarChart chart;
    private Context context;
    private boolean animation;


    public Chart(BarChart chart, Context context, boolean animation) {
        this.chart = chart;
        this.context = context;
        this.animation = animation;

        initChart();
    }


    private void initChart() {

        //Axis
        YAxis yAxis = chart.getAxisLeft();
        yAxis.setEnabled(false);
        yAxis.setTextSize(13);
        yAxis.setTextColor(Color.WHITE);
        yAxis.setAxisMinimum(0f);
        yAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return "";
            }
        });

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(15);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(7f);
        xAxis.setGranularity(1);
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        //legend
        Legend l = chart.getLegend();
        l.setFormSize(20f);
        l.setTextSize(15);
        l.setTextColor(Color.WHITE);
        l.setFormToTextSpace(5f);
        l.setXEntrySpace(10f);


        //property
        chart.setTouchEnabled(false);
        chart.getAxisRight().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setClipValuesToContent(false);
    }


    public void setData(List<Driver.WeekDay> weekDays) {

        List<Float> totalByDayOfWeek = new ArrayList<>();
        totalByDayOfWeek.add(0f);
        totalByDayOfWeek.add(0f);
        totalByDayOfWeek.add(0f);
        totalByDayOfWeek.add(0f);
        totalByDayOfWeek.add(0f);
        totalByDayOfWeek.add(0f);
        totalByDayOfWeek.add(0f);

        for (Driver.WeekDay weekDay : weekDays) {

            Date date = DateFormatter.getDateFromStringUTCTimeZone(weekDay.getDate());
            if (date != null) {
                int day = DateFormatter.getDayOfWeek(date);

                String numberOnly = weekDay.getTotal().replaceAll("[^0-9]", "");
                Float total = Float.parseFloat(numberOnly);

                totalByDayOfWeek.set(day, total);
            }
        }

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0.5f, totalByDayOfWeek.get(0)));
        entries.add(new BarEntry(1.5f, totalByDayOfWeek.get(1)));
        entries.add(new BarEntry(2.5f, totalByDayOfWeek.get(2)));
        entries.add(new BarEntry(3.5f, totalByDayOfWeek.get(3)));
        entries.add(new BarEntry(4.5f, totalByDayOfWeek.get(4)));
        entries.add(new BarEntry(5.5f, totalByDayOfWeek.get(5)));
        entries.add(new BarEntry(6.5f, totalByDayOfWeek.get(6)));

        BarDataSet set = new BarDataSet(entries, "BarDataSet");
        set.setColor(context.getResources().getColor(R.color.bottomBarButtonGreen));
        set.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.valueOf((int) value) + "\u20BD";
            }
        });


        BarData data = new BarData(set);
        data.setBarWidth(0.5f); // set custom bar width
        data.setValueTextColor(context.getResources().getColor(R.color.textGrayDark));
        data.setValueTextSize(10f);

        chart.setData(data);
        chart.setFitBars(true); // make the x axis fit exactly all bars

        if (animation)
            chart.animateY(1000);

        chart.getLegend().setEnabled(false);
        chart.setBackgroundColor(context.getResources().getColor(R.color.white));

        chart.invalidate(); // refresh
    }


}
