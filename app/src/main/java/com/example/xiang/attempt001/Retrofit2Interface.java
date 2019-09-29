package com.example.xiang.attempt001;

/**
 * Created by xiang on 2017-06-18.
 */

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

public interface Retrofit2Interface {

    //These are the ones for logging in and creating users, they work no problem.
    @POST("customers")
    Observable<Response> register(@Body User user);

    @POST("authenticate")
    Observable<Response> login();

    @GET("customers/{email}")
    Observable<User> getProfile(@Path("email") String email);

    @PUT("customers/{email}")
    Observable<Response> changePassword(@Path("email") String emailorcart, @Body User user);

    @POST("customers/{email}/password")
    Observable<Response> resetPasswordInit(@Path("email") String email);

    @POST("customers/{email}/password")
    Observable<Response> resetPasswordFinish(@Path("email") String email, @Body User user);

}
