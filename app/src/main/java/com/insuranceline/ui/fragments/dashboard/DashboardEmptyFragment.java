package com.insuranceline.ui.fragments.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.insuranceline.R;
import com.insuranceline.ui.fragments.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Zeki Guler on 04,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class DashboardEmptyFragment extends BaseFragment {


    private static final String KEY_EMPTY_MESSAGE_ID = "dashboard.empty.fragment.key1";
    private @StringRes int mesId = R.string.dashboard_permission_need_info;

    public static DashboardEmptyFragment getInstance(@StringRes int id){
        DashboardEmptyFragment dashboardEmptyFragment = new DashboardEmptyFragment();
        Bundle data = new Bundle();
        data.putInt(KEY_EMPTY_MESSAGE_ID, id);
        dashboardEmptyFragment.setArguments(data);
        return dashboardEmptyFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Bind(R.id.empty_def_string) TextView mEmptyDefTextView;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setTitle("Dashboard");
        final View view = inflater.inflate(R.layout.fragment_dashboard_empty, container, false);
        ButterKnife.bind(this,view);

        mesId = getArguments().getInt(KEY_EMPTY_MESSAGE_ID);

        mEmptyDefTextView.setText(Html.fromHtml(getString(mesId)));

        return view;
    }
}