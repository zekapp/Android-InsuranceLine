package com.insuranceline.event;

/**
 * Created by zeki on 22/02/2016.
 */
public class LogOutFromEdgeEvent {
    private String logoutReason;

    public LogOutFromEdgeEvent(String logoutReason){

        this.logoutReason = logoutReason;
    }

    public String getLogoutReason() {
        return logoutReason;
    }

    public void setLogoutReason(String logoutReason) {
        this.logoutReason = logoutReason;
    }
}
