package com.insuranceline.ui.fragments.dashboard;

import com.insuranceline.data.DataManager;
import com.insuranceline.data.remote.model.DashboardModel;
import com.insuranceline.data.vo.DailySummary;
import com.insuranceline.data.vo.Goal;
import com.insuranceline.ui.base.BasePresenter;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Zeki Guler on 05,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class DashboardPresenter extends BasePresenter<DashboardMvpView>{


    private final DataManager mDataManager;
    private Subscription mSubscription;
    private DecimalFormat mformatter = new DecimalFormat("#,###,###");
    @Inject
    public DashboardPresenter(DataManager dataManager){

        mDataManager = dataManager;

    }

    @Override
    public void attachView(DashboardMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        Timber.d("Dashboard MvpView detachView()");
        stopFetchingData();
    }

    public void fetch() {
        mSubscription = mDataManager.getDashboardModel()
/*                .repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
                    @Override
                    public Observable<?> call(Observable<? extends Void> observable) {
                        return observable.delay(1, TimeUnit.MINUTES);
                    }
                })*/
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DashboardModel>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("onCompleted()");
                        if (getMvpView() != null) getMvpView().hideProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e("onError(%s)", e.getMessage());
                    }

                    @Override
                    public void onNext(DashboardModel dashboardModel) {
                        Timber.d("onNext(): Cal:%s", dashboardModel.getmDailySummary().getDailyCalories());
                        if (getMvpView() != null)
                            presentData(dashboardModel);
                    }
                });


    }
    private void presentData(DashboardModel dashboardModel) {
        // Active Minute Update
        Goal activeGoal = dashboardModel.getActiveGoal();
        DailySummary dailySummary = dashboardModel.getmDailySummary();

        getMvpView().updateDailyActiveMinutes(
                dailyPerDone(activeGoal.getRequiredDailyActiveMin(), dailySummary.getDailyActiveMinutes()),
                dailySummary.getDailyActiveMinutes());

        // Calorie Update
        getMvpView().updateDailyCalories(
                dailyPerDone(activeGoal.getRequiredDailyCalorie(), dailySummary.getDailyCalories()),
                dailySummary.getDailyCalories());

        // Steps Update
        getMvpView().updateDailySteps(
                dailyPerDone((int)activeGoal.getRequiredDailySteps(), dailySummary.getDailySteps()),
                dailySummary.getDailySteps());

        // Steps Update
        getMvpView().updateDailyDistance(
                dailyPerDone(activeGoal.getRequiredDailyDistance(), dailySummary.getDailyDistance()),
                dailySummary.getDailyDistance());

        getMvpView().updateWheelProgress(
                calculateDegree((int)activeGoal.getTarget(), (int)activeGoal.getAchievedSteps()),
                calculatePercentage((int)activeGoal.getTarget(), (int)activeGoal.getAchievedSteps()),
                mformatter.format(activeGoal.getAchievedSteps()) +" steps");

    }

    private String calculatePercentage(int target, int achieved) {
        String per = String.valueOf((100 * achieved) / target);
        return per + "%";
    }

    //return 0 - 360
    private int calculateDegree(int target, int achieved) {
        if (target > 0)
            return (360 * achieved) / target;
        else
            return 0;
    }

    // return int between 0 and 100
    private int dailyPerDone(int required, float done) {
        return (int)(done * 100) / required;
    }

    public void resetGoal(int target) {
        mDataManager.resetGoals(target);
    }

    public void stopFetchingData() {
        try{
            if (mSubscription != null)
                mSubscription.unsubscribe();
        }catch (Exception e){
            Timber.e(e.getMessage());
        }

    }


    //        //working example
//        mDataManager.getDailySummary()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<DailySummary>() {
//                    @Override
//                    public void onCompleted() {
//                        Timber.d("onCompleted()");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Timber.e("onError(%s)",e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(DailySummary dailySummary) {
//                        Timber.d("onNext(): Cal:%s", dailySummary.getDailyCalories());
//                        presentData(dailySummary);
//                    }
//                });

/*    private void presentData(DailySummary dailySummary) {
        Goal goal = mDataManager.getActiveGoal();
        if (goal == null) {
            getMvpView().onNoActiveGoal("There is no active goal");
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
    }*/
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
