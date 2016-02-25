package com.insuranceline.data.remote;

import com.insuranceline.data.remote.responses.SampleResponseData;
import com.insuranceline.data.remote.responses.TermCondResponse;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
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
