package com.insuranceline;

import android.app.Application;
import android.content.Context;

import com.insuranceline.di.component.AppComponent;
import com.insuranceline.di.component.DaggerAppComponent;
import com.insuranceline.di.module.AppModule;
import com.facebook.stetho.Stetho;
import com.raizlabs.android.dbflow.config.FlowManager;

import timber.log.Timber;

/**
 * Created by Zeki Guler on 18,January,2016
 * ©2015 Appscore. All Rights Reserved
 */
public class App extends Application{

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        FlowManager.init(this);

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
            Timber.plant(new Timber.DebugTree());
        }

        getComponent();
    }

    public static App get(Context context) {
        return (App) (context.getApplicationContext());
    }

    public AppComponent getComponent() {
        if (mAppComponent == null) {
            mAppComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(this))
                    .build();
        }
        return mAppComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(AppComponent applicationComponent) {
        mAppComponent = applicationComponent;
    }
}
