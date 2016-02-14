package com.insuranceline.ui.fragments.dashboard;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.progresviews.ProgressLine;
import com.app.progresviews.ProgressWheel;
import com.insuranceline.R;
import com.insuranceline.ui.fragments.BaseFragment;
import com.insuranceline.utils.DialogFactory;

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
    @Bind(R.id.target) TextView mTargetTextView;

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
        ButterKnife.bind(this, view);
        mDashboardPresenter.attachView(this);
        mDashboardPresenter.updateView();
        mDashboardPresenter.fetch();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();
        setHasOptionsMenu(true);
        /*mDashboardPresenter.fetch();*/
    }

    @Override
    public void onPause() {
        super.onPause();
        mDashboardPresenter.stopFetchingData();
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

    @Override
    public void updateTarget(String target) {
        mTargetTextView.setText(target);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_reset) {
            openTestDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openTestDialog() {
        final EditText input = new EditText(getActivity());
        input.setHeight(100);
        input.setWidth(140);
        input.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setGravity(Gravity.LEFT);
        input.setImeOptions(EditorInfo.IME_ACTION_DONE);


        DialogFactory.createDialogWithEditText
                (getActivity(), input, "Test Alert", "Set test Goal.",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String target = input.getText().toString();
                                try{
                                    mDashboardPresenter.resetGoal(Integer.valueOf(target));
                                }catch (Exception e){
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                .show();
    }
}
