package com.example.stepsv2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyAdapter extends FragmentPagerAdapter {
    public MyAdapter(FragmentManager mgr) {
        super(mgr);
    }
    @Override
    public int getCount() {
        return(4);
    }
    @Override
    public Fragment getItem(int position) {
        return(daily.newInstance(position));
    }
}