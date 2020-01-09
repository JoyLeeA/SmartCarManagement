package com.github.pires.obd.reader.API;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIInterface {


    @Headers("Content-Type: application/json")
    @POST("user/SignUp")
    Call<String> signup(@Body String body);


    @Headers("Content-Type: application/json")
    @POST("user/Login")
    Call<String> login(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("obd/insert")
    Call<String> setObdData(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("obd/get")
    Call<String> getObdData(@Body String body);

}
