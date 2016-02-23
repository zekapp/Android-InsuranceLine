package com.insuranceline.di.component;

import android.app.AlarmManager;

import com.insuranceline.config.AppConfig;
import com.insuranceline.controller.AlarmIntentService;
import com.insuranceline.data.DataManager;
import com.insuranceline.data.job.fetch.FetchSamplesJob;
import com.insuranceline.data.local.PreferencesHelper;
import com.insuranceline.data.remote.oauth.TokenAuthenticator;
import com.insuranceline.di.module.AppModule;
import com.insuranceline.receiver.NotificationHelper;

import javax.inject.Singleton;

import au.com.lumo.ameego.LumoController;
import dagger.Component;
import de.greenrobot.event.EventBus;

/**
 * Created by Zeki Guler on 18,January,2016
 * ©2015 Appscore. All Rights Reserved
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    PreferencesHelper preferencesHelper();
    DataManager getDataManager();
    TokenAuthenticator getTokenAuthenticator();
    AppConfig getAppConfig();
    EventBus getEventBus();
    LumoController getLumocontroller();
    AlarmManager getAlarmManager();
    NotificationHelper getNotificationHelper();

    void inject(FetchSamplesJob getFetchSamplesJob);
    void inject(AlarmIntentService getAlarmIntentService);
}
