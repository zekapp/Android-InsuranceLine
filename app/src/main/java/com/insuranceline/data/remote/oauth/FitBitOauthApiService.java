package com.insuranceline.data.remote.oauth;

import com.insuranceline.data.remote.responses.FitBitTokenResponse;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Headers;
import retrofit.http.POST;
import rx.Observable;

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
