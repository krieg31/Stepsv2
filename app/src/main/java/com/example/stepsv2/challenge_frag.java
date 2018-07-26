package com.example.stepsv2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class challenge_frag extends Fragment {

    Button hotChal;
    Button chal1;
    Button chal2;
    Button chal3;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenge_frag, container, false);
        registerButtons(view);
        return view;
    }

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            switch (v.getId()) {
                case R.id.hotChal:
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Challenge")
                            .setMessage("Ti uveren?")
                            .setIcon(R.drawable.ic_home_black_24dp)
                            .setCancelable(false)
                            .setNegativeButton("Challenge Accepted",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Toasty.success(getActivity(), "Success!", Toast.LENGTH_SHORT, true).show();
                                        }
                                    })
                            .setPositiveButton("Slabak",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                             Toasty.error(getActivity(), "Cancel", Toast.LENGTH_SHORT, true).show();
                                        }
                                     });
                    AlertDialog alert = builder.create();
                    alert.show();
                    break;
                case R.id.chal1:
                    Toasty.success(getActivity(), "Success!", Toast.LENGTH_SHORT, true).show();
                    break;
                case R.id.chal2:
                    Toasty.success(getActivity(), "Success!", Toast.LENGTH_SHORT, true).show();
                    break;
                case R.id.chal3:
                    Toasty.success(getActivity(), "Success!", Toast.LENGTH_SHORT, true).show();
                    break;
            }
        }
    };
    public void registerButtons(View view){
        register(R.id.hotChal, view);
        register(R.id.chal1,view);
        register(R.id.chal2,view);
        register(R.id.chal3,view);
    }
    void register(int buttonResourceId,View view){
        view.findViewById(buttonResourceId).setOnClickListener(buttonClickListener);
    }

/*
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hotChal:
                break;
            case R.id.chal1:
                break;
            case R.id.chal2:
                break;
            case R.id.chal3:
                break;
        }
    }*/
}
