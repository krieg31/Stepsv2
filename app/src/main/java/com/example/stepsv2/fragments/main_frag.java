package com.example.stepsv2.fragments;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.stepsv2.methods.JSONSharedPreferences;
import com.example.stepsv2.activity.ProfileActivity;
import com.example.stepsv2.R;
import com.example.stepsv2.activity.StartActivity;
import com.example.stepsv2.graph.CustomMarkerView;
import com.example.stepsv2.graph.MyValueFormatter;
import com.example.stepsv2.graph.MyXAxisValueFormatter;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class main_frag extends Fragment {

    private Button start;
    private Button profile;
    private ArcProgress arcProgress;
    private int progress;
    private int maxarc;
    private int progresum;
    private int todaysum;


    private static final String APP_PREFERENCES = "mysettings";
    private static final String APP_PREFERENCES_CHALLENGE_PROGRESS = "cprogress";
    private static final String APP_PREFERENCES_CHALLENGE_MAX = "cmax";
    private static final String APP_GRAPH= "graph";
    private static final String APP_DATE= "date";
    private static final int APP_TODAYRUN = 0;
    private SharedPreferences mSettings;

    Calendar calendar = Calendar.getInstance();
    private String date= new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(new Date());
    private JSONArray jsonArray =new JSONArray();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_frag, container, false);

        mSettings = this.getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        start = view.findViewById(R.id.Start);
        profile = view.findViewById(R.id.profile_btn);
        profile.setVisibility(View.VISIBLE);
        profile.setBackgroundColor(Color.TRANSPARENT);
        arcProgress = view.findViewById(R.id.arc_progress);
        ConstraintLayout frameLayout= view.findViewById(R.id.frameLayout2);
        frameLayout.setBackgroundResource(R.drawable.box_src);

        CustomMarkerView markerView = new CustomMarkerView(getActivity().getApplicationContext(),R.layout.custom_marker_view);

        BarChart barChart = view.findViewById(R.id.barchart);
        barChart.setMarker(markerView);
        barChart.getDescription().setEnabled(false);
        setlegend(barChart);
        setdata(barChart);


        EventBus.getDefault().register(this);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), StartActivity.class);
                getActivity().startActivity(myIntent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), ProfileActivity.class);
                getActivity().startActivity(myIntent);
            }
        });
        if (mSettings.contains(APP_PREFERENCES_CHALLENGE_PROGRESS)) {
            progresum = mSettings.getInt(APP_PREFERENCES_CHALLENGE_PROGRESS, 0);
            arcProgress.setProgress(progresum);
        }
        if (mSettings.contains(APP_PREFERENCES_CHALLENGE_MAX)) {
            maxarc = mSettings.getInt(APP_PREFERENCES_CHALLENGE_MAX, 0);
            arcProgress.setMax(maxarc);
        }
        if ((mSettings.contains(APP_PREFERENCES_CHALLENGE_MAX)&&(mSettings.contains(APP_PREFERENCES_CHALLENGE_PROGRESS)))) {
            arcProgress.setBottomText(mSettings.getInt(APP_PREFERENCES_CHALLENGE_PROGRESS, 0) + "/" + mSettings.getInt(APP_PREFERENCES_CHALLENGE_MAX, 0));
        }
        if ((mSettings.contains(APP_PREFERENCES_CHALLENGE_MAX)&&(!mSettings.contains(APP_PREFERENCES_CHALLENGE_PROGRESS)))) {
            arcProgress.setBottomText("0/"+mSettings.getInt(APP_PREFERENCES_CHALLENGE_MAX, 0));
        }
        if ((!mSettings.contains(APP_PREFERENCES_CHALLENGE_MAX)&&(mSettings.contains(APP_PREFERENCES_CHALLENGE_PROGRESS)))) {
            arcProgress.setBottomText(mSettings.getInt(APP_PREFERENCES_CHALLENGE_MAX, 0)+"/100");
        }
        if (mSettings.contains(APP_GRAPH)){
            try {
                jsonArray = JSONSharedPreferences.loadJSONArray(getActivity().getApplicationContext(),APP_GRAPH, "graph");
                EventBus.getDefault().postSticky(new sendstat(jsonArray));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(mSettings.contains(APP_DATE)){
            if (!APP_DATE.equals(date))
            {
                SharedPreferences.Editor editor=mSettings.edit();
                editor.putInt(APP_GRAPH, APP_TODAYRUN);
                editor.apply();
                jsonArray.put(APP_TODAYRUN);
                JSONSharedPreferences.saveJSONArray(getActivity().getApplicationContext(),APP_GRAPH, "graph",jsonArray);
                EventBus.getDefault().postSticky(new sendstat(jsonArray));
            }
            else {
                todaysum+=progress;
                jsonArray.put(APP_TODAYRUN);
                SharedPreferences.Editor editor=mSettings.edit();
                EventBus.getDefault().postSticky(new sendstat(jsonArray));
                //editor.putInt(APP_TODAYRUN, todaysum);
                editor.apply();
            }
        }
        else {
            SharedPreferences.Editor editor=mSettings.edit();
            editor.putString(APP_DATE, date);
            editor.apply();
        }



        return view;
    }
    @Override
    public void onResume()
    {
        super.onResume();
        arcProgress.setProgress(progresum);
        arcProgress.setMax(maxarc);

    }
    @Override
    public void onDestroyView ()
    {
        super.onDestroyView();
        if (mSettings.contains(APP_PREFERENCES_CHALLENGE_PROGRESS)){

            SharedPreferences.Editor editor=mSettings.edit();
            editor.putInt(APP_PREFERENCES_CHALLENGE_PROGRESS, progresum);
            editor.apply();
        }
        if (mSettings.contains(APP_PREFERENCES_CHALLENGE_MAX)) {
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putInt(APP_PREFERENCES_CHALLENGE_MAX, maxarc);
            editor.apply();
        }
        EventBus.getDefault().unregister(this);
    }

    static class sendstat {

        JSONArray jsonArray;

        sendstat(JSONArray jsonArray) {
            this.jsonArray = jsonArray;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ChangeProgressEvent(StartActivity.ChangeProgressEvent event) {
        progress=event.progressmessage;
        progresum+=progress;
        arcProgress.setProgress(progresum);
        SharedPreferences.Editor editor=mSettings.edit();
        editor.putInt(APP_PREFERENCES_CHALLENGE_PROGRESS, progresum);
        editor.apply();
        if ((mSettings.contains(APP_PREFERENCES_CHALLENGE_MAX)&&(mSettings.contains(APP_PREFERENCES_CHALLENGE_PROGRESS)))) {
            arcProgress.setBottomText(mSettings.getInt(APP_PREFERENCES_CHALLENGE_PROGRESS, 0) + "/" + mSettings.getInt(APP_PREFERENCES_CHALLENGE_MAX, 0));
        }
        if ((mSettings.contains(APP_PREFERENCES_CHALLENGE_MAX)&&(!mSettings.contains(APP_PREFERENCES_CHALLENGE_PROGRESS)))) {
            arcProgress.setBottomText("0/"+mSettings.getInt(APP_PREFERENCES_CHALLENGE_MAX, 0));
        }
        if ((!mSettings.contains(APP_PREFERENCES_CHALLENGE_MAX)&&(mSettings.contains(APP_PREFERENCES_CHALLENGE_PROGRESS)))) {
            arcProgress.setBottomText(mSettings.getInt(APP_PREFERENCES_CHALLENGE_MAX, 0)+"/0");}
    }
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void ChangeMaxEvent(challenge_frag.ChangeMaxEvent event) {
        maxarc=event.maxmessage;
        SharedPreferences.Editor editor=mSettings.edit();
        editor.putInt(APP_PREFERENCES_CHALLENGE_MAX, maxarc);
        editor.apply();
        if ((mSettings.contains(APP_PREFERENCES_CHALLENGE_MAX)&&(mSettings.contains(APP_PREFERENCES_CHALLENGE_PROGRESS)))) {
            arcProgress.setBottomText(mSettings.getInt(APP_PREFERENCES_CHALLENGE_PROGRESS, 0) + "/" + mSettings.getInt(APP_PREFERENCES_CHALLENGE_MAX, 0));
        }
        if ((mSettings.contains(APP_PREFERENCES_CHALLENGE_MAX)&&(!mSettings.contains(APP_PREFERENCES_CHALLENGE_PROGRESS)))) {
            arcProgress.setBottomText("0/"+mSettings.getInt(APP_PREFERENCES_CHALLENGE_MAX, 0));
        }
        if ((!mSettings.contains(APP_PREFERENCES_CHALLENGE_MAX)&&(mSettings.contains(APP_PREFERENCES_CHALLENGE_PROGRESS)))) {
            arcProgress.setBottomText(mSettings.getInt(APP_PREFERENCES_CHALLENGE_MAX, 0)+"/100");}
    }
    private void setdata(com.github.mikephil.charting.charts.BarChart barChart){
        String[] dninedeli = {
                "пн","вт","ср","чт","пт","сб","вс"
        };
        List<BarEntry> entries = new ArrayList<>();
        barChart.setDrawGridBackground(false);
        entries.add(new BarEntry(0f, 150));
        entries.add(new BarEntry(1f, 700));
        entries.add(new BarEntry(2f, 389));
        entries.add(new BarEntry(3f, 2400));

        entries.add(new BarEntry(5f, 900));
        entries.add(new BarEntry(6f, 4200));
        BarDataSet set = new BarDataSet(entries, "BarDataSet");
        set.setValueFormatter(new MyValueFormatter());
        set.setValueTextSize(8);
        set.setHighlightEnabled(true);
        set.setHighLightColor(R.color.text_color);
        set.setDrawValues(false);


        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setValueFormatter(new MyXAxisValueFormatter(dninedeli));

        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setDrawAxisLine(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setEnabled(false);

        set.setColors(new int[]{R.color.colorPrimary,R.color.colorPrimary,R.color.colorPrimary,R.color.colorPrimary,R.color.colorPrimary,R.color.colorPrimary,R.color.colorPrimary},getActivity().getApplicationContext());

        barChart.setScaleEnabled(false);//zoom
        barChart.setDragEnabled(false);//hz
       // barChart.setHighlightPerTapEnabled(true);//click
        //barChart.setTouchEnabled(false);

        BarData data = new BarData(set);
        data.setBarWidth(0.5f);
        barChart.setData(data);
        barChart.setFitBars(true);
        barChart.invalidate();
        //barChart.setMaxVisibleValueCount(2);
    }
    private void setlegend(com.github.mikephil.charting.charts.BarChart barChart){
        Legend l = barChart.getLegend();
        l.setEnabled(false);
    }
}
