package com.insuranceline.ui.fragments.containers.goals;

import com.insuranceline.data.DataManager;
import com.insuranceline.ui.base.BasePresenter;

import javax.inject.Inject;

/**
 * Created by Zeki Guler on 04,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class GoalContainerPresenter extends BasePresenter<GoalContainerMvpView>{

    private final DataManager mDataManager;

    @Inject
    public GoalContainerPresenter(DataManager dataManager){

        mDataManager = dataManager;
    }

    @Override
    public void attachView(GoalContainerMvpView mvpView) {
        super.attachView(mvpView);
    }

    public void fetchNextView() {
        int id = mDataManager.getlastUnfinishGoalId();
        getMvpView().initView(id);
    }


}
