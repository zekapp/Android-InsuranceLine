package com.insuranceline.ui.fragments.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.progresviews.ProgressLine;
import com.app.progresviews.ProgressWheel;
import com.insuranceline.R;
import com.insuranceline.ui.fragments.BaseFragment;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Zeki Guler on 03,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class DashboardFragment extends BaseFragment implements DashboardMvpView {

    @Bind(R.id.wheel_progress) ProgressWheel mWheelProgress;
    @Bind(R.id.steps_line_progress) ProgressLine mStepsProgressLine;
    @Bind(R.id.calories_line_progress) ProgressLine mCalProgressLine;
    @Bind(R.id.active_minutes_line_progress) ProgressLine mActiveMinProgressLine;
    @Bind(R.id.daily_distance_line_progress) ProgressLine mDailyDistProgressLine;
    @Bind(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;

    @Inject DashboardPresenter mDashboardPresenter;

    public static DashboardFragment getInstance(){
        return new DashboardFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setTitle("Dashboard");
        final View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        mDashboardPresenter.attachView(this);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mDashboardPresenter.fetch();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDashboardPresenter.detachView();
    }

    private void trySetupSwipeRefresh() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setColorSchemeResources(
                    R.color.refresh_progress_1,
                    R.color.refresh_progress_2,
                    R.color.refresh_progress_3,
                    R.color.refresh_progress_4);


/*            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mDashboardPresenter.fetch();
                }
            });*/
        }
    }

    /******* MVP View Methods Implementation*/

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {
//        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void error(String error) {
        Toast.makeText(mContext," " + error,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNoActiveGoal(String successMessage) {
        Toast.makeText(mContext,successMessage,Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateWheelProgress(int totalDegreeDone, String totalPercentageDone, String totalStepsCountDone) {
        mWheelProgress.setPercentage(totalDegreeDone);
        mWheelProgress.setStepCountText(totalPercentageDone);
        mWheelProgress.setDefText(totalStepsCountDone);
    }

    @Override
    public void updateDailySteps(int dailyPercentageDone, int dailyStepsCountDone) {
        mStepsProgressLine.setmPercentage(dailyPercentageDone);
        mStepsProgressLine.setmValueText(dailyStepsCountDone);
    }

    @Override
    public void updateDailyCalories(int dailyPercentageDone, int dailyCaloriesDone) {
        mCalProgressLine.setmPercentage(dailyPercentageDone);
        mCalProgressLine.setmValueText(dailyCaloriesDone);
    }

    @Override
    public void updateDailyActiveMinutes(int dailyPercentageDone, int dailyActiveMinDone) {
        mActiveMinProgressLine.setmPercentage(dailyPercentageDone);
        mActiveMinProgressLine.setmValueText(dailyActiveMinDone);
    }

    @Override
    public void updateDailyDistance(int dailyPercentageDone, float dailyDistanceDone) {
        mDailyDistProgressLine.setmPercentage(dailyPercentageDone);
        mDailyDistProgressLine.setmValueText(String.valueOf(dailyDistanceDone));
    }
}
