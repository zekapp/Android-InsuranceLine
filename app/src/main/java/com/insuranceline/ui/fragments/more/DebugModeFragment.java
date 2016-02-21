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

import java.text.SimpleDateFormat;
import java.util.Date;

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

    @SuppressLint("unused")
    @OnClick(R.id.reset_button)
    public void onResetButtonClicked() {
        String boostPerMin = keepGoingNotificationPeriod.getText().toString();
        String reminderPerMin = reminderNotificationPeriod.getText().toString();
        String endOfCampaign = campaignEndDate.getText().toString();
        String target = newTarget.getText().toString();
        long endOfCampaingLong = 0;
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

        if (TextUtils.isEmpty(target)) {
            newTarget.setError(getString(R.string.error_field_required));
            focusView = newTarget;
            cancel = true;
        }


        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat f = new SimpleDateFormat("dd MM yyyy HH:mm a");
            Date d = f.parse(endOfCampaign);
            endOfCampaingLong = d.getTime();
        } catch (Exception e) {
            campaignEndDate.setError(e.getLocalizedMessage());
            focusView = campaignEndDate;
            cancel = true;
        }


        if (cancel)
            focusView.requestFocus();
        else
            resetGoal(boostPerMin, reminderPerMin, target, endOfCampaingLong);

    }

    private void resetGoal(String boostPerMin, String reminderPerMin, String target, long endOfCampaign) {

        if (endOfCampaign == 0) throw new RuntimeException("end of campaing is zero");

        prefHelper.saveBoostNotificationPeriod(Integer.valueOf(boostPerMin));
        prefHelper.saveReminderNotificationPeriod(Integer.valueOf(reminderPerMin));
        prefHelper.saveEndOfCampaignDate(endOfCampaign);

        dataManager.resetGoals(Long.valueOf(target));

        Toast.makeText(getActivity(), "Goals reset according to your new values", Toast.LENGTH_LONG).show();

        msgToActvInterface.onBackPresses(this);

    }
}
