package com.insuranceline.ui.claim;

import android.support.annotation.DrawableRes;

import com.insuranceline.ui.base.MvpView;

/**
 * Created by zeki on 15/02/2016.
 */
public interface NextGoalMvpView extends MvpView{

    void updateNextGoal(String nextTarget, boolean enableStartButton, @DrawableRes int cupResId);

    void newActivityStarted();

    void onError(String error);

    void showProgress();

    void hideProgress();

    void closeFragment();
}
