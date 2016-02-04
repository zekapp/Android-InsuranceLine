package com.insuranceline.ui.fragments.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

    public static DashboardEmptyFragment getInstance(){
        return new DashboardEmptyFragment();
    }

    @Bind(R.id.empty_def_string) TextView mEmptyDefTextView;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setTitle("Dashboard");
        final View view = inflater.inflate(R.layout.fragment_dashboard_empty, container, false);
        ButterKnife.bind(this,view);
        mEmptyDefTextView.setText(Html.fromHtml(getString(R.string.no_data_info)));
        return view;
    }
}