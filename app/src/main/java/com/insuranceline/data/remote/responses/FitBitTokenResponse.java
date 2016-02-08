package com.insuranceline.data.remote.responses;

/**
 * Created by Zeki Guler on 2/02/2016.
 */
public class FitBitTokenResponse {
    private String access_token;
    private int expires_in;
    private String refresh_token;
    private String scope;
    private String token_type;
    private String user_id;

    public String getAccess_token() {
        return access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public String getScope() {
        return scope;
    }

    public String getToken_type() {
        return token_type;
    }

    public String getUser_id() {
        return user_id;
    }


    public String getTokenAsString() {
        return  getAccess_token()  + ":" +
                getRefresh_token() + ":" +
                getUser_id();
    }
}
