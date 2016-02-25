package com.insuranceline.config;

import android.content.Context;
import android.content.Intent;

import com.insuranceline.R;
import com.insuranceline.data.local.PreferencesHelper;
import com.insuranceline.di.qualifier.ApplicationContext;
import com.insuranceline.utils.TimeUtils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

/**
 * Created by Zeki Guler on 03,February,2016
 * ©2015 Appscore. All Rights Reserved
 */

@Singleton
public class AppConfig {
    /**
     * todo:
     *
     * - create Fitbit application for staging
     * - change app sectrect (not neccessary for Impicit Login)
     * - change callback url on fitbit
     * - change staging server url for edge loyalty
     * - change staging edge PRODUCTION_APP_ID fot staging server
     *
     * */

    private static final String KEY_API_URL = "api_url";
    private static final int PASSWORD_LENGHT = 6;

    private static final String FIT_BIT_CLIENT_ID        = "227FGN";
    private static final String FIT_BIT_CLIENT_SECRET    = "c187653cce60f46581eebe0d5f11865b";
    private static final String FIT_BIT_REDIRECT_URI     = "apitester://logincallback"; // DO NOT FORGET TO CHANGE ACTIVITY INTENT FILTER IF YOU CHANGE THIS


    private static final String FIT_BIT_WEB_URL         = "https://www.fitbit.com/oauth2/";

    /** CAMPAIGN END DATE ==> 01 SEPTEMBER 2016 23:59:59 **/
    private static final int CAMPAIGN_END_YEAR          = 2016;
    private static final int CAMPAIGN_END_MONTH         = Calendar.SEPTEMBER;
    private static final int CAMPAIGN_END_DAY_OF_MONTH  = 1;
    private static final int CAMPAIGN_END_HOUR_OF_DAY   = 23;
    private static final int CAMPAIGN_END_MINUTE        = 59;
    private static final int CAMPAIGN_END_SECOND        = 59;


    public static final long BOOST_NOTIFICATION_PERIOD_DAYS = 21; // day
    public static final long REMINDER_NOTIFICATION_PERIOD_DAYS = 14; // day


/*    private static final String EDGE_PRODUCTION_BASE_URL = "https://api.lifestylerewards.com.au/";
    private static final String EDGE_STAGE_BASE_URL      = "https://api.lifestylerewards.com.au/";

    public static final String STAGING_APP_ID           = "929da5ad-2b68-4493-8a3d-1466a8792e00";
    public static final String PRODUCTION_APP_ID        = "5e435d08-3537-4e50-ad41-05bfbdbf0bfb";*/

    public static final int INITIALS_TARGET_STEP_COUNT  = 100000;

/*//    public static final String[] SKU = {
//            "DG4DIB1CV40A",     //  National Adult Restricted eVoucher TAL:
//            "WMKO6PC3KKK6",     // New Balance
//            "5AL7T6R5JK7Q"      // Good Health Magazine
//    };*/

    private static long BOOM_END;

    public static final String FITBIT_PACKAGE_NAME =  "com.fitbit.FitbitMobile";

    private final PreferencesHelper mSharedPreferences;
    private Context mContext;
    private String mEncodedAuthorizationHeader = "";


    static {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        cal.set(Calendar.YEAR,          CAMPAIGN_END_YEAR);
        cal.set(Calendar.MONTH,         CAMPAIGN_END_MONTH);
        cal.set(Calendar.DAY_OF_MONTH,  CAMPAIGN_END_DAY_OF_MONTH);
        cal.set(Calendar.HOUR_OF_DAY,   CAMPAIGN_END_HOUR_OF_DAY);
        cal.set(Calendar.MINUTE,        CAMPAIGN_END_MINUTE);
        cal.set(Calendar.SECOND,        CAMPAIGN_END_SECOND);

        BOOM_END = cal.getTimeInMillis();
    }


    @Inject
    public AppConfig(PreferencesHelper preferencesHelper, @ApplicationContext Context context) {
        mSharedPreferences = preferencesHelper;
        mContext = context;
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
        return mContext.getString(R.string.server_uri_edge);
    }

    public String getFitBitBaseUrl() {
        return mContext.getString(R.string.server_uri_fitbit);
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
                "response_type=token" + /*"response_type=code" +*/
                "&client_id=" + FIT_BIT_CLIENT_ID +
                "&scope=activity profile heartrate location nutrition settings social sleep weight" +
                "&prompt=login" +
                "&expires_in=" + "31536000" +
                "&redirect_uri=" + FIT_BIT_REDIRECT_URI;
    }

    public long getEndOfCampaign() {
        long endOfCampaignDate = mSharedPreferences.getEndOfCampaignDate(BOOM_END); // default return BOOM_END
        Timber.d("End Date: unix:%s readable: %s",endOfCampaignDate, TimeUtils.convertReadableDate(endOfCampaignDate, TimeUtils.DATE_FORMAT_TYPE_1));
        return endOfCampaignDate;
    }

    public  int getStockItemId(int i) {
        if (i == 0)
            return Integer.valueOf(mContext.getString(R.string.stockItemId1));
        else if( i == 1)
            return Integer.valueOf(mContext.getString(R.string.stockItemId2));
        else
            return Integer.valueOf(mContext.getString(R.string.stockItemId3));
    }

    public boolean isFitBitUser(String appId) {
        return appId.equals(mContext.getString(R.string.edge_app_id));
    }
}
