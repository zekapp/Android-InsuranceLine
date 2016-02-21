package com.insuranceline.event;

/**
 * Created by Zeki Guler on 12,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class LogOutFromFitBitEvent {
    private String logoutReason;

    public LogOutFromFitBitEvent(String logoutReason){

        this.logoutReason = logoutReason;
    }

    public String getLogoutReason() {
        return logoutReason;
    }

    public void setLogoutReason(String logoutReason) {
        this.logoutReason = logoutReason;
    }
}
