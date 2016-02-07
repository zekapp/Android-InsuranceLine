package com.insuranceline.data.remote.model;

import com.insuranceline.data.vo.DailySummary;
import com.insuranceline.data.vo.Goal;

/**
 * Created by zeki on 8/02/2016.
 */
public class DashboardModel {
    private Goal  mActiveGoal;
    private DailySummary mDailySummary;

    public Goal getActiveGoal() {
        return mActiveGoal;
    }

    public void setActiveGoal(Goal activeGoal) {
        this.mActiveGoal = activeGoal;
    }

    public Goal getmActiveGoal() {
        return mActiveGoal;
    }


    public DailySummary getmDailySummary() {
        return mDailySummary;
    }

    public void setmDailySummary(DailySummary mDailySummary) {
        this.mDailySummary = mDailySummary;
    }
}
