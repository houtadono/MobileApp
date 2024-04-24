package vn.id.houta.myapplication.module4;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import vn.id.houta.myapplication.R;
import vn.id.houta.myapplication.database.FirebaseHelper;

public class StatisticFragment extends Fragment {

    private View view;
    private BarChart barChart;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_statistic, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)requireActivity()).setSupportActionBar(toolbar);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayShowTitleEnabled(false);

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), onBackPressedCallback);
        barChart = view.findViewById(R.id.chart);
        new FirebaseHelper().getTimeLearnDaily((lbe, days )-> {
            if (lbe != null && !lbe.isEmpty()) {
                BarDataSet barDataSet = new BarDataSet(lbe, "Số giờ học mỗi ngày");
                barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                barDataSet.setDrawValues(true);
                barDataSet.setColor(Color.GRAY);
                barDataSet.setBarBorderWidth(0.5f);

                BarData barData = new BarData(barDataSet);
                barChart.setData(barData);

                XAxis xAxis = barChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setGranularity(1f);
                xAxis.setTextSize(16f);
                xAxis.setTextColor(Color.GRAY);
                xAxis.setYOffset(10f);
                xAxis.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getAxisLabel(float value, AxisBase axis) {
                        int intValue = (int) value;
                        if (intValue >= 0 && intValue < lbe.size()) {
                            BarEntry entry = lbe.get(intValue);

                            return days.get((int) value);
                        }
                        return "";
                    }
                });

                YAxis yAxis = barChart.getAxisLeft();
                yAxis.setAxisMinimum(0);
                yAxis.setTextSize(16f);
                yAxis.setDrawAxisLine(false);
                yAxis.setTextColor(Color.GRAY);
                yAxis.setXOffset(10f);
                barChart.setDrawMarkers(true);
                barChart.getAxisRight().setEnabled(false);
                barChart.getXAxis().setDrawGridLines(false);
                barChart.setPinchZoom(false);
                barChart.setDoubleTapToZoomEnabled(false);
                barChart.setScaleEnabled(false);
                barChart.setDrawGridBackground(false);
                barChart.setGridBackgroundColor(Color.LTGRAY);
                barChart.getAxisLeft().setSpaceMin(0.5f);
                barChart.getDescription().setText("");
                barChart.getLegend().setXOffset(10f);
                barChart.invalidate();
            }
        });
        return view;
    }

    private String convertTimestampToDDMMYYYY(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
}