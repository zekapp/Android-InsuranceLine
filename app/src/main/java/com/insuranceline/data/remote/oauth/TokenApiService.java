package com.insuranceline.data.remote.oauth;

import com.insuranceline.data.remote.responses.RefreshTokenResponse;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * Created by Zeki Guler on 02,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public interface TokenApiService {
    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www.form-urlencoded",
            "clientId: 31159E2C-1DA9-4F94-B279-A3259A74F127"
    })
    @FormUrlEncoded
    @POST
    RefreshTokenResponse refreshToken(@Field("grant_type") String grandType, @Field("refresh_token") String oldRefreshToken);
}
