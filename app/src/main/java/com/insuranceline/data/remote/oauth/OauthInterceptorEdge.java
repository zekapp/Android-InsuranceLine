package com.insuranceline.data.remote.oauth;

import android.content.Context;

import com.insuranceline.R;
import com.insuranceline.di.qualifier.ApplicationContext;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.http.Headers;
import timber.log.Timber;

/**
 * Created by Zeki Guler on 25,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
@Singleton
public final class OauthInterceptorEdge implements Interceptor {

    private Context mContext;

    @Inject
    public OauthInterceptorEdge(@ApplicationContext Context context) {

        mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Timber.d("Url: %s, ", original.urlString());
        Timber.d("httpUrl: %s, ", original.httpUrl().encodedFragment());

        Request request;
        if (original.urlString().contains("auth")){
            request = original.newBuilder()
                   .header("Accept", "application/json")
                   .header("Content-Type", "application/x-www.form-urlencoded")
                   .header("clientId",      mContext.getString(R.string.edge_app_client_id))
                   .method(original.method(), original.body())
                   .build();

        }else{
            request = original.newBuilder()
                    .method(original.method(), original.body()).build();
        }

        return chain.proceed(request);
    }
}
