package com.insuranceline.ui.sample;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.insuranceline.R;
import com.insuranceline.data.vo.Sample;
import com.insuranceline.ui.base.BaseActivity;
import com.insuranceline.utils.DialogFactory;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Zeki Guler on 20,January,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class TestActivity extends BaseActivity implements TestMvpView {

    @Inject
    TestPresenter mTestPresenter;
    @Inject
    SampleAdapter mSampleAdapter;

    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mRecyclerView.setAdapter(mSampleAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTestPresenter.attachView(this);
        mTestPresenter.loadSamples();
    }

    /***** MVP View methods implementation *****/

    @Override
    public void showSamples(List<Sample> samples) {
        mSampleAdapter.setSamples(samples);
        mSampleAdapter.notifyDataSetChanged();
    }

    @Override
    public void showSamplesEmpty() {
        Toast.makeText(this, R.string.empty_samples, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showError() {
        DialogFactory.createGenericErrorDialog(this, getString(R.string.error_loading_sample))
                .show();
    }

    @OnClick(R.id.deleteUser)
    @SuppressWarnings("unused")
    public void onUserDelete(){
        Toast.makeText(this, "On delete Clicked",Toast.LENGTH_LONG).show();
        mTestPresenter.deleteUser();
    }
}