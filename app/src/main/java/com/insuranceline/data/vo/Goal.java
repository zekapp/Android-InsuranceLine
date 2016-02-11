package com.insuranceline.data.vo;

import com.insuranceline.data.local.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.concurrent.TimeUnit;

/**
 * Created by Zeki Guler on 05,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
@Table(databaseName = AppDatabase.NAME)
public class Goal extends BaseModel {
    private static final int TYPE_STEPS         = 1;
    private static final int TYPE_CALORIE       = 2;
    private static final int TYPE_ACTIVE_MIN    = 3;
    private static final int TYPE_DISTANCE      = 4;
    public static final int GOAL_STATUS_IDLE    = 0;
    public static final int GOAL_STATUS_ACTIVE  = 1;
    public static final int GOAL_STATUS_DONE    = 2;


    @Column
    @PrimaryKey(autoincrement = false)
    long mGoalId;

    @Column
    int mTarget;

    @Column
    int mAchievedSteps;

    @Column
    int mAchievedCalorie;

    @Column
    int mAchievedActiveMin;

    @Column
    int mAchievedDistance;

    @Column
    long mBaseDate;

    @Column
    long mEndDate;

    @Column(defaultValue = "1")
    int mGoalType = 1;

    @Column(defaultValue = "0")
    int mStatus = GOAL_STATUS_IDLE;

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

    public int getAchievedSteps() {
        return mAchievedSteps;
    }

    public void setAchievedSteps(int achievedSteps) {
        this.mAchievedSteps = achievedSteps;
    }

    public long getBaseDate() {
        return mBaseDate;
    }

    public void setBaseDate(long baseDate) {
        this.mBaseDate = baseDate;
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

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int status) {
        mStatus = status;
    }


    public int getAchievedCalorie() {
        return mAchievedCalorie;
    }

    public void setAchievedCalorie(int achievedCalorie) {
        mAchievedCalorie = achievedCalorie;
    }

    public int getAchievedActiveMin() {
        return mAchievedActiveMin;
    }

    public void setAchievedActiveMin(int achievedActiveMin) {
        mAchievedActiveMin = achievedActiveMin;
    }

    public int getAchievedDistance() {
        return mAchievedDistance;
    }

    public void setAchievedDistance(int achievedDistance) {
        mAchievedDistance = achievedDistance;
    }

    public static Goal createDefaultGoal(int goalId) {
        Goal goal = new Goal();
        goal.setAchievedSteps(0);
        goal.setAchievedCalorie(0);
        goal.setAchievedDistance(0);
        goal.setAchievedActiveMin(0);
        goal.setStatus(GOAL_STATUS_IDLE);
        goal.setGoalId(goalId);
        goal.setEndDate(0);
        goal.setBaseDate(System.currentTimeMillis());
        goal.setRequiredDailyActiveMin(60);
        goal.setRequiredDailyCalorie(3000);
        goal.setRequiredDailyDistance(8);
        goal.setRequiredDailySteps(5000);
        goal.setGoalType(TYPE_STEPS); // STEPS
        goal.setTarget(100000); // Target ids 100,000 steps in 3 months (date is not important)
        return goal;
    }

    private static int calculateReqDailyActiveMin(Goal prevGoal) {
        return 0;
    }

    public int getNextTarget(int dayLeft) {
        return dayLeft * getTarget() / getAchievedInDays();
    }

    public int getAchievedInDays() {
        long days = (int)TimeUnit.MILLISECONDS.toDays(getEndDate() - getBaseDate());
        return days > 0 ? (int)days : 1;
    }

    public int getNextActiveMinute() {
        return  getRequiredDailyActiveMin();
    }

    public int getNextReqCalorie() {
        return getRequiredDailyCalorie();
    }

    public int getNextDailyReqSteps() {
        return getRequiredDailyDistance();
    }

    public boolean isActive() {
        return getStatus() == GOAL_STATUS_ACTIVE;
    }
}
