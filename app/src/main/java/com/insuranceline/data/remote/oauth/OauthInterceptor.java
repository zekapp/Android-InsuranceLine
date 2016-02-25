package com.insuranceline.data.remote.oauth;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

@Singleton
public final class OauthInterceptor implements Interceptor {
  private final TokenManager mTokenManager;

  @Inject
  public OauthInterceptor( TokenManager tokenManager) {
    this.mTokenManager = tokenManager;
  }

  @Override public Response intercept(Chain chain) throws IOException {
    Request.Builder builder = chain.request().newBuilder();
    Timber.d("OauthInterceptor called");
    if (mTokenManager.isTokenSet()) {
      Timber.d("OauthInterceptor called and header changed");
      builder.header("Authorization", "Bearer " + mTokenManager.getAccessToken());
    }

    return chain.proceed(builder.build());
  }
}
