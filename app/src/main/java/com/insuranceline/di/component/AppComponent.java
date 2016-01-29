package com.insuranceline.di.component;

import com.insuranceline.config.AppConfig;
import com.insuranceline.data.DataManager;
import com.insuranceline.data.job.fetch.FetchSamplesJob;
import com.insuranceline.di.module.AppModule;

import javax.inject.Singleton;
import dagger.Component;
/**
 * Created by Zeki Guler on 18,January,2016
 * ©2015 Appscore. All Rights Reserved
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    DataManager getDataManager();

    AppConfig getAppConfig();

    void inject(FetchSamplesJob getFetchSamplesJob);
}
