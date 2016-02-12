package com.insuranceline.event;

import com.insuranceline.data.vo.Goal;

/**
 * Created by Zeki Guler on 12,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class GoalAchieveEvent {
    private Goal mActiveGoal;

    public GoalAchieveEvent(Goal activeGoal) {
        mActiveGoal = activeGoal;
    }

    public Goal getActiveGoal() {
        return mActiveGoal;
    }
}
