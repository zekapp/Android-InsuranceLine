package com.insuranceline.data.remote.oauth;

import com.insuranceline.data.remote.responses.FitBitTokenResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Zeki Guler on 02,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public interface FitBitOauthApiService {

    // todo: get app secret from appConfig
    @Headers({
            "Authorization: Basic MjI3RkdOOmMxODc2NTNjY2U2MGY0NjU4MWVlYmUwZDVmMTE4NjVi"
    })
    @FormUrlEncoded
    @POST("oauth2/token")
    Call<FitBitTokenResponse> refreshToken(@Field("grant_type")    String grandType,
                                           @Field("refresh_token") String oldRefreshToken);

}
