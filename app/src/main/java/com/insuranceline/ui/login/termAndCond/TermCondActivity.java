package com.insuranceline.ui.login.termAndCond;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.insuranceline.R;
import com.insuranceline.ui.base.BaseActivity;
import com.insuranceline.utils.DialogFactory;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Zeki Guler on 02,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class TermCondActivity extends BaseActivity implements TermCondMvpView{

    @Inject TermCondPresenter mTermCondPresenter;

    private ProgressDialog mProcessDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_terms_cond);
        ButterKnife.bind(this);
        mTermCondPresenter.attachView(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mProcessDialog != null) mProcessDialog.dismiss();
    }

    @OnClick(R.id.tcAccepted)
    @SuppressWarnings("unused")
    public void onAcceptClicked(){
        Toast.makeText(this, "onAcceptClicked", Toast.LENGTH_LONG).show();
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
