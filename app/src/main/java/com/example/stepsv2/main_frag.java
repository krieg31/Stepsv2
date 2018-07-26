package com.example.stepsv2;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;

public class main_frag extends Fragment {

    public Button start;
    ArcProgress  arcProgress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_frag, container, false);

        ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setBackgroundResource(R.drawable.main_kotik);
        start = view.findViewById(R.id.start);
        arcProgress = view.findViewById(R.id.arc_progress);

        AnimationDrawable mAnimationDrawable = (AnimationDrawable) imageView.getBackground();
        mAnimationDrawable.start();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getActivity(), StartActivity.class);
                getActivity().startActivity(myIntent);
            }
        });
        return view;
    }
}
