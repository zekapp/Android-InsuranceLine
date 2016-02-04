package com.insuranceline.ui.fragments.rewards;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.insuranceline.R;
import com.insuranceline.ui.fragments.BaseFragment;

import butterknife.ButterKnife;

/**
 * Created by Zeki Guler on 04,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class ThirdRewardFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_third_reward, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle("Rewards");
    }

    public static ThirdRewardFragment getInstance() {
        return new ThirdRewardFragment();
    }
}
