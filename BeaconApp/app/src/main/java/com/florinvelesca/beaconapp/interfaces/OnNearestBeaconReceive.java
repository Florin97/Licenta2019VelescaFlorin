package com.florinvelesca.beaconapp.interfaces;

import com.florinvelesca.beaconapp.database.BeaconTable;

public interface OnNearestBeaconReceive {
    void onNearestBeaconReceive(BeaconTable beaconName);
}
