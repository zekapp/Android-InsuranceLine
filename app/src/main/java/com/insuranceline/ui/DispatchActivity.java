package com.insuranceline.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.insuranceline.config.AppConfig;
import com.insuranceline.data.DataManager;
import com.insuranceline.data.vo.EdgeUser;
import com.insuranceline.ui.base.BaseActivity;
import com.insuranceline.ui.login.LoginActivity;
import com.insuranceline.ui.login.connect.FBConnectActivity;
import com.insuranceline.ui.login.termAndCond.TermCondActivity;
import com.insuranceline.ui.main.MainActivity;
import com.insuranceline.utils.DialogFactory;
import com.insuranceline.utils.Utils;

import javax.inject.Inject;

import au.com.lumo.ameego.LumoController;
import rx.Observer;
import rx.Subscription;
import timber.log.Timber;

/**
 * Created by Zeki Guler on 29,January,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class DispatchActivity extends BaseActivity{

    @Inject  DataManager mDataManager;

    private Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);

        mSubscription = mDataManager.getUser().subscribe(new Observer<EdgeUser>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Timber.e(e.getMessage());
                dispatchLoginActivity();
            }

            @Override
            public void onNext(EdgeUser edgeUser) {
                if (edgeUser.isFitBitUser()) {
                    if (edgeUser.isTermCondAccepted()) {
                        dispatchInsuranceLineApp();
                    } else {
                        dispatchUserTermCondAccept();
                    }
                } else {
                    dispatchLumoAmeego(edgeUser);
                }
/*                if (edgeUser.isTermCondAccepted()){
                    if (edgeUser.isFitBitUser())
                        dispatchInsuranceLineApp();
                    else
                        dispatchLumoAmeego(edgeUser);
                }else
                    dispatchUserTermCondAccept();*/
            }
        });
    }

    private void dispatchUserTermCondAccept() {
        Intent intent = new Intent(this, TermCondActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void dispatchLumoAmeego(EdgeUser edgeUser) {
//        Toast.makeText(this,"Lumo Ameego is launching...",Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, LumoController.getInstance().getDispatchActivity());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    private void dispatchInsuranceLineApp() {
        boolean isConnected = mDataManager.isFitBitConnected();
        if (!isConnected) dispatchFitBitConnect();
        else             dispatchFitBitMain();
    }

    private void dispatchFitBitMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void dispatchFitBitConnect() {
        Intent intent = new Intent(this, FBConnectActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void dispatchLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
