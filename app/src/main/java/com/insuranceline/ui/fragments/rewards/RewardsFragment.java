package com.insuranceline.ui.fragments.rewards;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.insuranceline.R;
import com.insuranceline.ui.fragments.BaseFragment;

/**
 * Created by Zeki Guler on 03,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class RewardsFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setTitle("Rewards");
        final View view = inflater.inflate(R.layout.fragment_reward, container, false);
        return view;
    }

}
