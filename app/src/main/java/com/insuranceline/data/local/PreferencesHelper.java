package com.insuranceline.data.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.insuranceline.config.AppConfig;
import com.insuranceline.data.remote.responses.FitBitTokenResponse;
import com.insuranceline.di.qualifier.ApplicationContext;

import java.util.concurrent.TimeUnit;

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
    private static final String BOOST_NOTIFICATION_PERIOD = "BOOST_NOTIFICATION_PERIOD";
    private static final String BOOST_NOTIFICATION_BASE_TIME = "BOOST_NOTIFICATION_BASE_TIME";
    private static final String REMINDER_NOTIFICATION_PERIOD = "REMINDER_NOTIFICATION_PERIOD";
    private static final String REMINDER_NOTIFICATION_BASE_TIME = "REMINDER_NOTIFICATION_BASE_TIME";
    private static final String END_OF_CAMPAIGN_DATE = "END_OF_CAMPAIGN_DATE";
    private static final String USER_TYPE_AS_FIT_BIT = "USER_TYPE_AS_FIT_BIT";
    private static final String LOGIN_USER_NAME_OR_EMAIL = "LOGIN_USER_NAME_OR_EMAIL";
    private static final String USER_PASSWORD = "USER_PASSWORD";
    private static final String EDGE_T_AND_C_ACCEPTED = "EDGE_T_AND_C_ACCEPTED";

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
        setFitBitConnected(false);
    }

    public long getBoostNotificationPeriod() {
        return mPref.getLong(BOOST_NOTIFICATION_PERIOD, TimeUnit.DAYS.toMillis(AppConfig.BOOST_NOTIFICATION_PERIOD_DAYS));
    }

    public long getBaseTimeOfBoostNotification() {
        return mPref.getLong(BOOST_NOTIFICATION_BASE_TIME,System.currentTimeMillis());
    }

    public void saveBaseTimeOfBoostNotification(long baseTime) {
        mPref.edit().putLong(BOOST_NOTIFICATION_BASE_TIME, baseTime).apply();
    }

    public long getReminderNotificationPeriod() {
        return mPref.getLong(REMINDER_NOTIFICATION_PERIOD, TimeUnit.DAYS.toMillis(AppConfig.REMINDER_NOTIFICATION_PERIOD_DAYS));
    }

    public long getBaseTimeOfReminderNotification() {
        return mPref.getLong(REMINDER_NOTIFICATION_BASE_TIME, System.currentTimeMillis());
    }

    public void saveBaseTimeOfReminderNotification(long baseTime) {
        mPref.edit().putLong(REMINDER_NOTIFICATION_BASE_TIME, baseTime).apply();
    }

    public long getEndOfCampaignDate(long defaultCampaignEndDate) {
        return mPref.getLong(END_OF_CAMPAIGN_DATE, defaultCampaignEndDate);
    }

    public String getPassword() {
        return mPref.getString(USER_PASSWORD, "");
    }

    public void setEdgeTandCAccepted(boolean isAccepted) {
        mPref.edit().putBoolean(EDGE_T_AND_C_ACCEPTED, isAccepted).apply();
    }

    public boolean isEdgeTandCAccepted() {
        return mPref.getBoolean(EDGE_T_AND_C_ACCEPTED, false);
    }

    /***** TEST PURPOSES FUNCTION **** */

    // This function is only test purposes
    public void saveBoostNotificationPeriod(int min){
        mPref.edit().putLong(BOOST_NOTIFICATION_PERIOD, TimeUnit.MINUTES.toMillis(min)).apply();
    }

    // This function is only test purposes
    public void saveReminderNotificationPeriod(int min){
        mPref.edit().putLong(REMINDER_NOTIFICATION_PERIOD, TimeUnit.MINUTES.toMillis(min)).commit();
    }

    // This function is only test purposes. User always default value.
    public void saveEndOfCampaignDate(long boomEnd) {
        mPref.edit().putLong(END_OF_CAMPAIGN_DATE, boomEnd).commit();
    }

    // This function is only test purposes. User always default value.
    public void setUserAsFitBit(boolean isFitBitUser) {
        mPref.edit().putBoolean(USER_TYPE_AS_FIT_BIT, isFitBitUser).commit();
    }

    // This function is only test purposes. User always default value.
    public boolean isUseFitBitOwner() {
        return mPref.getBoolean(USER_TYPE_AS_FIT_BIT, true);
    }

    public void saveUserLoginEmail(String email) {
        mPref.edit().putString(LOGIN_USER_NAME_OR_EMAIL, email).commit();
    }

    public String getUserLoginEmail() {
        return mPref.getString(LOGIN_USER_NAME_OR_EMAIL, "");
    }

    public void savePassword(String password) {
        mPref.edit().putString(USER_PASSWORD, password).commit();
    }

}
