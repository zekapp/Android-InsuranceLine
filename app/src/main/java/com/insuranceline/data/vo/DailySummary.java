package com.insuranceline.data.vo;

import com.insuranceline.data.local.AppDatabase;
import com.insuranceline.utils.Validation;
import com.insuranceline.utils.ValidationFailedException;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Zeki Guler on 05,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
@Table(databaseName = AppDatabase.NAME)
public class DailySummary extends BaseModel implements Validation {
    private static final long SECOND   = 1000;
    private static final long MINUTE   = 60 * SECOND;
    private static final long STALE_MS = 5 * SECOND; // Data is stale after 5 seconds

    @Column
    @PrimaryKey(autoincrement = false)
    long mSummaryId;

    @Column
    int mDailySteps;

    @Column
    int mDailyCalories;

    @Column
    int mDailyActiveMinutes;

    @Column
    float mDailyDistance;

    @Column
    long mRefreshTime;

    public long getmSummaryId() {
        return mSummaryId;
    }

    public void setmSummaryId(long mSummaryId) {
        this.mSummaryId = mSummaryId;
    }

    public int getDailySteps() {
        return mDailySteps;
    }

    public void setDailySteps(int dailySteps) {
        mDailySteps = dailySteps;
    }

    public int getDailyCalories() {
        return mDailyCalories;
    }

    public void setDailyCalories(int dailyCalories) {
        mDailyCalories = dailyCalories;
    }

    public int getDailyActiveMinutes() {
        return mDailyActiveMinutes;
    }

    public void setDailyActiveMinutes(int dailyActiveMinutes) {
        mDailyActiveMinutes = dailyActiveMinutes;
    }

    public float getDailyDistance() {
        return mDailyDistance;
    }

    public void setDailyDistance(float dailyDistance) {
        mDailyDistance = dailyDistance;
    }

    public long getmRefreshTime() {
        return mRefreshTime;
    }

    public void setmRefreshTime(long mRefreshTime) {
        this.mRefreshTime = mRefreshTime;
    }

    public boolean isUpToDate() {
        return System.currentTimeMillis() - mRefreshTime < MINUTE;

    }

    @Override
    public void validate() {
        if (mSummaryId > 0)
            throw new ValidationFailedException("invalid user email");
    }
}
