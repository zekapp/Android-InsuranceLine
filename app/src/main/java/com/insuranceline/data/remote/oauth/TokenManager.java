package com.insuranceline.data.remote.oauth;

import com.insuranceline.data.job.NetworkException;
import com.insuranceline.data.local.PreferencesHelper;
import com.insuranceline.data.remote.responses.FitBitTokenResponse;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import kotlin.jvm.Throws;
import retrofit.Response;
import timber.log.Timber;

/**
 * Created by Zeki Guler on 02,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
@Singleton
public class TokenManager {

    private final FitBitOauthApiService mFitBitOauthApiService;
    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public TokenManager(FitBitOauthApiService fitBitOauthApiService, PreferencesHelper preferencesHelper){
        this.mFitBitOauthApiService = fitBitOauthApiService;
        this.mPreferencesHelper = preferencesHelper;
    }

    @Throws(exceptionClasses = Exception.class)
    public FitBitTokenResponse refreshToken() throws IOException {
        String oldRefreshToken = mPreferencesHelper.getFitBitRefreshToken();

        Response<FitBitTokenResponse> response = mFitBitOauthApiService.refreshToken("refresh_token", oldRefreshToken)
                .execute();

        if (response.isSuccess()) {
            FitBitTokenResponse fitBitTokenResponse = response.body();

            // save refresh_token
            mPreferencesHelper.saveFitBitRefreshToken(fitBitTokenResponse.getRefresh_token());
            // save access_token
            mPreferencesHelper.saveFitBitAccessToken(fitBitTokenResponse.getAccess_token());

            return fitBitTokenResponse;
        } else{
            Timber.e("Error on response: %s", response.errorBody());
            throw new NetworkException(response.code());
        }

    }

    public boolean isTokenSet() {
        return !mPreferencesHelper.getFitBitAccessToken().isEmpty();
    }

    public String getAccessToken() {
        return mPreferencesHelper.getFitBitAccessToken();
    }
}
