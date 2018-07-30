package com.example.stepsv2;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.ArcProgress;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class main_frag extends Fragment {

    public Button start;
    ArcProgress arcProgress;
    int progress, maxarc ,progresum;


    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_CHALLENGE_PROGRESS = "cprogress";
    public static final String APP_PREFERENCES_CHALLENGE_MAX = "cmax";
    private SharedPreferences mSettings;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_frag, container, false);

        mSettings = this.getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setBackgroundResource(R.drawable.main_kotik);
        start = view.findViewById(R.id.start);
        arcProgress = view.findViewById(R.id.arc_progress);

        EventBus.getDefault().register(this);

        AnimationDrawable mAnimationDrawable = (AnimationDrawable) imageView.getBackground();
        mAnimationDrawable.start();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), StartActivity.class);
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

}
