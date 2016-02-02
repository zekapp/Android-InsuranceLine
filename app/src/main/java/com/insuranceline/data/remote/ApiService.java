package com.insuranceline.data.remote;

import com.insuranceline.data.remote.responses.SampleResponseData;
import com.insuranceline.data.remote.responses.TermCondResponse;

import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by zeki on 17/01/2016.
 */
public interface ApiService {

    @GET("samples")
    Observable<SampleResponseData> getSamples(@Query("page") int page, @Query("per_page") int perPage);

    @POST("termsAndCondition")
    Observable<TermCondResponse> tcResponse(@Header("Authorization") String token);
}
