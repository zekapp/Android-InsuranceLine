package com.insuranceline.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.insuranceline.di.qualifier.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by zeki on 17/01/2016.
 */

@Singleton
public class AppConfig {

    private static final String KEY_API_URL = "api_url";

    private final SharedPreferences mSharedPreferences;

    @Inject
    public AppConfig(@ApplicationContext Context context) {
        mSharedPreferences = context.getSharedPreferences("app_cfg", Context.MODE_PRIVATE);
    }

    public String getApiUrl() {
        return "http://private-f7ff9-androidarchitecturetestapi.apiary-mock.com/api/v1/";
//        return mSharedPreferences.getString(KEY_API_URL, "http://private-f7ff9-androidarchitecturetestapi.apiary-mock.com/api/v1/");
    }
}
