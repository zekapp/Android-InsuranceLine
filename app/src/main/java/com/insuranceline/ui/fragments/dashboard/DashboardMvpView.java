package com.insuranceline.ui.fragments.dashboard;

import com.insuranceline.ui.base.MvpView;

/**
 * Created by Zeki Guler on 05,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public interface DashboardMvpView extends MvpView{

    void showProgress();

    void hideProgress();

    void error(String error);

    void onNoActiveGoal(String successMessage);

    /**
     * This fucntion updates the WheelProcess on Dashboard.
     *
     * @param totalDegreeDone: value should be between 0 - 360 degree.
     * @param totalPercentageDone: it is the text show how much the process done as percentage
     * @param totalStepsCountDone: it is the text shows ho much steps done
     * */
    void updateWheelProgress( int totalDegreeDone, String totalPercentageDone, String totalStepsCountDone);

    /**
     * @param dailyPercentageDone: value should be 0 and 100.
     * @param dailyStepsCountDone: value is the string ans shows daily statistic
     * */
    void updateDailySteps( int dailyPercentageDone, int dailyStepsCountDone);

    /**
     * @param dailyPercentageDone: value should be 0 and 100.
     * @param dailyCaloriesDone: value is the string ans shows daily statistic
     * */
    void updateDailyCalories(int dailyPercentageDone, int dailyCaloriesDone);

    /**
     * @param dailyPercentageDone: value should be 0 and 100.
     * @param dailyActiveMinDone: value is the string ans shows daily statistic
     * */
    void updateDailyActiveMinutes(int dailyPercentageDone, int dailyActiveMinDone);

    /**
     * @param dailyPercentageDone: value should be 0 and 100.
     * @param dailyDistanceDone: value is the string ans shows daily statistic
     * */
    void updateDailyDistance(int dailyPercentageDone, float dailyDistanceDone);

    void updateTarget(String target);
}
