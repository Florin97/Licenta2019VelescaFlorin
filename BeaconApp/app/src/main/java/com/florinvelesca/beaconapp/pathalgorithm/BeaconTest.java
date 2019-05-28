package com.florinvelesca.beaconapp.pathalgorithm;

import android.util.Log;

import com.florinvelesca.beaconapp.database.AppDatabase;
import com.florinvelesca.beaconapp.database.BeaconTable;
import com.florinvelesca.beaconapp.database.BeaconDao;
import com.florinvelesca.beaconapp.database.BeaconLink;
import com.florinvelesca.beaconapp.database.BeaconLinkDao;

import java.util.List;


public class BeaconTest {
    public static void start(final AppDatabase appDatabase) {

        BeaconLinkDao beaconLinkDao = appDatabase.beaconLinkDao();
        BeaconDao beaconDao = appDatabase.beaconDao();
        List<BeaconLink> allLinks = beaconLinkDao.getAllBeaconLinks();
        BeaconGraph beaconGraph = new BeaconGraph();
        for (BeaconLink link : allLinks) {
            Log.d("link", link.toString());
            beaconGraph.addLink(link.getBeacon1Id(), link.getBeacon2Id(), link.getDistance());
        }

        List<BeaconTable> beaconTables = beaconDao.getAllBeacons();
        BeaconTable lastBeaconTable = beaconTables.get(beaconTables.size() - 1);
        List<Integer> path = beaconGraph.getPath(beaconTables.get(0).getId(), lastBeaconTable.getId());

        for (Integer integer : path) {
            Log.d("path", integer.toString());
        }
    }

}
