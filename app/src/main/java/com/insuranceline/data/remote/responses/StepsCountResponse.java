package com.insuranceline.data.remote.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by zeki on 7/02/2016.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public final class StepsCountResponse {

    @JsonProperty("activities-steps")
    public List<Steps> steps;

    @JsonIgnoreProperties(ignoreUnknown=true)
    static public class Steps {
        public String dateTime;
        public int value;
    }

    public int getTotlaStepsCount(){
        int count = 0;
        if (steps != null && !steps.isEmpty()){
            for (Steps step : steps){
                count += step.value;
            }
        }
        return count;
    }
}
