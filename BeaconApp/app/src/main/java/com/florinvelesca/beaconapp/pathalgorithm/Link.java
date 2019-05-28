package com.florinvelesca.beaconapp.pathalgorithm;

/**
 * Created by Ionut Stirban on 2019-05-12.
 */

public class Link {
    private int beacon;
    private int distance;

    public Link(int beacon, int distance) {
        this.beacon = beacon;
        this.distance = distance;
    }

    public int getBeacon() {
        return beacon;
    }

    public void setBeacon(int beacon) {
        this.beacon = beacon;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
