package com.insuranceline.ui.claim;

import android.support.annotation.DrawableRes;

import com.insuranceline.R;
import com.insuranceline.data.DataManager;
import com.insuranceline.data.vo.DailySummary;
import com.insuranceline.data.vo.Goal;
import com.insuranceline.ui.base.BasePresenter;

import java.text.DecimalFormat;

import javax.inject.Inject;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Zeki Guler on 29,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class NextGoalPresenter extends BasePresenter<NextGoalMvpView>{

    private final DataManager mDataManager;

    @DrawableRes
    int[] cupIcons = {
            R.drawable.icon_goal1,
            R.drawable.icon_goal2,
            R.drawable.icon_goal3,
    };

    DecimalFormat formatter = new DecimalFormat("#,###,###");

    @Inject
    public NextGoalPresenter(DataManager dataManager) {

        mDataManager = dataManager;
    }

    @Override
    public void attachView(NextGoalMvpView mvpView) {
        super.attachView(mvpView);
    }


    public void updateView() {
        Goal idleGoal = mDataManager.getIdleGoal();

        if (idleGoal != null){
            int indx = (int)idleGoal.getGoalId();

            String nextTarget = String.format("New Goal - %s steps",formatter.format(mDataManager.getNextTarget(idleGoal.getGoalId())));

            boolean enableStartButton = idleGoal.getStatus() == Goal.GOAL_STATUS_IDLE;
            int cupResId = cupIcons[indx];

            getMvpView().updateNextGoal(nextTarget, enableStartButton, cupResId);
        } else {
            getMvpView().updateNextGoal("Well done. You achieved all goals.", false, R.drawable.icon_goal3);
            getMvpView().closeFragment();
        }
    }

    public void startNewGoal() {
        final Goal idlegoal = mDataManager.getIdleGoal();

        if (idlegoal != null){
            getMvpView().showProgress();
            mDataManager.calculateDailyBias()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Observer<DailySummary>() {
                        @Override
                        public void onCompleted() {
                            getMvpView().hideProgress();
                        }

                        @Override
                        public void onError(Throwable e) {
                            getMvpView().hideProgress();
                            Timber.d(e.getMessage());
                            getMvpView().onError(e.getMessage());
                        }

                        @Override
                        public void onNext(DailySummary dailySummary) {
                            getMvpView().hideProgress();
                            mDataManager.startNewGoal(idlegoal.getGoalId(),dailySummary.getDailySteps());
                            getMvpView().newActivityStarted();
                        }
                    });
        }else
        {
            getMvpView().closeFragment();
        }

    }
}
