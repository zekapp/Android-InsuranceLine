package com.insuranceline.ui.fragments.goals;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.insuranceline.R;
import com.insuranceline.data.DataManager;
import com.insuranceline.data.vo.Goal;
import com.insuranceline.ui.base.BasePresenter;

import javax.inject.Inject;

import rx.Subscription;
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

    @StringRes int[] goalInfo = {
            R.string.first_goal_challenge_text,
            R.string.second_goal_challenge_text,
            R.string.third_goal_challenge_text,
    };

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

    private Goal mGoal;

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
        mGoal = mDataManager.getRelevantGoal();
        int index = (int) mGoal.getGoalId();
        Timber.d("Index of goal: %s", index);
        getMvpView().updateCupImg(cupIcons[index]);
        getMvpView().updateGoalDef(goalInfo[index]);
        getMvpView().updateGoalTitle(goalTitle[index]);

        int status =  mGoal.getStatus();
        int tiIndx = status ==  Goal.GOAL_STATUS_IDLE ? 0 : status == Goal.GOAL_STATUS_ACTIVE ? 1 : 2;

        getMvpView().updateButtonTitleAndStatus(buttonStatus[tiIndx], mGoal.getStatus() == Goal.GOAL_STATUS_IDLE);
    }

    public void activityStarted() {
        mDataManager.startNewGoal(mGoal.getGoalId());
        updateView();
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