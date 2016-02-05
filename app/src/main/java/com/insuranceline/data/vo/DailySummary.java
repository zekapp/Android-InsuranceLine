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
public class DailySummary extends BaseModel {
    @Column
    @PrimaryKey(autoincrement = false)
    long mId = 1;

    @Column
    int mDailySteps;

    @Column
    int mDailyCalories;

    @Column
    int mDailyActiveMinutes;

    @Column
    float mDailyDistance;

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
}
