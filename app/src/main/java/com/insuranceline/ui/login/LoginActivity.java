package com.insuranceline.ui.login;

import android.os.Bundle;

import com.insuranceline.R;
import com.insuranceline.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * Created by Zeki Guler on 29,January,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class LoginActivity extends BaseActivity implements LoginMvpView{

    @Inject LoginPresenter mLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }

    /***** MVP View methods implementation *****/

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showLoginError() {

    }

    @Override
    public void loginSuccess() {

    }

    @Override
    public void onErrorPassword() {

    }

    @Override
    public void onErrorEmail() {

    }
}
