package com.insuranceline.data.remote;

import com.insuranceline.data.remote.responses.DailySummaryResponse;
import com.insuranceline.data.remote.responses.FitBitTokenResponse;
import com.insuranceline.data.remote.responses.StepsCountResponse;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Zeki Guler on 03,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public interface FitBitApiService {

    // todo get header info from AppConfig.
    @Headers({
            "Authorization: Basic MjI3RkdOOmMxODc2NTNjY2U2MGY0NjU4MWVlYmUwZDVmMTE4NjVi"
    })
    @FormUrlEncoded
    @POST("oauth2/token")
    Observable<FitBitTokenResponse> accessTokenRequest(@Field("client_id") String clientId,
                                                       @Field("redirect_uri") String redirectUri,
                                                       @Field("code") String code,
                                                       @Field("grant_type") String grantType);

    @GET("1/user/{userId}/profile.json")
    Observable<String> getProfile(@Path("userId") String userId);

    @GET("1/user/-/activities/date/today.json")
    Observable<DailySummaryResponse> getDailySummary();

    @GET("1/user/-/activities/steps/date/{base-date}/{end-date}.json")
    Observable<StepsCountResponse> getStepsCountsBetweenDates(@Path("base-date") String startDate,
                                                              @Path("end-date") String endDate);
}
