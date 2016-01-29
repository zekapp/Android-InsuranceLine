package com.insuranceline.di.module;

import android.app.Activity;
import android.content.Context;

import com.insuranceline.di.qualifier.ActivityContext;

import dagger.Module;
import dagger.Provides;

/**
 * Created by zeki on 17/01/2016.
 */
@Module
public class ActivityModule {

    private Activity mActivity;

    public ActivityModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    Activity provideActivity() {
        return mActivity;
    }

    @Provides
    @ActivityContext
    Context providesContext() {
        return mActivity;
    }

}
