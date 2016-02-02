package com.insuranceline.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.insuranceline.di.qualifier.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PreferencesHelper {

    public static final String PREF_FILE_NAME = "android_arhitecture_pref_file";
    private static final String FIT_BIT_REFRESH_TOKEN = "FIT_BIT_REFRESH_TOKEN";
    private static final String FIT_BIT_ACCESS_TOKEN = "FIT_BIT_ACCESS_TOKEN";
    private static final String EDGE_SYSTEM_ACCESS_TOKEN = "EDGE_SYSTEM_ACCESS_TOKEN";

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
}
