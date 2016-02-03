package com.insuranceline.ui.login.connect;

import com.insuranceline.ui.base.MvpView;

/**
 * Created by Zeki Guler on 03,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public interface FBMvpView extends MvpView {

    void showProgress();

    void hideProgress();

    void onSuccess();

    void error(String localizedMessage);
}
