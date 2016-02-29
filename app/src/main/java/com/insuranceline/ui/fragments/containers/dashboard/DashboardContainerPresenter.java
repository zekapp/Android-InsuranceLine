package com.insuranceline.ui.fragments.containers.dashboard;

import com.insuranceline.data.DataManager;
import com.insuranceline.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by Zeki Guler on 04,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class DashboardContainerPresenter extends BasePresenter<DashboardContainerMvpView> {

    private final DataManager mDataManager;

    @Inject
    public DashboardContainerPresenter(DataManager dataManager){

        mDataManager = dataManager;
    }

    @Override
    public void attachView(DashboardContainerMvpView mvpView) {
        super.attachView(mvpView);
    }

    public void fetchNextView() {
        boolean isSet = mDataManager.getActvGoal() != null;
        boolean isPermissionDone = mDataManager.isFitBitScopePermissionDone();
        boolean isAllGoalDone = mDataManager.isAllGoalDone();
        getMvpView().initView(isSet, isPermissionDone,isAllGoalDone);
    }
}
