package com.insuranceline.ui.fragments.rewards;

import com.insuranceline.R;
import com.insuranceline.data.DataManager;
import com.insuranceline.data.vo.DailySummary;
import com.insuranceline.data.vo.Goal;
import com.insuranceline.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by Zeki Guler on 12,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class RewardPresenter extends BasePresenter<RewardMvpView>{

    private DataManager mDataManager;


    // Idle definition
    String idle_goal1def = "Complete %s steps and you will be eligible to redeem a Village Cinema Movie Voucher worth $20*";
    String idle_goal2def = "Complete %s steps and you will be eligible to redeem a New Balance Voucher worth $30*";
    String idle_goal3def = "Complete %s steps and you will be eligible to redeem a Health Voucher worth $50*";

    // Idle/active/done definition
    String iad_goal1def = "Complete 100.000 steps and you will be eligible to redeem a Village Cinema Movie Voucher worth $20*";
    String iad_goal2def = "Complete 200.000 steps and you will be eligible to redeem a New Balance Voucher worth $30*";
    String iad_goal3def = "Complete 500.000 steps and you will be eligible to redeem a Health Voucher worth $50*";

    String[] goalInfoIdleActiveDone = {
            idle_goal1def,
            idle_goal2def,
            idle_goal3def
    };

    String[] goalInfoLock = {
            iad_goal1def,
            iad_goal2def,
            iad_goal3def,
    };

/*    @StringRes
    int[] goalInfoIdleActiveDone = {
            R.string.first_goal_challenge_text,
            R.string.second_goal_challenge_text,
            R.string.third_goal_challenge_text,
    };*/

//    @StringRes
//    int[] goalInfoLock = {
//            R.string.first_goal_challenge_text,
//            R.string.unlocked_second_reward_message,
//            R.string.unlocked_third_reward_message,
//    };

    @Inject
    public RewardPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(RewardMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    public void updateViewAccordingToGoalIndex(long goalId){
        Goal goal = mDataManager.getGoalById(goalId);

        int buttonStatus = goal.getStatus();

        int indx = (int)goalId;

        String title =
                (buttonStatus == Goal.GOAL_STATUS_IDLE)    ? "Start" :
                (buttonStatus == Goal.GOAL_STATUS_ACTIVE)  ? "In Progress" :
                (buttonStatus == Goal.GOAL_STATUS_DONE)    ? "Done" : ""; // use background img

        int background =
                (buttonStatus == Goal.GOAL_STATUS_IDLE)    ? R.drawable.orange_button_bg :
                (buttonStatus == Goal.GOAL_STATUS_ACTIVE)  ? R.drawable.orange_button_bg :
                (buttonStatus == Goal.GOAL_STATUS_DONE)    ? R.drawable.gray_button_bg   :
                                                             R.drawable.btn_locked;

        String definition =
                (buttonStatus == Goal.GOAL_STATUS_IDLE)    ? String.format(goalInfoIdleActiveDone[indx], goal.getTarget()) :
                (buttonStatus == Goal.GOAL_STATUS_ACTIVE)  ? String.format(goalInfoIdleActiveDone[indx], goal.getTarget()) :
                (buttonStatus == Goal.GOAL_STATUS_DONE)    ? String.format(goalInfoIdleActiveDone[indx], goal.getTarget()) :
                                                             goalInfoLock[indx];


        getMvpView().updateButtonStatus(title, background, buttonStatus == Goal.GOAL_STATUS_IDLE);
        getMvpView().updateDefinition(definition);

    }


    public void startGoal(final long goaldId) {
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
                        mDataManager.startNewGoal(goaldId,dailySummary.getDailySteps());
                        updateViewAccordingToGoalIndex(goaldId);
                    }
                });

    }
}
