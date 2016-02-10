package com.insuranceline.ui.fragments.containers.dashboard;

import com.insuranceline.ui.base.MvpView;

/**
 * Created by Zeki Guler on 04,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public interface DashboardContainerMvpView extends MvpView{
    void initView(boolean isAnyGoalSet, boolean isPermissionDone, boolean keepInitializedView);
}
