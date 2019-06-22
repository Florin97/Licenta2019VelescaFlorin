package com.florinvelesca.beaconapp.pathalgorithm;

import android.util.Log;

import com.florinvelesca.beaconapp.database.AppDatabase;
import com.florinvelesca.beaconapp.database.BeaconTable;
import com.florinvelesca.beaconapp.database.BeaconDao;
import com.florinvelesca.beaconapp.database.BeaconLink;
import com.florinvelesca.beaconapp.database.BeaconLinkDao;

import java.util.ArrayList;
import java.util.List;


public class BeaconPath {

    public static List<BeaconTable> getPath(final AppDatabase appDatabase, String currentLocation, String destination) {

        BeaconLinkDao beaconLinkDao = appDatabase.beaconLinkDao();
        BeaconDao beaconDao = appDatabase.beaconDao();

        int currentLocationId = beaconDao.getClassIdByName(currentLocation);
        int destinationId = beaconDao.getClassIdByName(destination);


        List<BeaconLink> allLinks = beaconLinkDao.getAllBeaconLinks();
        BeaconGraph beaconGraph = new BeaconGraph();
        for (BeaconLink link : allLinks) {
            Log.d("link", link.toString());
            beaconGraph.addLink(link.getBeacon1Id(), link.getBeacon2Id(), link.getDistance());
        }

//        List<BeaconTable> beaconTables = beaconDao.getAllBeacons();
//        BeaconTable lastBeaconTable = beaconTables.get(beaconTables.size() - 1);

        List<Integer> path = beaconGraph.getPath(currentLocationId, destinationId);

        for (Integer integer : path) {
            Log.d("path", integer.toString());
        }

        List<BeaconTable> pathRooms = new ArrayList<>();
        for (int i : path) {
            pathRooms.add(beaconDao.getRoomById(i));
        }

        return pathRooms;
    }

}
