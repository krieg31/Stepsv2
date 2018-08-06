package com.example.stepsv2;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.charts.StackedBarChart;
import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.BarModel;
import org.eazegraph.lib.models.StackedBarModel;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class daily extends Fragment {

    private int pageNumber;
    JSONArray jsonArray;
    int temp;
    int sum=0;

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
        BarChart graph= view.findViewById(R.id.graph);
        TextView totaldis = view.findViewById(R.id.totaldis);
        EventBus.getDefault().register(this);

        switch (pageNumber){
            case 0:
                graph.setVisibility(View.VISIBLE);
                graph.addBar(new BarModel(3000, Color.parseColor("#e0ae4b")));
                graph.addBar(new BarModel(2000, Color.parseColor("#e0ae4b")));
                graph.addBar(new BarModel(250, Color.parseColor("#e0ae4b")));
                graph.addBar(new BarModel(700, Color.parseColor("#e0ae4b")));
                graph.addBar(new BarModel(1000,  Color.parseColor("#e0ae4b")));
                graph.addBar(new BarModel(4000, Color.parseColor("#e0ae4b")));
                graph.addBar(new BarModel(3750,  Color.parseColor("#e0ae4b")));
                graph.setBackgroundColor(Color.parseColor("#a1a79c"));
                break;
            case 1:
                graph.setVisibility(View.VISIBLE);
                for (int i=0;i<31;i++)
                {
                    graph.addBar(new BarModel(1000+i*100,Color.parseColor("#e0ae4b")));
                    sum+=1000+i*100;
                    totaldis.setText(""+sum);
                }
                graph.setBackgroundColor(Color.parseColor("#f6ffff"));
                break;
        }
        return view;
    }
    @Override
    public void onDestroyView ()
    {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void sendstat(main_frag.sendstat event) {
        jsonArray=event.jsonArray;
    }

}
