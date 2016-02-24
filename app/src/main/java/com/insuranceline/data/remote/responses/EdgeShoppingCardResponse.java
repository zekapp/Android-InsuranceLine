package com.insuranceline.data.remote.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Created by Zeki Guler on 24,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class EdgeShoppingCardResponse {
    public boolean success;
    public Errors errors;

    @JsonIgnoreProperties(ignoreUnknown = true)
    static public class Errors{
        public int $id;
        public List<Values> $values;

        @JsonIgnoreProperties(ignoreUnknown = true)
        static public class Values{
            public int errorCode;
            public String message;
        }
    }

    public String getErrorsAsText(){

        String errorMes = "Error not defined";
        if (errors != null) {
            for (Errors.Values error : errors.$values){
                errorMes += error.message + "/n";
            }
        }

        return errorMes;
    }

}
