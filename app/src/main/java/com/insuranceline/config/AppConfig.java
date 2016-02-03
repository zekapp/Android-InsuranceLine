package com.insuranceline.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.insuranceline.di.qualifier.ApplicationContext;

import org.apache.commons.codec.binary.Base64;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Zeki Guler on 03,February,2016
 * ©2015 Appscore. All Rights Reserved
 */

@Singleton
public class AppConfig {

    private static final String KEY_API_URL = "api_url";
    private static final int PASSWORD_LENGHT = 6;

    private static final String FIT_BIT_CLIENT_ID       = "227FGN";
    private static final String FIT_BIT_CLIENT_SECRET   = "c187653cce60f46581eebe0d5f11865b";
    private static final String FIT_BIT_REDIRECT_URI    = "apitester://logincallback"; // DO NOT FORGET TO CHANGE ACTIVITY INTENT FILTER IF YOU CHANGE THIS
    private static final String EDGE_SYSTEM_BASE_URL    = "https://api.lifestylerewards.com.au/";

    private static final String FIT_BIT_WEB_URL         = "https://www.fitbit.com/oauth2/";
    private static final String FIT_BIT_BASE_API_URL    = "https://api.fitbit.com/";


    private final SharedPreferences mSharedPreferences;
    private String mEncodedAuthorizationHeader = "";

    @Inject
    public AppConfig(@ApplicationContext Context context) {
        mSharedPreferences = context.getSharedPreferences("app_cfg", Context.MODE_PRIVATE);
        mEncodedAuthorizationHeader = enCode();
    }

    private String enCode() {
        String combinedString = FIT_BIT_CLIENT_ID + ":" + FIT_BIT_CLIENT_SECRET;
        byte[] encoded = Base64.encodeBase64(combinedString.getBytes());
        return Arrays.toString(encoded);
    }

    public String getApiUrl() {
        return "http://private-f7ff9-androidarchitecturetestapi.apiary-mock.com/api/v1/";
    }


    public String getEdgeSystemBaseUrl() {
        return EDGE_SYSTEM_BASE_URL;
    }

    public String getFitBitBaseUrl() {
        return FIT_BIT_BASE_API_URL;
    }

    public int getPasswordLength(){
        return PASSWORD_LENGHT;
    }

    public String getFitBitClientId() {
        return FIT_BIT_CLIENT_ID;
    }

    public String getFitBitReDirectUri() {
        return FIT_BIT_REDIRECT_URI;
    }

    public String getAuthorizationHeader() {
        return "Basic " + encodedString();
    }

    private String encodedString() {
       return mEncodedAuthorizationHeader;
    }

    public String getFitBitBrowserUrl() {
        return  FIT_BIT_WEB_URL + "authorize?" +
                "response_type=code" +
                "&client_id=" + FIT_BIT_CLIENT_ID +
                "&scope=activity profile heartrate location nutrition settings social sleep weight" +
                "&prompt=login" +
                "&redirect_uri=" + FIT_BIT_REDIRECT_URI;
    }
}
