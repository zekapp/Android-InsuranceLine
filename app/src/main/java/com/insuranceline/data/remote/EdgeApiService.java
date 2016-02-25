package com.insuranceline.data.remote;

import com.insuranceline.data.remote.responses.EdgeAuthResponse;
import com.insuranceline.data.remote.responses.EdgePayResponse;
import com.insuranceline.data.remote.responses.EdgeShoppingCardResponse;
import com.insuranceline.data.remote.responses.EdgeWhoAmIResponse;
import com.insuranceline.data.vo.EdgeShoppingCart;
import com.insuranceline.data.vo.Pay;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import rx.Observable;

/**
 * Created by Zeki Guler on 25,February,2016
 * ©2015 Appscore. All Rights Reserved
 */
public interface EdgeApiService {


    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www.form-urlencoded",
            "clientId: this value changed by OauthInterceptrEdge accordign to te stage/pro server"
    })
    @FormUrlEncoded
    @POST("auth")
    Observable<EdgeAuthResponse> getAuthToken(@Field("username") String userName, @Field("password") String password, @Field("grant_type") String type);


/*    @GET("api/v1/site")
    Observable<MSiteHelper> getSite(@Header("Authorization") String token); // do not forget to add Bearer to Token*/

    @GET("api/v1/whoami")
    Observable<EdgeWhoAmIResponse> whoami(); // do not forget to add Bearer to Token

    @PUT("api/v1/Membership")
    Observable<EdgeWhoAmIResponse> putWhoAmI(@Body EdgeWhoAmIResponse whoAmI);

    @POST("api/v1/ShoppingCart")
    Observable<EdgeShoppingCardResponse> claimReward(@Body EdgeShoppingCart edgeShoppingCart);

    @POST("api/v1/Pay")
    Observable<EdgePayResponse> pay(@Body Pay payment);


}
