package com.insuranceline.ui.fragments.goals;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.insuranceline.ui.base.MvpView;

/**
 * Created by Zeki Guler on 10,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public interface GoalFragmentMvpView extends MvpView{

    void updateCupImg(@DrawableRes int cupRes);

    void updateGoalDef(String defRes);

    void updateGoalTitle(@StringRes  int titleRes);

    void updateButtonTitleAndStatus(@StringRes int buttonStatus, boolean enabled);

    void onError(String error);

    void showProgress();

    void hideProgress();
}
