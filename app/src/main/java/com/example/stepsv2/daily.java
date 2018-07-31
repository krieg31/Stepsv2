package com.example.stepsv2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.StackedBarChart;
import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.StackedBarModel;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;


public class daily extends Fragment {

    private int pageNumber;

    public static daily newInstance(int page) {
        daily fragment = new daily();
        Bundle args = new Bundle();
        args.putInt("num", page);
        fragment.setArguments(args);
        return fragment;
    }

    public daily() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments() != null ? getArguments().getInt("num") : 1;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_daily, container, false);
        ValueLineChart mCubicValueLineChart = view.findViewById(R.id.cubiclinechart);
        BarChart graph= view.findViewById(R.id.graph);

        switch (pageNumber){
            case 0:
                graph.setVisibility(View.VISIBLE);
                mCubicValueLineChart.setVisibility(View.INVISIBLE);
                graph.addBar(new BarModel(2.3f, 0xFF123456));
                graph.addBar(new BarModel(2.f,  0xFF343456));
                graph.addBar(new BarModel(3.3f, 0xFF563456));
                graph.addBar(new BarModel(1.1f, 0xFF873F56));
                graph.addBar(new BarModel(2.7f, 0xFF56B7F1));
                graph.addBar(new BarModel(2.f,  0xFF343456));
                graph.addBar(new BarModel(0.4f, 0xFF1FF4AC));
                graph.addBar(new BarModel(4.f,  0xFF1BA4E6));
                graph.startAnimation();
                break;
            case 1:
                mCubicValueLineChart.setVisibility(View.VISIBLE);
                graph.setVisibility(View.INVISIBLE);
                ValueLineSeries series = new ValueLineSeries();
                series.setColor(0xFF56B7F1);
                series.addPoint(new ValueLinePoint("Jan", 0f));
                series.addPoint(new ValueLinePoint("Jan", 2.4f));
                series.addPoint(new ValueLinePoint("Feb", 3.4f));
                series.addPoint(new ValueLinePoint("Mar", .4f));
                series.addPoint(new ValueLinePoint("Apr", 1.2f));
                series.addPoint(new ValueLinePoint("Mai", 2.6f));
                series.addPoint(new ValueLinePoint("Jun", 1.0f));
                series.addPoint(new ValueLinePoint("Jul", 3.5f));
                series.addPoint(new ValueLinePoint("Aug", 2.4f));
                series.addPoint(new ValueLinePoint("Sep", 2.4f));
                series.addPoint(new ValueLinePoint("Oct", 3.4f));
                series.addPoint(new ValueLinePoint("Nov", .4f));
                series.addPoint(new ValueLinePoint("Dec", 1.3f));
                series.addPoint(new ValueLinePoint("Jan", 0f));

                mCubicValueLineChart.addSeries(series);
                mCubicValueLineChart.startAnimation();
                break;
            case 2:
                graph.setVisibility(View.INVISIBLE);
                mCubicValueLineChart.setVisibility(View.INVISIBLE);
                break;
        }
        return view;
    }
}
