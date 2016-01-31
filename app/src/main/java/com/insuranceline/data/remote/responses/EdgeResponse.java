package com.insuranceline.data.remote.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by zeki on 30/01/2016.
 */
public class EdgeResponse {

    @JsonProperty("access_token")
    String mAccessToken;

    @JsonProperty("token_type")
    String mTokenType;

    @JsonProperty("expires_in")
    long mExpireIn;
    public String getmAccessToken() {
        return mAccessToken;
    }

    public void setmAccessToken(String mAccessToken) {
        this.mAccessToken = mAccessToken;
    }

    public String getmTokenType() {
        return mTokenType;
    }

    public void setmTokenType(String mTokenType) {
        this.mTokenType = mTokenType;
    }

    public long getmExpireIn() {
        return mExpireIn;
    }

    public void setmExpireIn(long mExpireIn) {
        this.mExpireIn = mExpireIn;
    }
}
