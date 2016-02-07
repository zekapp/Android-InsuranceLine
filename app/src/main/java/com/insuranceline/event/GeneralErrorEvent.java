package com.insuranceline.event;

/**
 * Created by zeki on 7/02/2016.
 */
public class GeneralErrorEvent {
    private Throwable throwable;

    public GeneralErrorEvent(Throwable throwable) {
        this.throwable = throwable;
    }

    public Throwable getThrowable() {
        return throwable;
    }
}
