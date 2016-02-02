package com.insuranceline.data.remote;

import com.insuranceline.data.remote.responses.RefreshTokenResponse;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * Created by zeki on 30/01/2016.
 */
public interface FitBitApiService {
    @Headers({
            "Authorization: Basic MjI3RkdOOmMxODc2NTNjY2U2MGY0NjU4MWVlYmUwZDVmMTE4NjVi",
            "Content-Type: application/x-www.form-urlencoded"
    })
    @FormUrlEncoded
    @POST
    RefreshTokenResponse refreshToken(@Field("grant_type") String grandType, @Field("refresh_token") String oldRefreshToken);
}
