package com.florinvelesca.beaconapp.interfaces;

import com.florinvelesca.beaconapp.database.BeaconTable;

import java.util.List;

public interface OnPathReceive {
    public void onPathReceive(List<BeaconTable> path);
}
