package com.insuranceline.ui.fragments.more;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.insuranceline.R;
import com.insuranceline.ui.fragments.BaseFragment;

/**
 * Created by zeki on 24/02/2016.
 */
public class PrivacyPolicy extends BaseFragment {


    public static PrivacyPolicy getInstance(){
        return new PrivacyPolicy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_privacy_insuranceline, container, false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle("About");
        setHomeAsUpEnabled(R.drawable.icon_back);
    }
}
