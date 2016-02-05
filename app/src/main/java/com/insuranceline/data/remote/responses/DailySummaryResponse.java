package com.insuranceline.data.remote.responses;
import java.util.List;

/**
 * Created by Zeki Guler on 05,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class DailySummaryResponse {

    public Summary summary;

    public class Summary {
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
            if (distances != null || !distances.isEmpty()){
                for (Distance d : distances){
                    if (d.activity.contains("total"))
                     return d.distance;
                }
                return 0f;
            }
            return 0f;
        }

        public class Distance {
            private String activity;
            private float distance;
        }
    }
}
