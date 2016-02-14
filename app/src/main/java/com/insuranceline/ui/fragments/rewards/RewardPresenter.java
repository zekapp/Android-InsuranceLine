package com.insuranceline.ui.fragments.rewards;

import android.support.annotation.StringRes;

import com.insuranceline.R;
import com.insuranceline.data.DataManager;
import com.insuranceline.data.vo.Goal;
import com.insuranceline.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by Zeki Guler on 12,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class RewardPresenter extends BasePresenter<RewardMvpView>{

    private DataManager mDataManager;

    @StringRes
    int[] goalInfoIdleActiveDone = {
            R.string.first_goal_challenge_text,
            R.string.unlocked_second_reward_message,
            R.string.unlocked_third_reward_message,
    };

    @StringRes
    int[] goalInfoLock = {
            R.string.first_goal_challenge_text,
            R.string.unlocked_second_reward_message,
            R.string.unlocked_third_reward_message,
    };

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

        int definition =
                (buttonStatus == Goal.GOAL_STATUS_IDLE)    ? goalInfoIdleActiveDone[indx] :
                (buttonStatus == Goal.GOAL_STATUS_ACTIVE)  ? goalInfoIdleActiveDone[indx] :
                (buttonStatus == Goal.GOAL_STATUS_DONE)    ? goalInfoIdleActiveDone[indx] :
                                                             goalInfoLock[indx];


        getMvpView().updateButtonStatus(title, background, buttonStatus == Goal.GOAL_STATUS_IDLE);
        getMvpView().updateDefinition(definition);

    }


    public void startGoal(long goaldId) {
        mDataManager.startNewGoal(goaldId);
        updateViewAccordingToGoalIndex(goaldId);
    }
}
