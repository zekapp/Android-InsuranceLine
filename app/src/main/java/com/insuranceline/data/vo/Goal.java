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

    public static final int GOAL_STATUS_LOCK    = 0;
    public static final int GOAL_STATUS_IDLE    = 1;
    public static final int GOAL_STATUS_ACTIVE  = 2;
    public static final int GOAL_STATUS_DONE    = 3;


    @Column
    @PrimaryKey(autoincrement = false)
    long mGoalId;

    @Column
    long mTarget;

    @Column
    long mAchievedSteps;

    @Column
    long mAchievedCalorie;

    @Column
    long mAchievedActiveMin;

    @Column
    long mAchievedDistance;

    @Column
    long mBaseDate;

    @Column
    long mEndDate;

    @Column(defaultValue = "1")
    int mGoalType = 1;

    @Column()
    int mStatus;

    @Column
    long mRequiredDailySteps;

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

    public long getTarget() {
        return mTarget;
    }

    public void setTarget(long target) {
        this.mTarget = target;
    }

    public long getAchievedSteps() {
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

    public long getRequiredDailySteps() {
        return mRequiredDailySteps;
    }

    public void setRequiredDailySteps(long requiredDailySteps) {
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


    public long getAchievedCalorie() {
        return mAchievedCalorie;
    }

    public void setAchievedCalorie(long achievedCalorie) {
        mAchievedCalorie = achievedCalorie;
    }

    public long getAchievedActiveMin() {
        return mAchievedActiveMin;
    }

    public void setAchievedActiveMin(long achievedActiveMin) {
        mAchievedActiveMin = achievedActiveMin;
    }

    public long getAchievedDistance() {
        return mAchievedDistance;
    }

    public void setAchievedDistance(int achievedDistance) {
        mAchievedDistance = achievedDistance;
    }

    public static Goal createDefaultGoal(int goalId, long endDate) {
        Goal goal = new Goal();
        goal.setGoalId(goalId);
        goal.reset(endDate, 100000);
        return goal;
    }

    public void reset( long endOfCampaignDate, long target) {
        boolean isCampaignActive = TimeUnit
                .MILLISECONDS.toDays(endOfCampaignDate - System.currentTimeMillis()) > 0;

        int stauts = isCampaignActive ?
                getGoalId() == 0  ? GOAL_STATUS_IDLE : GOAL_STATUS_LOCK :
                GOAL_STATUS_DONE;

        setAchievedSteps(0);
        setAchievedCalorie(0);
        setAchievedDistance(0);
        setAchievedActiveMin(0);
        setStatus(stauts);
        setEndDate(endOfCampaignDate);
        setBaseDate(System.currentTimeMillis());
        setRequiredDailyActiveMin(60);
        setRequiredDailyCalorie(3000);
        setRequiredDailyDistance(8);
        setRequiredDailySteps(5000);
        setGoalType(TYPE_STEPS); // STEPS
        setTarget(target);
    }

    private static int calculateReqDailyActiveMin(Goal prevGoal) {
        return 0;
    }

    public long getNextTarget(float leftDayForNextGoal, float difficulty ) {

        float pacePerDay    = getTarget() / getAchievedInDays();
        float newPacePerDay = pacePerDay * difficulty;
        float taget         = newPacePerDay * leftDayForNextGoal;

        return (long)taget;
    }

    public long getAchievedInDays() {
        long days = TimeUnit.MILLISECONDS.toDays(getEndDate() - getBaseDate());
        return days > 0 ? days : 1;
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
