package com.insuranceline.event;

/**
 * Created by Zeki Guler on 23,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class FitBitLogoutEvent {
    private String logoutReason;

    public FitBitLogoutEvent(String logoutReason){

        this.logoutReason = logoutReason;
    }

    public String getLogoutReason() {
        return logoutReason;
    }

    public void setLogoutReason(String logoutReason) {
        this.logoutReason = logoutReason;
    }
}
