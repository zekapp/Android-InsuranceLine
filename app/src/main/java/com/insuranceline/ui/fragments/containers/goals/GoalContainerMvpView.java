package com.insuranceline.ui.fragments.containers.goals;

import com.insuranceline.data.vo.Goal;
import com.insuranceline.ui.base.MvpView;

/**
 * Created by Zeki Guler on 04,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public interface GoalContainerMvpView extends MvpView{

    void initView(Goal activeGoal);

}
