package com.example.stepsv2;

import android.app.Fragment;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

//import android.support.v4.app.Fragment;
public class main_frag extends Fragment {

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


    }
}