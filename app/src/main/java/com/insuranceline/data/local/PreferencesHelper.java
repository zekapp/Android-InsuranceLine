package com.insuranceline.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.insuranceline.data.remote.responses.FitBitTokenResponse;
import com.insuranceline.di.qualifier.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferencesHelper {

    public static final String PREF_FILE_NAME = "android_arhitecture_pref_file";
    private static final String FIT_BIT_REFRESH_TOKEN = "FIT_BIT_REFRESH_TOKEN";
    private static final String FIT_BIT_ACCESS_TOKEN = "FIT_BIT_ACCESS_TOKEN";
    private static final String EDGE_SYSTEM_ACCESS_TOKEN = "EDGE_SYSTEM_ACCESS_TOKEN";
    private static final String FIT_BIT_CONNECTION_STATUS = "FIT_BIT_CONNECTION_STATUS";
    private static final String FIT_BIT_USER_ID = "FIT_BIT_USER_ID";
    private static final String FIT_BIT_SCOPE_PERMISSION = "FIT_BIT_SCOPE_PERMISSION";
    private static final String APPLICATION_FIRST_LAUNCHING = "APPLICATION_FIRST_LAUNCHING";

    private final SharedPreferences mPref;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void clear() {
        mPref.edit().clear().apply();
    }

    public String getFitBitRefreshToken() {
        return mPref.getString(FIT_BIT_REFRESH_TOKEN,"");
    }

    public void saveFitBitRefreshToken(String refresh_token) {
        mPref.edit().putString(FIT_BIT_REFRESH_TOKEN, refresh_token).apply();
    }

    public void saveFitBitAccessToken(String access_token) {
        mPref.edit().putString(FIT_BIT_ACCESS_TOKEN, access_token).apply();
    }

    public String getFitBitAccessToken() {
        return mPref.getString(FIT_BIT_ACCESS_TOKEN,"");
    }

    public void saveEdgeSystemToken(String token) {
        mPref.edit().putString(EDGE_SYSTEM_ACCESS_TOKEN, token).apply();
    }

    public String getEdgeSystemToken() {
        return mPref.getString(EDGE_SYSTEM_ACCESS_TOKEN,"");
    }

    public boolean isFitBitConnected() {
        return mPref.getBoolean(FIT_BIT_CONNECTION_STATUS,false);
    }

    public void setFitBitConnected(boolean isConnected) {
        mPref.edit().putBoolean(FIT_BIT_CONNECTION_STATUS, isConnected).apply();
    }

    public void saveFitBitUserId(String user_id) {
        mPref.edit().putString(FIT_BIT_USER_ID, user_id).apply();
    }

    public String getFitBitUserId() {
        return mPref.getString(FIT_BIT_USER_ID,"");
    }

    private void savePermissionGrantedFitBitScopes(String scopes) {
        mPref.edit().putString(FIT_BIT_SCOPE_PERMISSION, scopes).apply();
    }

    public String getPermissionGrantedFitBitScopes() {
        return mPref.getString(FIT_BIT_SCOPE_PERMISSION,"");
    }

    public void saveFitBitToken(FitBitTokenResponse fitBitTokenResponse) {
        saveFitBitAccessToken(fitBitTokenResponse.getAccess_token());
        saveFitBitRefreshToken(fitBitTokenResponse.getRefresh_token());
        saveFitBitUserId(fitBitTokenResponse.getUser_id());
        savePermissionGrantedFitBitScopes(fitBitTokenResponse.getScope());
        setFitBitConnected(true);
    }


    public boolean isFirstLaunch() {
        return mPref.getBoolean(APPLICATION_FIRST_LAUNCHING,true);
    }

    public void setIsFirstLaunch(boolean b) {
        mPref.edit().putBoolean(APPLICATION_FIRST_LAUNCHING, b).apply();
    }

    public void deleteFitBitToken() {
        saveFitBitAccessToken("");
        saveFitBitRefreshToken("");
    }
}
