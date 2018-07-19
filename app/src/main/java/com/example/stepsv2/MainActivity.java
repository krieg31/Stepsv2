package com.example.stepsv2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.stepsv2.fragments.main_frag;
import com.example.stepsv2.fragments.second_frag;
import com.example.stepsv2.fragments.settings_frag;
import com.example.stepsv2.fragments.third_frag;

public class MainActivity extends FragmentActivity {


    main_frag main_frag = new main_frag();
    second_frag second_frag = new second_frag();
    third_frag third_frag = new third_frag();
    settings_frag settings_frag = new settings_frag();


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frgmCont, main_frag).commit();
                    break;
                case R.id.navigation_second:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frgmCont, second_frag).commit();
                    break;
                case R.id.navigation_third:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frgmCont, third_frag).commit();
                    break;
                case R.id.navigation_settings:
                    getSupportFragmentManager().beginTransaction().replace(R.id.frgmCont, settings_frag).commit();
                    break;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction().replace(R.id.frgmCont, main_frag).commit();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
