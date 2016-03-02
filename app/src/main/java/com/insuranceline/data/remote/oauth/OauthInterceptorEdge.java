package com.insuranceline.data.remote.oauth;

import android.content.Context;

import com.insuranceline.R;
import com.insuranceline.data.local.PreferencesHelper;
import com.insuranceline.di.qualifier.ApplicationContext;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Zeki Guler on 25,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
@Singleton
public final class OauthInterceptorEdge implements Interceptor {

    private final Context mContext;
    private PreferencesHelper preferencesHelper;

    @Inject
    public OauthInterceptorEdge(@ApplicationContext Context context, PreferencesHelper preferencesHelper) {
        mContext = context;
        this.preferencesHelper = preferencesHelper;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request request;
        if(original.url().toString().contains("auth")){

//            if (preferencesHelper.isLoginAttemptForFitBitUser()){
                request = original.newBuilder()
                        .header("clientId", mContext.getString(R.string.edge_app_client_id))
                        .method(original.method(), original.body())
                        .build();
/*            }else{
                request = original.newBuilder()
                        .header("clientId", mContext.getString(R.string.edge_app_client_id_lumo))
                        .method(original.method(), original.body())
                        .build();
            }*/

        } else{
            request = original.newBuilder()
                    .header("Authorization", "Bearer " + preferencesHelper.getEdgeSystemToken())
                    .method(original.method(), original.body())
                    .build();
        }



        return chain.proceed(request);
    }
}
