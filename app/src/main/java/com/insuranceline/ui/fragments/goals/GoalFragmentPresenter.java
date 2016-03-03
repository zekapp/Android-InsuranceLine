package com.insuranceline.ui.fragments.goals;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.insuranceline.R;
import com.insuranceline.data.DataManager;
import com.insuranceline.data.vo.DailySummary;
import com.insuranceline.data.vo.Goal;
import com.insuranceline.ui.base.BasePresenter;

import java.text.DecimalFormat;

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
public class GoalFragmentPresenter extends BasePresenter<GoalFragmentMvpView>{

    private final DataManager mDataManager;

    @DrawableRes  int[] cupIcons = {
            R.drawable.icon_goal1,
            R.drawable.icon_goal2,
            R.drawable.icon_goal3,
    };

    // Idle definition
    String idle_goal1def = "Complete %s steps and you will be eligible to redeem a Village Cinema Movie Voucher worth $20*";
    String idle_goal2def = "Complete %s steps and you will be eligible to redeem a New Balance Voucher worth $30*";
    String idle_goal3def = "Complete %s steps and you will be eligible to redeem a Health Voucher worth $50*";

    String[] goalInfo = {
            idle_goal1def,
            idle_goal2def,
            idle_goal3def
    };

/*    @StringRes int[] goalInfo = {
            R.string.first_goal_challenge_text,
            R.string.second_goal_challenge_text,
            R.string.third_goal_challenge_text,
    };*/

    @StringRes int[] goalTitle = {
            R.string.first_goal,
            R.string.second_goal,
            R.string.third_goal,
    };

    @StringRes int[] buttonStatus = {
        R.string.btn_start_text,
        R.string.btn_progress_text,
        R.string.btn_done_text,
    };

    private Subscription mSubscription;

    DecimalFormat formatter = new DecimalFormat("#,###,###");

    @Inject
    public GoalFragmentPresenter(DataManager dataManager) {

        mDataManager = dataManager;
    }

    @Override
    public void attachView(GoalFragmentMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    public void updateView() {
        Goal goal  = mDataManager.getIdleGoal();

        // if there is no Idle then return current Active goal.
        if (goal == null)
            goal = mDataManager.getActvGoal();

        if (goal == null)
            goal = mDataManager.getLastGoal();


        int index = (int) goal.getGoalId();
        Timber.d("Index of goal: %s", index);
        getMvpView().updateCupImg(cupIcons[index]);
        getMvpView().updateGoalDef(String.format(goalInfo[index], formatter.format(goal.getTarget())) /*goalInfo[index]*/);
        getMvpView().updateGoalTitle(goalTitle[index]);

        int status =  goal.getStatus();
        int tiIndx = status ==  Goal.GOAL_STATUS_IDLE ? 0 :
                status == Goal.GOAL_STATUS_ACTIVE || status == Goal.GOAL_STATUS_CLAIMING ? 1 : 2;

        getMvpView().updateButtonTitleAndStatus(buttonStatus[tiIndx], goal.getStatus() == Goal.GOAL_STATUS_IDLE);

    }

    public void startActivity() {
        getMvpView().showProgress();
        final Goal goal  = mDataManager.getIdleGoal();

        if (goal != null){
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

                            mDataManager.startNewGoal(goal.getGoalId(),dailySummary.getDailySteps());
                            updateView();
                        }
                    });
        }

    }
}
/*        mSubscription = mDataManager.getGoalForGoalFragHost()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Goal>() {
                    @Override
                    public void onCompleted() {
                        Timber.e("onCompleted()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e("Error(%s)", e.getMessage());
                    }

                    @Override
                    public void onNext(Goal goal) {
                        int index = (int) goal.getGoalId();
                        Timber.d("Index of goal: %s", index);
                        mGoal = goal;

                        getMvpView().updateCupImg(cupIcons[index]);
                        getMvpView().updateGoalDef(goalInfo[index]);
                        getMvpView().updateGoalTitle(goalTitle[index]);
                        getMvpView().updateButtonTitleAndStatus(buttonStatus[index], goal.getStatus() == Goal.GOAL_STATUS_IDLE);
                    }
                });*/