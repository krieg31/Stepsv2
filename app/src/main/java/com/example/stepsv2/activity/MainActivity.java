package com.example.stepsv2.activity;
import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stepsv2.R;
import com.example.stepsv2.fragments.challenge_frag;
import com.example.stepsv2.fragments.main_frag;
import com.example.stepsv2.fragments.second_frag;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.example.stepsv2.challenge.adapter.FeedListAdapter;

import es.dmoral.toasty.Toasty;

public class MainActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomBar bottomBar = findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {

                Fragment fragment = null;
                switch (tabId) {
                    case R.id.home:
                        fragment = new main_frag();
                        break;
                    case R.id.tab_history:
                        fragment = new second_frag();
                        break;
                    case R.id.tab_challenge:
                        fragment = new challenge_frag();
                        break;
                    default:
                        fragment = new main_frag();
                        break;
                }
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
               // FragmentManager fragmentManager = getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
               // FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, fragment);
                fragmentTransaction.commit();
            }
        });

        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                Toast.makeText(MainActivity.this, "Re selected", Toast.LENGTH_SHORT).show();
            }
        });
    }


}