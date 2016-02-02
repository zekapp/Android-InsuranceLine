package com.insuranceline.data.remote.oauth;

import com.insuranceline.data.local.PreferencesHelper;
import com.insuranceline.data.remote.responses.RefreshTokenResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

import kotlin.jvm.Throws;

/**
 * Created by Zeki Guler on 02,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
@Singleton
public class TokenManager {

    private final TokenApiService mTokenApiService;
    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public TokenManager(TokenApiService tokenApiService, PreferencesHelper preferencesHelper){
        this.mTokenApiService = tokenApiService;
        this.mPreferencesHelper = preferencesHelper;
    }

    @Throws(exceptionClasses = Exception.class)
    public RefreshTokenResponse refreshToken() {
        String refreshToken = mPreferencesHelper.getFitBitRefreshToken();

        RefreshTokenResponse refreshTokenResponse = mTokenApiService.refreshToken("refresh_token", refreshToken);

        // save refresh_token
        mPreferencesHelper.saveFitBitRefreshToken(refreshTokenResponse.getRefresh_token());
        // save access_token
        mPreferencesHelper.saveFitBitAccessToken(refreshTokenResponse.getAccess_token());

        return refreshTokenResponse;
    }
}
