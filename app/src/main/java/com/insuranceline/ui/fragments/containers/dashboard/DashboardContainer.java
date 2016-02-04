package com.insuranceline.ui.fragments.containers.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.insuranceline.R;
import com.insuranceline.ui.fragments.containers.BaseContainerFragment;
import com.insuranceline.ui.fragments.dashboard.DashboardEmptyFragment;
import com.insuranceline.ui.fragments.dashboard.DashboardFragment;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Zeki Guler on 20,January,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class DashboardContainer extends BaseContainerFragment implements DashboardMvpView{
    public static final String TAG = DashboardContainer.class.getSimpleName();

    private boolean mIsViewInitiated;

    @Inject
    DashboardContainerPresenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("onCreated");
        getActivityComponent().inject(this);
        mPresenter.attachView(this);
    }

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
            mPresenter.fetchNextView();
        }
    }

    @Override
    public void initView(boolean isAnyGoalSet) {
        Timber.d("%s init view", TAG);

        if (isAnyGoalSet) replaceFragment(DashboardFragment.getInstance(), false);
        else              replaceFragment(DashboardEmptyFragment.getInstance(), false);

    }

}
