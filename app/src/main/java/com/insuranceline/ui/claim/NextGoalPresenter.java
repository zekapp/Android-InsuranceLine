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


    String idle_goal1def = "Your next goal is %s steps. When you achieve %s steps you will receive a $30 New Balance Voucher to spend on the latest gear.";
    String idle_goal2def = "Your next goal is %s steps. When you achieve %s steps you will receive a $30 New Balance Voucher to spend on the latest gear.";
    String idle_goal3def = "Your next goal is %s steps. When you achieve %s steps you will receive a 3 month subscription to Good Health Magazine.";

    // dummy
    String idle_goal4def = "Your next goal is %s steps. When you achieve %s steps you will receive a 3 month subscription to Good Health Magazine.";

    @DrawableRes
    int[] cupIcons = {
            R.drawable.icon_goal1,
            R.drawable.icon_goal2,
            R.drawable.icon_goal3,
    };

    String[] goalInfoActive = {
            idle_goal1def,
            idle_goal2def,
            idle_goal3def,
            idle_goal4def
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

            if((indx+1) <= goalInfoActive.length){

                String nxtTarget = formatter.format(mDataManager.getNextTarget(idleGoal.getGoalId()));
                String nextTargetDef = String.format(goalInfoActive[indx+1],nxtTarget,nxtTarget);

                boolean enableStartButton = idleGoal.getStatus() == Goal.GOAL_STATUS_IDLE;
                int cupResId = cupIcons[indx];
                getMvpView().updateNextGoal(nextTargetDef, enableStartButton, cupResId);
            }

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
