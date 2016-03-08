package com.insuranceline.ui.claim;

import android.support.annotation.DrawableRes;

import com.insuranceline.ui.base.MvpView;

import javax.annotation.Resource;

/**
 * Created by zeki on 15/02/2016.
 */
public interface EmailGetMVPView extends MvpView {

    void fillEmailAddressField(String emailAddress);

    void onErrorEmail(String error);

    void onError(String error);

    void onSuccess();

    void hideProgress();

    void showProgress();

    void allGoalAchieved();

    void updateImage(@DrawableRes int resId);
}
