package com.insuranceline.ui.login.termAndCond;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.insuranceline.R;
import com.insuranceline.ui.DispatchActivity;
import com.insuranceline.ui.base.BaseActivity;
import com.insuranceline.ui.login.LoginActivity;
import com.insuranceline.ui.login.connect.FBConnectActivity;
import com.insuranceline.utils.DialogFactory;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Zeki Guler on 02,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class TermCondActivity extends BaseActivity implements TermCondMvpView{

    @Inject TermCondPresenter mTermCondPresenter;

    private ProgressDialog mProcessDialog;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_terms_cond);
        ButterKnife.bind(this);
        mTermCondPresenter.attachView(this);

        setHomeAsUpEnabled();

    }

    public void setHomeAsUpEnabled(){
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.icon_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mToolbar.setNavigationIcon(R.drawable.icon_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TermCondActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mProcessDialog != null) mProcessDialog.dismiss();
    }

    @OnClick(R.id.tcAccepted)
    @SuppressWarnings("unused")
    public void onAcceptClicked(){
        mTermCondPresenter.accept();
    }

    @OnClick(R.id.tcDeclined)
    @SuppressWarnings("unused")
    public void onDeclineClicked(){
        DialogFactory.createGenericDialog(this,
                R.string.term_cond_activity_name,
                R.string.tandc_rejected_error,
                R.string.ok,
                R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    /************ MVP View Method Implementation **************/

    @Override
    public void onSuccess() {
        dispatchTo();
    }

    private void dispatchTo() {
        Intent intent = new Intent(this, DispatchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void showProgress() {
        mProcessDialog = DialogFactory.createProgressDialog(this, "Please wait...");
        mProcessDialog.show();
    }

    @Override
    public void hideProgress() {
        mProcessDialog.dismiss();
    }

    @Override
    public void error(String error) {
        Toast.makeText(this,error,Toast.LENGTH_LONG).show();
    }
}
