package com.florinvelesca.beaconapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import org.altbeacon.beacon.Beacon;

@Entity(tableName = "beacons")
public class BeaconModel {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "uuid")
    private String uuid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "distance")
    private String distance;
    @Ignore
    public BeaconModel(Beacon beacon){
        this.uuid = beacon.getId1().toString();
        this.distance = String.valueOf(beacon.getDistance());

    }
    public BeaconModel(String uuid, String distance, String name) {
        this.name = name;
        this.uuid = uuid;
        this.distance = distance;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

