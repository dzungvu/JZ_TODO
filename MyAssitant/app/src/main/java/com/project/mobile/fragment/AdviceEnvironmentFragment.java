package com.project.mobile.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.mobile.myassitant.R;

/**
 * Use to
 * Created by DzungVu on 8/16/2017.
 */

public class AdviceEnvironmentFragment  extends Fragment{
    public static AdviceEnvironmentFragment newInstance(){
        return new AdviceEnvironmentFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_advice_environment, container, false);
        return view;
    }
}
