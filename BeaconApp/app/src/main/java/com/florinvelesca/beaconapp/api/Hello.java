package com.florinvelesca.beaconapp.api;

import com.google.gson.annotations.SerializedName;

public class Hello {
    @SerializedName("uuid")
    private String uuid;
    @SerializedName("distance")
    private String distance;
    public Hello(String uuid, String distance){
        this.uuid = uuid;
        this.distance = distance;
    }
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String received) {
        this.uuid = received;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
