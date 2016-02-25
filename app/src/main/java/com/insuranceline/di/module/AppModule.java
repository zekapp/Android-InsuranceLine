package com.insuranceline.di.module;


import android.app.AlarmManager;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insuranceline.App;
import com.insuranceline.config.AppConfig;
import com.insuranceline.controller.ModuleController;
import com.insuranceline.data.job.BaseJob;
import com.insuranceline.data.local.AppDatabase;
import com.insuranceline.data.remote.ApiService;
import com.insuranceline.data.remote.EdgeApiService;
import com.insuranceline.data.remote.FitBitApiService;
import com.insuranceline.data.remote.oauth.FitBitOauthApiService;
import com.insuranceline.data.remote.oauth.OauthInterceptor;
import com.insuranceline.data.remote.oauth.OauthInterceptorEdge;
import com.insuranceline.data.remote.oauth.TokenAuthenticator;
import com.insuranceline.di.qualifier.ApplicationContext;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;
import com.path.android.jobqueue.di.DependencyInjector;
import com.raizlabs.android.dbflow.config.FlowManager;

import javax.inject.Singleton;

import au.com.lumo.ameego.LumoController;
import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;


/**
 * Created by Zeki Guler on 08,February,2016
 * ©2015 Appscore. All Rights Reserved
 */

@Module
public class AppModule {
    protected final App mApp;

    public AppModule(App application) {
        mApp = application;
    }

    @Provides
    Application provideApplication() {
        return mApp;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApp;
    }

    @Provides
    @Singleton
    public ApiService apiService(AppConfig appConfig) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient().newBuilder()
        .addInterceptor(logging)
        .addNetworkInterceptor(new StethoInterceptor())
        .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(appConfig.getApiUrl())
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient)
                .build();

        return retrofit.create(ApiService.class);
    }

    @Provides
    @Singleton
    public EdgeApiService edgeApiService(AppConfig appConfig, OauthInterceptorEdge oauthInterceptorEdge) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient().newBuilder()
                .addInterceptor(logging)
                .addInterceptor(oauthInterceptorEdge)
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(appConfig.getEdgeSystemBaseUrl())
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient)
                .build();

        return retrofit.create(EdgeApiService.class);
    }

    @Provides
    @Singleton
    public FitBitApiService fitBitApiService(AppConfig appConfig,
                                             TokenAuthenticator tokenAuthenticator,
                                             OauthInterceptor oauthInterceptor) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient().newBuilder()
                .addInterceptor(logging)
                .addInterceptor(oauthInterceptor)
                .authenticator(tokenAuthenticator)
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        //http://stackoverflow.com/questions/33266886/networkonmainthread-rxjava-retrofit-lollipop
        /*httpClient.setProtocols(Collections.singletonList(Protocol.HTTP_1_1));*/
//        httpClient.setConnectTimeout(25, TimeUnit.SECONDS);
//        httpClient.setConnectTimeout(10, TimeUnit.SECONDS);
//        httpClient.setReadTimeout(30, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(appConfig.getFitBitBaseUrl())
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient)
                .build();

        return retrofit.create(FitBitApiService.class);
    }

    @Provides
    @Singleton
    public FitBitOauthApiService fitBitAuthApiService(AppConfig appConfig) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient().newBuilder()
                .addInterceptor(logging)
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(appConfig.getFitBitBaseUrl())
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient)
                .build();

        return retrofit.create(FitBitOauthApiService.class);
    }

    @Provides
    @Singleton
    public SQLiteDatabase database() {
        return FlowManager.getDatabase(AppDatabase.NAME).getWritableDatabase();
    }

    @Provides
    @Singleton
    public LumoController lumoController(@ApplicationContext Context context, ModuleController moduleController){
        return new LumoController
                .Builder(context)
                .setCallback(moduleController)
                .build();
    }

    @Provides
    @Singleton
    public JobManager jobManager() {
        Configuration config = new Configuration.Builder(mApp)
                .consumerKeepAlive(45)
                .maxConsumerCount(3)
                .minConsumerCount(1)
                .injector(new DependencyInjector() {
                    @Override
                    public void inject(Job job) {
                        if (job instanceof BaseJob) {
                            ((BaseJob) job).inject(mApp.getComponent());
                        }
                    }
                })
                .build();
        return new JobManager(mApp, config);
    }

    @Provides
    @Singleton
    public EventBus eventBus() {
        return new EventBus();
    }

    @Provides
    public AlarmManager alarmManager(@ApplicationContext Context context){
        return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }
}
