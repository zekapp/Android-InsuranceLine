package com.insuranceline.data.remote.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by zeki on 17/01/2016.
 */
public class SampleResponseData {
    @JsonProperty("data")
    private  SampleResponse mSampleResponse;

    public SampleResponse getSampleResponse() {
        return mSampleResponse;
    }

    public void setSampleResponse(SampleResponse sampleResponse) {
        mSampleResponse = sampleResponse;
    }
}
