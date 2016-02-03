package com.insuranceline.ui.fragments.containers;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.insuranceline.R;
import com.insuranceline.ui.fragments.dashboard.DashboardFragment;

import timber.log.Timber;

/**
 * Created by Zeki Guler on 20,January,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class DashboardContainer extends BaseContainerFragment{
    public static final String TAG = DashboardContainer.class.getSimpleName();

    private boolean mIsViewInitiated;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Timber.d("%s onCreateView", TAG);
        return inflater.inflate(R.layout.container_fragment, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Timber.d("%s container on activity created", TAG);
        if (!mIsViewInitiated) {
            mIsViewInitiated = true;
            initView();
        }
    }

    private void initView() {
        Timber.d("%s init view", TAG);

        replaceFragment(new DashboardFragment(), false);

    }
}
