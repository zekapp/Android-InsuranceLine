package com.insuranceline.data.remote;

import com.insuranceline.data.remote.responses.SampleResponseData;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by zeki on 17/01/2016.
 */
public interface ApiService {

    @GET("samples")
    Observable<SampleResponseData> getSamples(@Query("page") int page, @Query("per_page") int perPage);
}
