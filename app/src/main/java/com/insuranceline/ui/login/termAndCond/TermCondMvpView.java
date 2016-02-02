package com.insuranceline.ui.login.termAndCond;

import com.insuranceline.ui.base.BasePresenter;
import com.insuranceline.ui.base.MvpView;


/**
 * Created by Zeki Guler on 02,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public interface TermCondMvpView extends MvpView{

    void onSuccess();

    void showProgress();

    void hideProgress();

    void error(String error);
}
