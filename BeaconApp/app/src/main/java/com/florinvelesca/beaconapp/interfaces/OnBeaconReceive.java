package com.florinvelesca.beaconapp.interfaces;


import org.altbeacon.beacon.Beacon;

import java.util.List;

public interface OnBeaconReceive {
     void OnBeaconReceive(List<Beacon> beaconList);
     void OnNearBeacon(Beacon beacon);
}
