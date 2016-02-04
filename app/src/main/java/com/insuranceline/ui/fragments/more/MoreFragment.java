package com.insuranceline.ui.fragments.more;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.insuranceline.R;
import com.insuranceline.di.qualifier.ActivityContext;
import com.insuranceline.ui.fragments.BaseFragment;
import com.insuranceline.ui.fragments.containers.BaseContainerFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Zeki Guler on 03,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class MoreFragment extends BaseFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_more, container, false);
        ButterKnife.bind(this, view);
        getActivityComponent().inject(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle("More");
    }

    @OnClick(R.id.more_1)
    public void more1(){

    }

    @OnClick(R.id.more_2)
    public void more2(){

    }

    @OnClick(R.id.more_3)
    public void more3(){

    }

    @OnClick(R.id.more_4)
    public void more4(){

    }

    @OnClick(R.id.more_5)
    public void more5(){
        Toast.makeText(mContext,"Ok",Toast.LENGTH_LONG).show();
        startFragment(AboutFragment.getInstance());
    }

    @OnClick(R.id.more_6)
    public void more6(){

    }
}
