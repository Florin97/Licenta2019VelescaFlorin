package com.florinvelesca.beaconapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {
    private static Retrofit retrofit;

    public static BeaconServerApi createApi() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl("http://192.168.0.104:8008")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            return retrofit.create(BeaconServerApi.class);
        } else {
            return retrofit.create(BeaconServerApi.class);
        }

    }

}
