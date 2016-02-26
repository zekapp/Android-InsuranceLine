package com.insuranceline.ui.main;

import com.insuranceline.data.DataManager;
import com.insuranceline.data.remote.model.DashboardModel;
import com.insuranceline.data.remote.responses.EdgeAuthResponse;
import com.insuranceline.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Zeki Guler on 10,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class MainActivityPresenter extends BasePresenter<MainActivityMvpView> {

    private final DataManager mDataManager;
    private Subscription mSubscription;

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
        stopFetchingData();
    }

    public void getFirstTabIndex() {
        getMvpView().changeTab(mDataManager.isFirstLaunch() ? MainActivity.GOAL_CONTAINER_INDEX : MainActivity.DASHBOARD_CONTAINER_INDEX);
    }

    public void setNextOpenReminderAlarm() {
        mDataManager.setNextReminderNotification();
    }

    public void setFitBitDisconnected() {
        mDataManager.setFitBitDisconnect();
    }

    public void deleteEdgeUser() {
        mDataManager.deleteEdgeUser();
    }

    public void validateTheEdgeUser() {
        mDataManager.getEdgeToken()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<EdgeAuthResponse>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("validateTheEdgeUser completed()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.d("validateTheEdgeUser error(%s)", e.getMessage());
                    }

                    @Override
                    public void onNext(EdgeAuthResponse edgeAuthResponse) {
                        Timber.d("validateTheEdgeUser onNext newToken: %s",
                                edgeAuthResponse.getmAccessToken());
                    }
                });
    }

    public void subscribeFetchingData() {
        if (!mDataManager.isAnyGoalActive()) return;

        mSubscription = mDataManager.getDashboardModel()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DashboardModel>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("subscribeFetchingData ednded");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e("subscribeFetchingData error: %s", e.getMessage() );
                    }

                    @Override
                    public void onNext(DashboardModel dashboardModel) {
                        Timber.d("subscribeFetchingData: dashboardModel here" );
                    }
                });
    }

    public void stopFetchingData() {
        try {
            if (mSubscription != null)
                mSubscription.unsubscribe();
        } catch (Exception e) {
            Timber.e(e.getMessage());
        }

    }
}
