package com.insuranceline.ui.fragments.dashboard;

import com.insuranceline.data.DataManager;
import com.insuranceline.data.vo.DailySummary;
import com.insuranceline.data.vo.Goal;
import com.insuranceline.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Zeki Guler on 05,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class DashboardPresenter extends BasePresenter<DashboardMvpView>{


    private final DataManager mDataManager;
    private Subscription mSubscription;

    @Inject
    public DashboardPresenter(DataManager dataManager){

        mDataManager = dataManager;

    }

    @Override
    public void attachView(DashboardMvpView mvpView) {
        super.attachView(mvpView);
        if (mSubscription != null) mSubscription.unsubscribe();
    }


    public void fetch() {


        //working example
        mDataManager.getDailySummary()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DailySummary>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e("onError(%s)",e.getMessage());
                    }

                    @Override
                    public void onNext(DailySummary dailySummary) {
                        Timber.d("onNext(): Cal:%s", dailySummary.getDailyCalories());
                        presentData(dailySummary);
                    }
                });


    }

    private void presentData(DailySummary dailySummary) {
        Goal goal = mDataManager.getActiveGoal();
        if (goal == null) {
            getMvpView().onNoAciveGoal("There is no active goal");
        } else {
            Timber.d("incoming goal: %s", goal.getGoalId());

            // Active Minute Update
            getMvpView().updateDailyActiveMinutes(
                    dailyPerDone(goal.getRequiredDailyActiveMin(), dailySummary.getDailyActiveMinutes()),
                    dailySummary.getDailyActiveMinutes());

            // Calorie Update
            getMvpView().updateDailyCalories(
                    dailyPerDone(goal.getRequiredDailyCalorie(), dailySummary.getDailyCalories()),
                    dailySummary.getDailyCalories());

            // Steps Update
            getMvpView().updateDailySteps(
                    dailyPerDone(goal.getRequiredDailySteps(), dailySummary.getDailySteps()),
                    dailySummary.getDailySteps());

            // Steps Update
            getMvpView().updateDailyDistance(
                    dailyPerDone(goal.getRequiredDailyDistance(), dailySummary.getDailyDistance()),
                    dailySummary.getDailyDistance());

        }
    }

    // return int between 0 and 100
    private int dailyPerDone(int required, float done) {
        return (int)(done * 100) / required;
    }
}

/*        Observable.interval(10, TimeUnit.SECONDS)
                .flatMap(new Func1<Long, Observable<DailySummary>>() {
                    @Override
                    public Observable<DailySummary> call(Long aLong) {
                        return mDataManager.getDailySummary();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<DailySummary>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("fetch onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e.getMessage());
                        getMvpView().error(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(DailySummary dailySummary) {
                        Timber.d("onNext(): Cal:%s", dailySummary.getDailyCalories());
                        presentData(dailySummary);
                    }
                });*/


/*
        //working example
mDataManager.getDailySummary()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DailySummary>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.d("onError(%s)",e.getMessage());
                    }

                    @Override
                    public void onNext(DailySummary dailySummary) {
                        Timber.d("onNext(): Cal:%s", dailySummary.getDailyCalories());
                        presentData(dailySummary);
                    }
                });*/


/*        mDataManager.getDailySummaryFromDb()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<DailySummary>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.d("onError(%s)",e.getMessage());
                    }

                    @Override
                    public void onNext(DailySummary dailySummary) {
                        Timber.d("onNext(): Cal:%s", dailySummary.getDailyCalories());
                    }
                });*/


/*        mDataManager.getDailySummaryFromApiWithSave()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<DailySummary>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.d("onError(%s)",e.getMessage());
                    }

                    @Override
                    public void onNext(DailySummary dailySummary) {
                        Timber.d("onNext(): Cal:%s", dailySummary.getDailyCalories());
                    }
                });*/
