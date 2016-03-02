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
    String idle_goal2def = "Complete %s steps and you will be eligible to redeem a $30 New Balance Voucher to spend on the latest gear.";
    String idle_goal3def = "Complete %s steps and you will be eligible to redeem a 3 month subscription to Good Health Magazine";

    // lock definition
    String iad_goal1def_lock = "Complete your initial goal and you will unlock the first reward. Village Cinema Movie Voucher worth $20*";
    String iad_goal2def_lock = "Complete your first goal and you will unlock the second reward. A $30 New Balance Voucher to spend on the latest gear.";
    String iad_goal3def_lock = "Complete your second goal and you will unlock the third reward. 3 month subscription to Good Health Magazine";

    // Active definition
    String iad_goal1def_active = "Complete your first goal and you will unlock your first reward. A Village Cinema Movie Voucher worth $20*.";
    String iad_goal2def_active = "Complete your second goal and you will unlock your second reward. A $30 New Balance Voucher to spend on the latest gear.";
    String iad_goal3def_active = "Complete your third goal and you will unlock your third reward. A 3 month subscription to Good Health Magazine.";

    String[] goalInfoIdleAndDone = {
            idle_goal1def,
            idle_goal2def,
            idle_goal3def
    };

    String[] goalInfoLock = {
            iad_goal1def_lock,
            iad_goal2def_lock,
            iad_goal3def_lock,
    };

    String[] goalInfoActive = {
            iad_goal1def_active,
            iad_goal2def_active,
            iad_goal3def_active
    };

/*    @StringRes
    int[] goalInfoIdleAndDone = {
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
                (buttonStatus == Goal.GOAL_STATUS_ACTIVE) || (buttonStatus == Goal.GOAL_STATUS_CLAIMING)  ? "In Progress" :
                (buttonStatus == Goal.GOAL_STATUS_DONE)    ? "Done" : ""; // use background img

        int background =
                (buttonStatus == Goal.GOAL_STATUS_IDLE)    ? R.drawable.orange_button_bg :
                (buttonStatus == Goal.GOAL_STATUS_ACTIVE) || (buttonStatus == Goal.GOAL_STATUS_CLAIMING) ? R.drawable.orange_button_bg :
                (buttonStatus == Goal.GOAL_STATUS_DONE)    ? R.drawable.gray_button_bg   :
                                                             R.drawable.btn_locked;

        String definition =
                (buttonStatus == Goal.GOAL_STATUS_IDLE)    ? String.format(goalInfoIdleAndDone[indx], goal.getTarget()) :
                (buttonStatus == Goal.GOAL_STATUS_ACTIVE) || (buttonStatus == Goal.GOAL_STATUS_CLAIMING) ? goalInfoActive[indx] :
                (buttonStatus == Goal.GOAL_STATUS_DONE)    ? String.format(goalInfoIdleAndDone[indx], goal.getTarget()) :
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
