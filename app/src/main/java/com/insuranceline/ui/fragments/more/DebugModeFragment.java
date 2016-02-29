package com.insuranceline.ui.fragments.more;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.insuranceline.R;
import com.insuranceline.config.AppConfig;
import com.insuranceline.data.DataManager;
import com.insuranceline.data.local.PreferencesHelper;
import com.insuranceline.data.vo.Goal;
import com.insuranceline.ui.fragments.BaseFragment;
import com.insuranceline.utils.TimeUtils;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zeki on 21/02/2016.
 */
public class DebugModeFragment extends BaseFragment {

    @Inject
    DataManager dataManager;
    @Inject
    PreferencesHelper prefHelper;

    @Inject
    AppConfig mAppConfig;

    @Bind(R.id.keep_going_notification_period)
    EditText keepGoingNotificationPeriod;
    @Bind(R.id.reminder_notification_period)
    EditText reminderNotificationPeriod;
    @Bind(R.id.campaign_end_date)
    EditText campaignEndDate;
    @Bind(R.id.new_target)
    EditText newTarget;
    @Bind(R.id.g1_start_date)
    TextView mG1StartDate;
    @Bind(R.id.g1_end_date)
    TextView mG1EndDate;
    @Bind(R.id.g1_target)
    TextView mG1Target;
    @Bind(R.id.g1_time_passed)
    TextView mG1TimePassed;
    @Bind(R.id.g2_start_date)
    TextView mG2StartDate;
    @Bind(R.id.g2_end_date)
    TextView mG2EndDate;
    @Bind(R.id.g2_target)
    TextView mG2Target;
    @Bind(R.id.g2_time_passed)
    TextView mG2TimePassed;
    @Bind(R.id.g3_start_date)
    TextView mG3StartDate;
    @Bind(R.id.g3_end_date)
    TextView mG3EndDate;
    @Bind(R.id.g3_target)
    TextView mG3Target;
    @Bind(R.id.g3_time_passed)
    TextView mG3TimePassed;
    @Bind(R.id.end_of_campaign)
    TextView mEndOfCampaign;
    @Bind(R.id.g1_end_goal)
    Button mG1EndGoal;
    @Bind(R.id.g2_end_goal)
    Button mG2EndGoal;
    @Bind(R.id.g3_end_goal)
    Button mG3EndGoal;
    @Bind(R.id.g1_claim_goal)
    Button mG1ClaimGoal;
    @Bind(R.id.g2_claim_goal)
    Button mG2ClaimGoal;
    @Bind(R.id.g3_claim_goal)
    Button mG3ClaimGoal;

