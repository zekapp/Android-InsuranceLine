package com.insuranceline.ui.fragments.more;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.insuranceline.R;
import com.insuranceline.data.DataManager;
import com.insuranceline.data.local.PreferencesHelper;
import com.insuranceline.ui.fragments.BaseFragment;
import com.insuranceline.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by zeki on 21/02/2016.
 */
public class DebugModeFragment extends BaseFragment {

    @Inject
    DataManager dataManager;
    @Inject
    PreferencesHelper prefHelper;

    @Bind(R.id.keep_going_notification_period)
    EditText keepGoingNotificationPeriod;
    @Bind(R.id.reminder_notification_period)
    EditText reminderNotificationPeriod;
    @Bind(R.id.campaign_end_date)
    EditText campaignEndDate;
    @Bind(R.id.new_target)
    EditText newTarget;

    public static DebugModeFragment getInstance() {
        return new DebugModeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_debug, container, false);
        ButterKnife.bind(this, view);
        getActivityComponent().inject(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle("Debug");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @OnClick(R.id.set_boost_period)
    public void onSetBoostClicked(){
        String boostPerMin = keepGoingNotificationPeriod.getText().toString();
        boolean cancel = false;
        View focusView = null;
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(boostPerMin)) {
            keepGoingNotificationPeriod.setError(getString(R.string.error_field_required));
            focusView = keepGoingNotificationPeriod;
            cancel = true;
        }

        if (cancel){
            focusView.requestFocus();
        }else{
            prefHelper.saveBoostNotificationPeriod(Integer.valueOf(boostPerMin));
            Toast.makeText(getActivity(),"Ok",Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.set_reminder_period)
    public void onSetReminderClicked(){
        String reminderPerMin = reminderNotificationPeriod.getText().toString();
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(reminderPerMin)) {
            reminderNotificationPeriod.setError(getString(R.string.error_field_required));
            focusView = reminderNotificationPeriod;
            cancel = true;
        }

        if (cancel){
            focusView.requestFocus();
        }else{
            prefHelper.saveReminderNotificationPeriod(Integer.valueOf(reminderPerMin));
            Toast.makeText(getActivity(),"Ok",Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.set_end_campaign)
    public void onSetEndOfCampaignClicked(){
        String endOfCampaign = campaignEndDate.getText().toString();
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(endOfCampaign)) {
            campaignEndDate.setError(getString(R.string.error_field_required));
            focusView = campaignEndDate;
            cancel = true;
        }

        if (cancel){
            focusView.requestFocus();
        }else{
            prefHelper.saveEndOfCampaignDate(
                    System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(Long.valueOf(endOfCampaign))
            );
            Toast.makeText(getActivity(),"Ok",Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressLint("unused")
    @OnClick(R.id.reset_button)
    public void onResetButtonClicked() {
        String target = newTarget.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(target)) {
            newTarget.setError(getString(R.string.error_field_required));
            focusView = newTarget;
            cancel = true;
        }

        if (cancel){
            focusView.requestFocus();
        }else{
            dataManager.resetGoals(Long.valueOf(target));
            msgToActvInterface.onBackPresses(this);
        }

    }
}
