package com.insuranceline.data.remote;

import com.insuranceline.data.remote.responses.EdgeAuthResponse;
import com.insuranceline.data.remote.responses.EdgePayResponse;
import com.insuranceline.data.remote.responses.EdgeShoppingCardResponse;
import com.insuranceline.data.remote.responses.EdgeWhoAmIResponse;
import com.insuranceline.data.vo.EdgeShoppingCart;
import com.insuranceline.data.vo.Pay;

import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Headers;
import retrofit.http.POST;
import rx.Observable;

/**
 * Created by zeki on 30/01/2016.
 */
public interface EdgeApiService {

    @Headers({
            "Accept: application/json",
            "Content-Type: application/x-www.form-urlencoded",
            "clientId: 59b9b30a-9597-4844-96d0-66c41442322b" // todo: DO NOT FORGET TO CHANGE PRODUCTION TO STAGING SERVER.
    })
    @FormUrlEncoded
    @POST("auth")
    Observable<EdgeAuthResponse> getAuthToken(@Field("username") String userName, @Field("password") String password, @Field("grant_type") String type);


/*    @GET("api/v1/site")
    Observable<MSiteHelper> getSite(@Header("Authorization") String token); // do not forget to add Bearer to Token*/

    @GET("api/v1/whoami")
    Observable<EdgeWhoAmIResponse> whoami(@Header("Authorization") String token); // do not forget to add Bearer to Token

    @POST("api/v1/Membership")
    Observable<EdgeWhoAmIResponse> postWhoAmI(@Header("Authorization") String token, @Body EdgeWhoAmIResponse whoAmI);

    @POST("api/v1/ShoppingCart")
    Observable<EdgeShoppingCardResponse> claimReward(@Header("Authorization") String token, @Body EdgeShoppingCart edgeShoppingCart);

    @POST("api/v1/Pay")
    Observable<EdgePayResponse> pay(@Header("Authorization") String token, @Body Pay payment);


}
