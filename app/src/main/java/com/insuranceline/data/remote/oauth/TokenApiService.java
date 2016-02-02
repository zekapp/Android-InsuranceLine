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
            "Authorization: Basic MjI3RkdOOmMxODc2NTNjY2U2MGY0NjU4MWVlYmUwZDVmMTE4NjVi",
            "Content-Type: application/x-www.form-urlencoded"
    })
    @FormUrlEncoded
    @POST
    RefreshTokenResponse refreshToken(@Field("grant_type") String grandType, @Field("refresh_token") String oldRefreshToken);
}
