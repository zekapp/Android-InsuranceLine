package com.insuranceline.data.remote.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by zeki on 31/01/2016.
 */
public class APIError {

    @JsonProperty("error")
    private String mError;

    @JsonProperty("error_description")
    private String mErrorDescription;

    public APIError() {
    }

    public String getmError() {
        return mError;
    }

    public String getmErrorDescription() {
        return mErrorDescription;
    }
}
