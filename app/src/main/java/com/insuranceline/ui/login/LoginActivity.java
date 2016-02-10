package com.insuranceline.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.insuranceline.R;
import com.insuranceline.ui.DispatchActivity;
import com.insuranceline.ui.base.BaseActivity;
import com.insuranceline.ui.sample.TestActivity;
import com.insuranceline.utils.DialogFactory;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Zeki Guler on 29,January,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class LoginActivity extends BaseActivity implements LoginMvpView{

    @Inject LoginPresenter mLoginPresenter;

    @Bind(R.id.login_email) EditText    mLoginEmmailEditText;
    @Bind(R.id.login_password) EditText mLoginPasswordEditText;

    private ProgressDialog mProcessDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mLoginPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginPresenter.detachView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLoginPresenter.detachView();
        if (mProcessDialog != null)
            mProcessDialog.dismiss();
    }

    /***** MVP View methods implementation *****/

    @Override
    public void showProgress() {
        mProcessDialog = DialogFactory.createProgressDialog(this, "Please wait...");
        mProcessDialog.show();
    }

    @Override
    public void hideProgress() {
        if (mProcessDialog != null) mProcessDialog.dismiss();
    }

    @Override
    public void showLoginError(String error) {
        Toast.makeText(this,error,Toast.LENGTH_LONG).show();
    }

    @Override
    public void loginSuccess() {
//        Toast.makeText(this,"Login Success",Toast.LENGTH_LONG).show();
        launchDispatchActivity();
    }


    @Override
    public void onErrorPassword(String error) {
        mLoginPasswordEditText.setError(error);
    }

    @Override
    public void onErrorEmail(String error) {
        mLoginEmmailEditText.setError(error);
    }

    @OnClick(R.id.login_get_started)
    @SuppressWarnings("unused")
    public void onLoginStart(View view) {
        resetErrors();
        mLoginPresenter.attemptToLogin(
                mLoginEmmailEditText.getText().toString(),
                mLoginPasswordEditText.getText().toString());
    }

    private void resetErrors(){
        mLoginEmmailEditText.setError(null);
        mLoginPasswordEditText.setError(null);
    }

    private void launchDispatchActivity() {
        Intent intent = new Intent(this, DispatchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @OnClick(R.id.test_button)
    @SuppressWarnings("unused")
    public void goMainClicked(View view) {
        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);
    }

}
