package com.insuranceline.data.remote.responses;

/**
 * Created by zeki on 15/02/2016.
 */
public class ClaimRewardResponse {
    boolean success = true;
    String errorMessage = "Something happens terrible";

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
