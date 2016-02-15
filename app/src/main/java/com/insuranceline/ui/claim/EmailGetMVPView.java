package com.insuranceline.ui.claim;

import com.insuranceline.ui.base.MvpView;

/**
 * Created by zeki on 15/02/2016.
 */
public interface EmailGetMVPView extends MvpView {

    void onErrorEmail(String error);

    void onError(String error);

    void onSuccess();

    void hideProgress();

    void showProgress();

    void allGoalAchieved();
}
