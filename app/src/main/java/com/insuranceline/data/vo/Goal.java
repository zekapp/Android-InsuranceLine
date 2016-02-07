package com.insuranceline.data.vo;

import com.insuranceline.data.local.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Zeki Guler on 05,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
@Table(databaseName = AppDatabase.NAME)
public class Goal extends BaseModel {
    private static final int TYPE_STEPS = 1;
    private static final int TYPE_CALORIE = 2;
    private static final int TYPE_ACTIVE_MIN = 3;
    private static final int TYPE_DISTANCE = 4;
    @Column
    @PrimaryKey(autoincrement = false)
    long mGoalId;

    @Column
    int mTarget;

    @Column
    int mAchieved;

    @Column
    long mStartDate;

    @Column
    long mEndDate;

    @Column(defaultValue = "1")
    int mGoalType = 1;

    @Column(defaultValue = "true")
    boolean isActive = true;

    @Column
    int mRequiredDailySteps;

    @Column
    int mRequiredDailyCalorie;

    @Column
    int requiredDailyActiveMin;

    @Column
    int requiredDailyDistance;

    public long getGoalId() {
        return mGoalId;
    }

    public void setGoalId(long goalId) {
        mGoalId = goalId;
    }

    public int getTarget() {
        return mTarget;
    }

    public void setTarget(int target) {
        this.mTarget = target;
    }

    public int getAchieved() {
        return mAchieved;
    }

    public void setAchieved(int achieved) {
        this.mAchieved = achieved;
    }

    public long getStartDate() {
        return mStartDate;
    }

    public void setStartDate(long startDate) {
        this.mStartDate = startDate;
    }

    public long getEndDate() {
        return mEndDate;
    }

    public void setEndDate(long endDate) {
        this.mEndDate = endDate;
    }

    public int getGoalType() {
        return mGoalType;
    }

    public void setGoalType(int goalType) {
        this.mGoalType = goalType;
    }

    public int getRequiredDailySteps() {
        return mRequiredDailySteps;
    }

    public void setRequiredDailySteps(int requiredDailySteps) {
        this.mRequiredDailySteps = requiredDailySteps;
    }

    public int getRequiredDailyCalorie() {
        return mRequiredDailyCalorie;
    }

    public void setRequiredDailyCalorie(int requiredDailyCalorie) {
        this.mRequiredDailyCalorie = requiredDailyCalorie;
    }

    public int getRequiredDailyActiveMin() {
        return requiredDailyActiveMin;
    }

    public void setRequiredDailyActiveMin(int requiredDailyActiveMin) {
        this.requiredDailyActiveMin = requiredDailyActiveMin;
    }

    public int getRequiredDailyDistance() {
        return requiredDailyDistance;
    }

    public void setRequiredDailyDistance(int requiredDailyDistance) {
        this.requiredDailyDistance = requiredDailyDistance;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public static Goal createDefaultGoal() {
        Goal goal = new Goal();
        goal.setAchieved(0);
        goal.setActive(true);
        goal.setEndDate(0);
        goal.setGoalId(1);
        goal.setStartDate(System.currentTimeMillis());
        goal.setRequiredDailyActiveMin(20);
        goal.setRequiredDailyCalorie(3000);
        goal.setRequiredDailyDistance(10);
        goal.setRequiredDailySteps(1000);
        goal.setGoalType(TYPE_STEPS); // STEPS
        goal.setTarget(100000); // Target ids 100,000 steps in 3 months (date is not important)
        return goal;
    }

}
