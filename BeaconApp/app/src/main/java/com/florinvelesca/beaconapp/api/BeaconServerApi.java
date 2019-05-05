package com.florinvelesca.beaconapp.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface BeaconServerApi {
    @GET("/")
    Call<Hello> hello();
    @POST("/")
    Call<Hello> createUser(@Body Hello hello);
}
