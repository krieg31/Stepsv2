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

public class main_frag extends Fragment{

    private AnimationDrawable mAnimationDrawable;
    public Button start;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        getActivity().setContentView(R.layout.fragment_main_frag);
        View view = inflater.inflate(R.layout.fragment_main_frag,container, false);
        ImageView imageView = getActivity().findViewById(R.id.imageView);
        imageView.setBackgroundResource(R.drawable.main_kotik);
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
        mAnimationDrawable = (AnimationDrawable) imageView.getBackground();
        mAnimationDrawable.start();
        return view;
}
}
