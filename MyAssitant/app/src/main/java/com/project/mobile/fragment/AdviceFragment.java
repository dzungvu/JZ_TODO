package com.project.mobile.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.mobile.adapter.AdvicePagerAdapter;
import com.project.mobile.myassitant.R;

/**
 * Use to
 * Created by DzungVu on 8/8/2017.
 */

public class AdviceFragment extends Fragment {
    public static AdviceFragment newInstance() {
        return new AdviceFragment();
    }

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private AdvicePagerAdapter advicePagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_advice, container, false);

        viewPager = view.findViewById(R.id.vp_advice);
        tabLayout = view.findViewById(R.id.tl_advice);
        advicePagerAdapter = new AdvicePagerAdapter(getFragmentManager());
        viewPager.setAdapter(advicePagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}
