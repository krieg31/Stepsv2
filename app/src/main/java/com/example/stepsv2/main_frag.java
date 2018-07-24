package com.example.stepsv2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
<<<<<<< HEAD
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
=======
import android.content.Intent;
>>>>>>> GPS
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
<<<<<<< HEAD
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stepsv2.R;

public class main_frag extends Fragment{

    private AnimationDrawable mAnimationDrawable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        getActivity().setContentView(R.layout.fragment_main_frag);

        ImageView imageView = getActivity().findViewById(R.id.imageView);
        imageView.setBackgroundResource(R.drawable.main_kotik);

        mAnimationDrawable = (AnimationDrawable) imageView.getBackground();
        mAnimationDrawable.start();
        return inflater.inflate(R.layout.fragment_main_frag, container, false);
=======
import android.widget.Button;


import com.example.stepsv2.R;

public class main_frag extends Fragment {

    public Button start;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_frag,container, false);
        Button button = view.findViewById(R.id.start);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent myIntent = new Intent(getActivity(), StartActivity.class);
                getActivity().startActivity(myIntent);
            }
        });
        return view;
>>>>>>> GPS
    }


}
