package com.insuranceline.ui.login.connect;

import android.os.Bundle;

import com.insuranceline.R;
import com.insuranceline.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zeki on 2/02/2016.
 */
public class FBConnectActivity extends BaseActivity implements FBMvpView{

    @Inject FBConnectPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_fit_bit_connect);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.connect_fitbit)
    @SuppressWarnings("unused")
    public void onFitBitConnect(){

    }

}
