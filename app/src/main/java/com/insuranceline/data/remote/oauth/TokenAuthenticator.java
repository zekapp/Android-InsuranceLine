package com.insuranceline.data.remote.oauth;

import com.insuranceline.data.remote.responses.FitBitTokenResponse;
import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.Proxy;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

/**
 * Created by Zeki Guler on 02,February,2016
 * ©2015 Appscore. All Rights Reserved
 *
 * http://stackoverflow.com/questions/22450036/refreshing-oauth-token-using-retrofit-without-modifying-all-calls
 */
@Singleton
public class TokenAuthenticator implements Authenticator{

    private final TokenManager mTokenManager;

    @Inject
    public TokenAuthenticator(TokenManager tokenManager){
        this.mTokenManager = tokenManager;
    }

    @Override
    public Request authenticate(Proxy proxy, Response response) throws IOException {
        Timber.d("authenticate called");
        // Refresh the access_token using a synchronous api request.
        try {
            FitBitTokenResponse newAccessToken = mTokenManager.refreshToken();

            return response.request().newBuilder()
                    .header("Authorization", newAccessToken.getToken_type() + " "+ newAccessToken.getAccess_token())
                    .build();

        }catch (Exception e){
            Timber.e("Error: %s", e.getMessage());
            return null;
        }
    }

    @Override
    public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
        return null;
    }
}
