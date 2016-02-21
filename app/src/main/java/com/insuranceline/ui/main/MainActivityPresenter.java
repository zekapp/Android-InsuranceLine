package com.insuranceline.ui.main;

import com.insuranceline.data.DataManager;
import com.insuranceline.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by Zeki Guler on 10,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class MainActivityPresenter extends BasePresenter<MainActivityMvpView> {

    private final DataManager mDataManager;

    @Inject
    public MainActivityPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(MainActivityMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    public void getFirstTabIndex(){
        getMvpView().changeTab(mDataManager.isFirstLaunch()? MainActivity.GOAL_CONTAINER_INDEX :MainActivity.DASHBOARD_CONTAINER_INDEX);
    }

    public void setNextOpenReminderAlarm() {
        mDataManager.setNextReminderNotification();
    }
}
