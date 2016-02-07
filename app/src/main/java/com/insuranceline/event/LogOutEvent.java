package com.insuranceline.event;

/**
 * Created by zeki on 7/02/2016.
 */
public class LogOutEvent {
    private String logoutReason;

    public LogOutEvent(String logoutReason){

        this.logoutReason = logoutReason;
    }

    public String getLogoutReason() {
        return logoutReason;
    }

    public void setLogoutReason(String logoutReason) {
        this.logoutReason = logoutReason;
    }
}
