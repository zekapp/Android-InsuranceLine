package com.insuranceline.data.remote.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.insuranceline.data.vo.DailySummary;

import java.util.List;

/**
 * Created by Zeki Guler on 05,February,2016
 * ©2015 Appscore. All Rights Reserved
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public final class DailySummaryResponse {

    public Summary summary;

    @JsonIgnoreProperties(ignoreUnknown=true)
    static public class Summary {
        public int activeScore;
        public int activityCalories;
        public int caloriesBMR;
        public int caloriesOut;
        public List<Distance> distances;
        public int fairlyActiveMinutes;
        public int lightlyActiveMinutes;
        public int marginalCalories;
        public int sedentaryMinutes;
        public int steps;
        public int veryActiveMinutes;

        public float getDistance() {
            if (distances != null && !distances.isEmpty()){
                for (Distance d : distances){
                    if (d.activity != null && d.activity.equals("total"))
                        return d.distance;
                }
                return 0f;
            }
            return 0f;
        }

        @JsonIgnoreProperties(ignoreUnknown=true)
        static public class Distance {
            public String activity;
            public float distance;
        }
    }

    public DailySummary getDailySummary(){
        DailySummary dailySum = new DailySummary();
        dailySum.setmSummaryId(1);
        dailySum.setDailyActiveMinutes(summary.lightlyActiveMinutes);
        dailySum.setDailyCalories(summary.caloriesOut);
        dailySum.setDailySteps(summary.steps);
        dailySum.setDailyDistance(summary.getDistance());
        dailySum.setmRefreshTime(System.currentTimeMillis());
        return dailySum;
    }
}
