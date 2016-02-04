package com.insuranceline.ui.fragments.goals;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.insuranceline.R;
import com.insuranceline.ui.fragments.BaseFragment;

/**
 * Created by Zeki Guler on 04,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class ThirdGoalFragment  extends BaseFragment {
    public static ThirdGoalFragment getInstance(){
        return new ThirdGoalFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setTitle("Goals");
        final View view = inflater.inflate(R.layout.fragment_third_goals, container, false);
        return view;
    }
}
