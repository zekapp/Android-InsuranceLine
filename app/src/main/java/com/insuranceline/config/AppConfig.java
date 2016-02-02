package com.insuranceline.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.insuranceline.di.qualifier.ApplicationContext;

import org.apache.commons.codec.binary.Base64;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by zeki on 17/01/2016.
 */

@Singleton
public class AppConfig {

    private static final String KEY_API_URL = "api_url";
    private static final int PASSWORD_LENGHT = 6;

    private static final String FITBIT_CLIENT_ID       = "227FGN";
    private static final String FITBIT_CLIENT_SECRET   = "c187653cce60f46581eebe0d5f11865b";
    private static final String FITBIT_REDIRECT_URI      = "apitester://logincallback";

    private final SharedPreferences mSharedPreferences;
    private String mEncodedAuthorizationHeader = "";

    @Inject
    public AppConfig(@ApplicationContext Context context) {
        mSharedPreferences = context.getSharedPreferences("app_cfg", Context.MODE_PRIVATE);
        mEncodedAuthorizationHeader = enCode();
    }

    private String enCode() {
        String combinedString = FITBIT_CLIENT_ID + ":" + FITBIT_CLIENT_SECRET;
        byte[] encoded = Base64.encodeBase64(combinedString.getBytes());
        return String.valueOf(encoded);
    }

    public String getApiUrl() {
        return "http://private-f7ff9-androidarchitecturetestapi.apiary-mock.com/api/v1/";
    }


    public String getEdgeSystemBaseUrl() {
        return "https://api.lifestylerewards.com.au/";
    }

    public String getFitBitBaseUrl() {
        return "https://www.fitbit.com/oauth2/";
    }

    public int getPasswordLength(){
        return PASSWORD_LENGHT;
    }

    public String getFitBitClientId() {
        return FITBIT_CLIENT_ID;
    }

    public String getFitBitReDirectUri() {
        return FITBIT_REDIRECT_URI;
    }

    public String getAuthorizationHeader() {
        return "Basic " + encodedString();
    }

    private String encodedString() {
       return mEncodedAuthorizationHeader;
    }
}
