package com.example.stepsv2;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

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
        GraphView graph= view.findViewById(R.id.graph);
        BarGraphSeries<DataPoint> series;

        switch (pageNumber){
            case 0:
                 series= new BarGraphSeries<DataPoint>(new DataPoint[] {
                        new DataPoint(0, 1),
                        new DataPoint(0, 0),
                });
                seriescustom(series);
                series.setTitle("1st");
                graph.addSeries(series);
                break;
            case 1:
                series= new BarGraphSeries<DataPoint>(new DataPoint[] {
                        new DataPoint(0, 1),
                        new DataPoint(10, 10),
                        new DataPoint(10, 10),
                });
                seriescustom(series);
                series.setTitle("2nd");
                graph.addSeries(series);
                break;
            case 2:
                series= new BarGraphSeries<DataPoint>(new DataPoint[] {
                        new DataPoint(0, 1),
                        new DataPoint(20, 20),
                        new DataPoint(20, 20),
                        new DataPoint(20, 20),
                });
                seriescustom(series);
                series.setTitle("3rd");
                graph.addSeries(series);
                break;
            case 3:
                series= new BarGraphSeries<DataPoint>(new DataPoint[] {
                        new DataPoint(0, 1),
                        new DataPoint(40, 40),
                        new DataPoint(40, 40),
                        new DataPoint(40, 40),
                        new DataPoint(40, 40),
                });
                seriescustom(series);
                series.setTitle("4th");
                graph.addSeries(series);
                break;
        }
        return view;
    }


    void seriescustom(BarGraphSeries series)
    {
        series.setSpacing(50);
        series.setDrawValuesOnTop(true);
        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });
    }


}
