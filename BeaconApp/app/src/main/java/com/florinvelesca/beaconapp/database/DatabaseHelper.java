package com.florinvelesca.beaconapp.database;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

public class DatabaseHelper {
    public static void insertBeacons(final AppDatabase database) {

        database.clearAllTables();
        BeaconDao beaconDao = database.beaconDao();
        BeaconLinkDao beaconLinkDao = database.beaconLinkDao();

        for (int i = 0; i < 10; i++) {
            BeaconTable beaconTable = new BeaconTable();
            beaconTable.setFloor(1);
            beaconTable.setRoomId(i);
            beaconTable.setType("room");

            beaconDao.insertBeacon(beaconTable);
        }

        List<BeaconTable> beaconTables = beaconDao.getAllBeacons();
        for (BeaconTable beaconTable : beaconTables) {
            Log.d("beaconz", beaconTable.toString());
        }

        for (int i = 0; i < beaconTables.size() - 1; i++) {
            BeaconTable beaconTable1 = beaconTables.get(i);
            BeaconTable beaconTable2 = beaconTables.get(i + 1);

            BeaconLink beaconLink = new BeaconLink();
            beaconLink.setBeacon1Id(beaconTable1.getId());
            beaconLink.setBeacon2Id(beaconTable2.getId());
            beaconLink.setDistance(10);
            beaconLinkDao.insertLink(beaconLink);
        }

        List<BeaconLink> links = beaconLinkDao.getAllBeaconLinks();
        for (BeaconLink link : links) {
            Log.d("beaconz", link.toString());
        }


    }

    public static void insertDataFromJson(Context context, final AppDatabase database) throws IOException {
        database.clearAllTables();
        BeaconDao beaconDao = database.beaconDao();
        BeaconLinkDao beaconLinkDao = database.beaconLinkDao();

        List<BeaconTable> beaconTables = readFromAssets(context, new TypeToken<List<BeaconTable>>(){}.getType(), "beacons.json");
        for (BeaconTable beaconTable : beaconTables) {
            beaconDao.insertBeacon(beaconTable);
        }

        List<BeaconLink> links = readFromAssets(context, new TypeToken<List<BeaconLink>>(){}.getType(), "links.json");
        for (BeaconLink link : links) {
            beaconLinkDao.insertLink(link);
        }
    }

    private static <T> List<T> readFromAssets(Context context, Type type, String file) throws IOException {
        InputStream is = context.getAssets().open(file);
        Gson gson = new Gson();
        return gson.fromJson(new InputStreamReader(is), type);
    }
}
