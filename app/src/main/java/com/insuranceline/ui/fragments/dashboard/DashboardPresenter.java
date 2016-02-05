package com.insuranceline.ui.fragments.dashboard;

import com.insuranceline.data.DataManager;
import com.insuranceline.data.vo.DailySummary;
import com.insuranceline.data.vo.Goal;
import com.insuranceline.ui.base.BasePresenter;
import com.insuranceline.ui.fragments.containers.dashboard.DashboardContainerMvpView;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Zeki Guler on 05,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class DashboardPresenter extends BasePresenter<DashboardMvpView>{

    private final DataManager mDataManager;

    public DashboardPresenter(DataManager dataManager){

        mDataManager = dataManager;

    }

    @Override
    public void attachView(DashboardMvpView mvpView) {
        super.attachView(mvpView);
    }


    public void fetch() {
        mDataManager.dailySummary()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<DailySummary>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getMvpView().error(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(DailySummary dailySummary) {

                    }
                });
    }
}
