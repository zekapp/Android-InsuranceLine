package com.insuranceline.data.remote;

import com.insuranceline.data.remote.responses.ClaimRewardResponse;
import com.insuranceline.data.remote.responses.EdgeResponse;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by zeki on 30/01/2016.
 */
public interface EdgeApiService {

    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www.form-urlencoded",
            "clientId: 31159E2C-1DA9-4F94-B279-A3259A74F127"
    })
    @FormUrlEncoded
    @POST("auth")
    Observable<EdgeResponse> loginToEdgeSystem(@Field("username") String userName, @Field("password") String password, @Field("grant_type") String type);


    @POST("accepted")
    Observable<Boolean> tcResponse(@Header("Authorization") String token);

    @POST("claimReward")
    Observable<ClaimRewardResponse> submitEmail(@Query("email") String email, @Query("rewardId") String id);
}
