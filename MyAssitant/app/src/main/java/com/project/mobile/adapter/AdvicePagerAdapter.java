package com.project.mobile.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.project.mobile.fragment.AdviceEnvironmentFragment;
import com.project.mobile.fragment.AdviceHealthFragment;
import com.project.mobile.fragment.AdvicePlaceFragment;
import com.project.mobile.fragment.AdviceWeatherFragment;
import com.project.mobile.myassitant.R;

/**
 * Use to
 * Created by DzungVu on 8/16/2017.
 */

public class AdvicePagerAdapter extends FragmentPagerAdapter {
    public AdvicePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Log.i("F--", "F1");
                return AdviceWeatherFragment.newInstance();
            case 1:
                Log.i("F--", "F2");
                return AdviceEnvironmentFragment.newInstance();
            case 2:
                Log.i("F--", "F3");
                return AdviceHealthFragment.newInstance();
            case 3:
                Log.i("F--", "F4");
                return AdvicePlaceFragment.newInstance();
            default:
                Log.i("F--", "F0");
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Weather";
            case 1:
                return "Environment";
            case 2:
                return "Health";
            case 3:
                return "Place";
            default:
                return null;
        }
    }
}
