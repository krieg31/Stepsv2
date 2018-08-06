package com.example.stepsv2;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.github.lzyzsd.circleprogress.ArcProgress;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class main_frag extends Fragment {

    public Button start;
    public Button profile;
    ArcProgress arcProgress;
    int progress, maxarc ,progresum,todaysum;


    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_CHALLENGE_PROGRESS = "cprogress";
    public static final String APP_PREFERENCES_CHALLENGE_MAX = "cmax";
    public static final String APP_GRAPH= "graph";
    public static final String APP_DATE= "date";
    public static final int APP_TODAYRUN = 0;
    private SharedPreferences mSettings;

    Calendar calendar = Calendar.getInstance();
    String date= new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(new Date());
    JSONArray jsonArray =new JSONArray();

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
        FrameLayout frameLayout= view.findViewById(R.id.frameLayout2);
        frameLayout.setBackgroundResource(R.drawable.box_src);
        BarChart barChart= view.findViewById(R.id.graph);
        setgraph(barChart);

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

    public static class sendstat {

        JSONArray jsonArray;

        public sendstat(JSONArray jsonArray) {
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
    }
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void ChangeMaxEvent(challenge_frag.ChangeMaxEvent event) {
        maxarc=event.maxmessage;
        SharedPreferences.Editor editor=mSettings.edit();;
        editor.putInt(APP_PREFERENCES_CHALLENGE_MAX, maxarc);;
        editor.apply();
    }
    public void setgraph(BarChart mBarChart){
        mBarChart.addBar(new BarModel(2.3f, Color.parseColor("#e0ae4b")));
        mBarChart.addBar(new BarModel(2.f,  Color.parseColor("#e0ae4b")));
        mBarChart.addBar(new BarModel(3.3f, Color.parseColor("#e0ae4b")));
        mBarChart.addBar(new BarModel(1.1f, Color.parseColor("#e0ae4b")));
        mBarChart.addBar(new BarModel(2.7f, Color.parseColor("#e0ae4b")));
        mBarChart.addBar(new BarModel(2.f,  Color.parseColor("#e0ae4b")));
        mBarChart.addBar(new BarModel(0.4f, Color.parseColor("#e0ae4b")));
    }
}
