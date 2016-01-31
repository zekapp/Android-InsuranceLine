package com.insuranceline.ui.login;

import com.insuranceline.ui.base.MvpView;

/**
 * Created by Zeki Guler on 29,January,2016
 * ©2015 Appscore. All Rights Reserved
 */
public interface LoginMvpView extends MvpView {

    void showProgress();

    void hideProgress();

    void showLoginError(String error);

    void loginSuccess();

    void onErrorPassword(String error);

    void onErrorEmail(String error);
}
