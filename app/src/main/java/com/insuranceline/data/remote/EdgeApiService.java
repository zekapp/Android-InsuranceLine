package com.insuranceline.data.remote;

import com.insuranceline.data.remote.responses.EdgeAuthResponse;
import com.insuranceline.data.remote.responses.ShoppingCardResponse;
import com.insuranceline.data.remote.responses.WhoAmIResponse;
import com.insuranceline.data.vo.ShoppingCart;

import au.com.lumo.ameego.model.MSiteHelper;
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
            "clientId: 31159E2C-1DA9-4F94-B279-A3259A74F127"
    })
    @FormUrlEncoded
    @POST("auth")
    Observable<EdgeAuthResponse> getAuthToken(@Field("username") String userName, @Field("password") String password, @Field("grant_type") String type);


    @GET("api/v1/site")
    Observable<MSiteHelper> getSite(@Header("Authorization") String token); // do not forget to add Bearer to Token

    @GET("api/v1/whoami")
    Observable<WhoAmIResponse> whoami(@Header("Authorization") String token); // do not forget to add Bearer to Token


    @POST("api/v1/ShoppingCart")
    Observable<ShoppingCardResponse> postShoppingCart(ShoppingCart shoppingCart);
}
