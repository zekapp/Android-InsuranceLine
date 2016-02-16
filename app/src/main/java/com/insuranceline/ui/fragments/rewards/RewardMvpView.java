package com.insuranceline.ui.fragments.rewards;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.insuranceline.ui.base.MvpView;

/**
 * Created by Zeki Guler on 12,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public interface RewardMvpView extends MvpView{

    void updateButtonStatus(String title, @DrawableRes int background, boolean isButtonEnabled);

    void updateDefinition(@StringRes int defResId);

    void onError(String error);

    void showProgress();

    void hideProgress();
}
