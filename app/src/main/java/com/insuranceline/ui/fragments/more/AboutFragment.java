package com.insuranceline.ui.fragments.more;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.insuranceline.R;
import com.insuranceline.ui.fragments.BaseFragment;

/**
 * Created by Zeki Guler on 04,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class AboutFragment extends BaseFragment {


    public static AboutFragment getInstance(){
        return new AboutFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_about_insuranceline, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle("About");
        setHomeAsUpEnabled(R.drawable.icon_back);
    }
}
