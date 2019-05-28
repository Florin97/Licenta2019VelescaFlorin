package com.florinvelesca.beaconapp.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


@Entity
public class BeaconLink {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int beacon1Id;
    private int beacon2Id;
    private int distance;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBeacon1Id() {
        return beacon1Id;
    }

    public void setBeacon1Id(int beacon1Id) {
        this.beacon1Id = beacon1Id;
    }

    public int getBeacon2Id() {
        return beacon2Id;
    }

    public void setBeacon2Id(int beacon2Id) {
        this.beacon2Id = beacon2Id;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "BeaconLink{" +
                "id=" + id +
                ", beacon1Id=" + beacon1Id +
                ", beacon2Id=" + beacon2Id +
                ", distance=" + distance +
                '}';
    }
}
