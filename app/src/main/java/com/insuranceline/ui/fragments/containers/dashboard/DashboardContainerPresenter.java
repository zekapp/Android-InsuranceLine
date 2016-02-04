package com.insuranceline.ui.fragments.containers.dashboard;

import com.insuranceline.data.DataManager;
import com.insuranceline.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by Zeki Guler on 04,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class DashboardContainerPresenter extends BasePresenter<DashboardMvpView> {

    private final DataManager mDataManager;

    @Inject
    public DashboardContainerPresenter(DataManager dataManager){

        mDataManager = dataManager;
    }

    @Override
    public void attachView(DashboardMvpView mvpView) {
        super.attachView(mvpView);
    }

    public void fetchNextView() {
        boolean isSet = mDataManager.isAnyGoalSet();
        getMvpView().initView(isSet);
    }
}