    public static DebugModeFragment getInstance() {
        return new DebugModeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_debug, container, false);
        ButterKnife.bind(this, view);
        getActivityComponent().inject(this);
        updateTestResult();
        return view;
    }

    private void updateTestResult() {
        mEndOfCampaign.setText(TimeUtils.convertReadableDate(mAppConfig.getEndOfCampaign(), TimeUtils.DATE_FORMAT_TYPE_1));
        updateTestReulst1(dataManager.getCatchedGoalList().get(0));
        updateTestReulst2(dataManager.getCatchedGoalList().get(1));
        updateTestReulst3(dataManager.getCatchedGoalList().get(2));
    }

    private void updateTestReulst3(Goal goal) {
        if (goal.getStatus() == Goal.GOAL_STATUS_LOCK || goal.getStatus() == Goal.GOAL_STATUS_IDLE) {
            mG3StartDate.setText("-");
            mG3EndDate.setText("-");
            mG3Target.setText("-");
            mG3TimePassed.setText("-");
            mG3EndGoal.setVisibility(View.GONE);
        } else if (goal.getStatus() == Goal.GOAL_STATUS_ACTIVE) {
            long timePassged = System.currentTimeMillis() - goal.getBaseDate();
            mG3StartDate.setText(TimeUtils.convertReadableDate(goal.getBaseDate(), TimeUtils.DATE_FORMAT_TYPE_2));
            mG3EndDate.setText("active");
            mG3Target.setText(String.valueOf(goal.getTarget()));
            mG3EndGoal.setVisibility(View.VISIBLE);
            mG1ClaimGoal.setVisibility(View.GONE);
            mG3TimePassed.setText(String.format("Day: %s Hour: %s Min: %s",
                    TimeUnit.MILLISECONDS.toDays(timePassged),
                    TimeUnit.MILLISECONDS.toHours(timePassged),
                    TimeUnit.MILLISECONDS.toMinutes(timePassged)));
        } else if (goal.getStatus() == Goal.GOAL_STATUS_CLAIMING) {
            long timePassged = System.currentTimeMillis() - goal.getBaseDate();
            mG3StartDate.setText(TimeUtils.convertReadableDate(goal.getBaseDate(), TimeUtils.DATE_FORMAT_TYPE_2));
            mG3EndDate.setText("active");
            mG3Target.setText(String.valueOf(goal.getTarget()));
            mG3EndGoal.setVisibility(View.GONE);
            mG3ClaimGoal.setVisibility(View.VISIBLE);
            mG3TimePassed.setText(String.format("Day: %s Hour: %s Min: %s",
                    TimeUnit.MILLISECONDS.toDays(timePassged),
                    TimeUnit.MILLISECONDS.toHours(timePassged),
                    TimeUnit.MILLISECONDS.toMinutes(timePassged)));
        } else {
            long timePassged = goal.getEndDate() - goal.getBaseDate();
            mG3StartDate.setText(TimeUtils.convertReadableDate(goal.getBaseDate(), TimeUtils.DATE_FORMAT_TYPE_2));
            mG3EndDate.setText(TimeUtils.convertReadableDate(goal.getEndDate(), TimeUtils.DATE_FORMAT_TYPE_2));
            mG3Target.setText(String.valueOf(goal.getTarget()));
            mG3EndGoal.setVisibility(View.GONE);
            mG3TimePassed.setText(String.format("Day: %s Hour: %s Min: %s",
                    TimeUnit.MILLISECONDS.toDays(timePassged),
                    TimeUnit.MILLISECONDS.toHours(timePassged),
                    TimeUnit.MILLISECONDS.toMinutes(timePassged)));
        }
    }

    private void updateTestReulst2(Goal goal) {
        if (goal.getStatus() == Goal.GOAL_STATUS_LOCK || goal.getStatus() == Goal.GOAL_STATUS_IDLE) {
            mG2StartDate.setText("-");
            mG2EndDate.setText("-");
            mG2Target.setText("-");
            mG2TimePassed.setText("-");
            mG2EndGoal.setVisibility(View.GONE);
        } else if (goal.getStatus() == Goal.GOAL_STATUS_ACTIVE) {
            long timePassged = System.currentTimeMillis() - goal.getBaseDate();
            mG2StartDate.setText(TimeUtils.convertReadableDate(goal.getBaseDate(), TimeUtils.DATE_FORMAT_TYPE_2));
            mG2EndDate.setText("active");
            mG2Target.setText(String.valueOf(goal.getTarget()));
            mG2EndGoal.setVisibility(View.VISIBLE);
            mG1ClaimGoal.setVisibility(View.GONE);
            mG2TimePassed.setText(String.format("Day: %s Hour: %s Min: %s",
                    TimeUnit.MILLISECONDS.toDays(timePassged),
                    TimeUnit.MILLISECONDS.toHours(timePassged),
                    TimeUnit.MILLISECONDS.toMinutes(timePassged)));
        } else if (goal.getStatus() == Goal.GOAL_STATUS_CLAIMING) {
            long timePassged = System.currentTimeMillis() - goal.getBaseDate();
            mG2StartDate.setText(TimeUtils.convertReadableDate(goal.getBaseDate(), TimeUtils.DATE_FORMAT_TYPE_2));
            mG2EndDate.setText("active");
            mG2Target.setText(String.valueOf(goal.getTarget()));
            mG2EndGoal.setVisibility(View.GONE);
            mG2ClaimGoal.setVisibility(View.VISIBLE);
            mG2TimePassed.setText(String.format("Day: %s Hour: %s Min: %s",
                    TimeUnit.MILLISECONDS.toDays(timePassged),
                    TimeUnit.MILLISECONDS.toHours(timePassged),
                    TimeUnit.MILLISECONDS.toMinutes(timePassged)));
        } else {
            long timePassged = goal.getEndDate() - goal.getBaseDate();
            mG2StartDate.setText(TimeUtils.convertReadableDate(goal.getBaseDate(), TimeUtils.DATE_FORMAT_TYPE_2));
            mG2EndDate.setText(TimeUtils.convertReadableDate(goal.getEndDate(), TimeUtils.DATE_FORMAT_TYPE_2));
            mG2Target.setText(String.valueOf(goal.getTarget()));
            mG2EndGoal.setVisibility(View.GONE);
            mG2TimePassed.setText(String.format("Day: %s Hour: %s Min: %s",
                    TimeUnit.MILLISECONDS.toDays(timePassged),
                    TimeUnit.MILLISECONDS.toHours(timePassged),
                    TimeUnit.MILLISECONDS.toMinutes(timePassged)));
        }
    }

    private void updateTestReulst1(Goal goal) {
        if (goal.getStatus() == Goal.GOAL_STATUS_LOCK || goal.getStatus() == Goal.GOAL_STATUS_IDLE) {
            mG1StartDate.setText("-");
            mG1EndDate.setText("-");
            mG1Target.setText("-");
            mG1TimePassed.setText("-");
            mG1EndGoal.setVisibility(View.GONE);
        } else if (goal.getStatus() == Goal.GOAL_STATUS_ACTIVE) {
            long timePassged = System.currentTimeMillis() - goal.getBaseDate();
            mG1StartDate.setText(TimeUtils.convertReadableDate(goal.getBaseDate(), TimeUtils.DATE_FORMAT_TYPE_2));
            mG1EndDate.setText("active");
            mG1Target.setText(String.valueOf(goal.getTarget()));
            mG1EndGoal.setVisibility(View.VISIBLE);
            mG1ClaimGoal.setVisibility(View.GONE);
            mG1TimePassed.setText(String.format("Day: %s Hour: %s Min: %s",
                    TimeUnit.MILLISECONDS.toDays(timePassged),
                    TimeUnit.MILLISECONDS.toHours(timePassged),
                    TimeUnit.MILLISECONDS.toMinutes(timePassged)));
        } else if (goal.getStatus() == Goal.GOAL_STATUS_CLAIMING) {
            long timePassged = System.currentTimeMillis() - goal.getBaseDate();
            mG1StartDate.setText(TimeUtils.convertReadableDate(goal.getBaseDate(), TimeUtils.DATE_FORMAT_TYPE_2));
            mG1EndDate.setText("active");
            mG1Target.setText(String.valueOf(goal.getTarget()));
            mG1EndGoal.setVisibility(View.VISIBLE);
            mG1EndGoal.setVisibility(View.GONE);
            mG1ClaimGoal.setVisibility(View.VISIBLE);
            mG1TimePassed.setText(String.format("Day: %s Hour: %s Min: %s",
                    TimeUnit.MILLISECONDS.toDays(timePassged),
                    TimeUnit.MILLISECONDS.toHours(timePassged),
                    TimeUnit.MILLISECONDS.toMinutes(timePassged)));
        } else {
            long timePassged = goal.getEndDate() - goal.getBaseDate();
            mG1StartDate.setText(TimeUtils.convertReadableDate(goal.getBaseDate(), TimeUtils.DATE_FORMAT_TYPE_2));
            mG1EndDate.setText(TimeUtils.convertReadableDate(goal.getEndDate(), TimeUtils.DATE_FORMAT_TYPE_2));
            mG1Target.setText(String.valueOf(goal.getTarget()));
            mG2EndGoal.setVisibility(View.GONE);
            mG1TimePassed.setText(String.format("Day: %s Hour: %s Min: %s",
                    TimeUnit.MILLISECONDS.toDays(timePassged),
                    TimeUnit.MILLISECONDS.toHours(timePassged),
                    TimeUnit.MILLISECONDS.toMinutes(timePassged)));
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle("Test View");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @OnClick(R.id.set_reminder_button)
    public void onSetReminderClicked() {
        String reminderPerMin = reminderNotificationPeriod.getText().toString();
        String boostPerMin = keepGoingNotificationPeriod.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(boostPerMin)) {
            keepGoingNotificationPeriod.setError(getString(R.string.error_field_required));
            focusView = keepGoingNotificationPeriod;
            cancel = true;
        }

        if (TextUtils.isEmpty(reminderPerMin)) {
            reminderNotificationPeriod.setError(getString(R.string.error_field_required));
            focusView = reminderNotificationPeriod;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            prefHelper.saveBoostNotificationPeriod(Integer.valueOf(boostPerMin));
            prefHelper.saveReminderNotificationPeriod(Integer.valueOf(reminderPerMin));
            Toast.makeText(getActivity(), "Ok", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("unused")
    @OnClick(R.id.reset_goals_button)
    public void onResetButtonClicked() {
        String target = newTarget.getText().toString();
        String endOfCampaign = campaignEndDate.getText().toString();

        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(endOfCampaign)) {
            campaignEndDate.setError(getString(R.string.error_field_required));
            focusView = campaignEndDate;
            cancel = true;
        }

        if (TextUtils.isEmpty(target)) {
            newTarget.setError(getString(R.string.error_field_required));
            focusView = newTarget;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            prefHelper.saveEndOfCampaignDate(System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(Long.valueOf(endOfCampaign)));
            dataManager.resetGoals(Long.valueOf(target));

            msgToActvInterface.onBackPresses(this);
        }

    }

    @OnClick({R.id.g1_claim_goal, R.id.g2_claim_goal, R.id.g3_claim_goal, R.id.g1_end_goal, R.id.g2_end_goal, R.id.g3_end_goal})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.g1_claim_goal:
                dataManager.rewardClaimedSuccessfullyForActiveGoal();
                break;
            case R.id.g2_claim_goal:
                dataManager.rewardClaimedSuccessfullyForActiveGoal();
                break;
            case R.id.g3_claim_goal:
                dataManager.rewardClaimedSuccessfullyForActiveGoal();
                break;
            case R.id.g1_end_goal:
                dataManager.endActiveGoal();
                break;
            case R.id.g2_end_goal:
                dataManager.endActiveGoal();
                break;
            case R.id.g3_end_goal:
                dataManager.endActiveGoal();
                break;
        }
    }
}
