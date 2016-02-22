package com.insuranceline.config;

import android.annotation.SuppressLint;

import com.insuranceline.data.local.PreferencesHelper;
import com.insuranceline.utils.TimeUtils;

import org.apache.commons.codec.binary.Base64;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

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
    private static final String END_OF_CAMPAIGN_DATE    = "01 09 2016 11:59 pm";

    public static final long BOOST_NOTIFICATION_PERIOD_DAYS = 21; // day
    public static final long REMINDER_NOTIFICATION_PERIOD_DAYS = 14; // day

    public static final String STAGING_APP_ID           = "929da5ad-2b68-4493-8a3d-1466a8792e00";
    public static final String PRODUCTION_APP_ID        = "5e435d08-3537-4e50-ad41-05bfbdbf0bfb";
    public static final int INITILA_TARGET_STEP_COUNT   = 100;


    private static long BOOM_END;

    public static final String FITBIT_PACKAGE_NAME =  "com.fitbit.FitbitMobile";

    private final PreferencesHelper mSharedPreferences;
    private String mEncodedAuthorizationHeader = "";

    static {
        try {
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat f = new SimpleDateFormat("dd MM yyyy HH:mm a", Locale.US);
            Date d = f.parse(END_OF_CAMPAIGN_DATE);
            BOOM_END = d.getTime();
        } catch (ParseException e) {
            throw new RuntimeException("Wrong time format");
        }
    }


    @Inject
    public AppConfig(PreferencesHelper preferencesHelper) {
        mSharedPreferences = preferencesHelper;
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

    public long getEndOfCampaign() {
        long endOfCampagn = mSharedPreferences.getEndOfCampaignDate(BOOM_END); // default return BOOM_END
        Timber.d("End Date: unix:%s readable:%s",endOfCampagn, TimeUtils.convertReadableDate(endOfCampagn, TimeUtils.DATE_FORMAT_TYPE_5));
        return endOfCampagn;
    }
}
