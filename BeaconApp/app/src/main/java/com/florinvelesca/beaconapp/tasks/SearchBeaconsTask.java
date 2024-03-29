package com.florinvelesca.beaconapp.tasks;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.util.Log;

import com.florinvelesca.beaconapp.MainActivity;
import com.florinvelesca.beaconapp.database.AppDatabase;
import com.florinvelesca.beaconapp.database.BeaconDao;
import com.florinvelesca.beaconapp.database.BeaconTable;
import com.florinvelesca.beaconapp.database.DatabaseHolder;
import com.florinvelesca.beaconapp.fragments.SearchBeaconFragment;
import com.florinvelesca.beaconapp.interfaces.OnBeaconClassRoomNameReceive;
import com.florinvelesca.beaconapp.interfaces.OnBeaconReceive;
import com.florinvelesca.beaconapp.interfaces.OnNearestBeaconReceive;
import com.florinvelesca.beaconapp.map.DrawActivity;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class SearchBeaconsTask extends AsyncTask<Void, Void, Void> {
    private Context context;
    private OnBeaconReceive onBeaconReceive;
    private OnBeaconClassRoomNameReceive onBeaconClassRoomNameReceive;
    private BeaconConsumer beaconConsumer;
    private BeaconManager beaconManager;
    private static final String IBEACON_LAYOUT = "m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24";
    private static final String EDDYSTONE_UID_LAYOUT = BeaconParser.EDDYSTONE_UID_LAYOUT;
    private static final String EDDYSTONE_URL_LAYOUT = BeaconParser.EDDYSTONE_URL_LAYOUT;
    private static final String EDDYSTONE_TLM_LAYOUT = BeaconParser.EDDYSTONE_TLM_LAYOUT;
    private OnBeaconReceive beaconReceive;
    private AppDatabase database;
    private OnNearestBeaconReceive nearestBeaconReceive;

    public SearchBeaconsTask(Context context) {
        this.context = context;
        beaconReceive = (OnBeaconReceive) context;
        nearestBeaconReceive = (OnNearestBeaconReceive) context;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        database = DatabaseHolder.getDatabase(context);


        beaconManager = BeaconManager.getInstanceForApplication(context);

        createBeaconConsumer();
        // Sets the delay between each scans according to the settings
        //instance.foregroundBetweenScanPeriod = prefs.getScanDelay()

        beaconManager.setForegroundBetweenScanPeriod(2000);
        // Add all the beacon types we want to discover
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(IBEACON_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(EDDYSTONE_UID_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(EDDYSTONE_URL_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(EDDYSTONE_TLM_LAYOUT));

        beaconManager.bind(beaconConsumer);

        return null;


    }


    private void createBeaconConsumer() {
        beaconConsumer = new BeaconConsumer() {
            @Override
            public void onBeaconServiceConnect() {
                connect();
            }

            @Override
            public Context getApplicationContext() {
                return getApplicationContext();
            }

            @Override
            public void unbindService(ServiceConnection serviceConnection) {

            }

            @Override
            public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
                return false;
            }
        };
    }

    private void connect() {
        beaconManager.removeAllRangeNotifiers();
        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {
                final List<Beacon> beacons = new ArrayList<>(collection);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (!beacons.isEmpty() && context != null) {

                            final Beacon closestBeacon = getClosestBeacon(beacons);
                            final BeaconTable closestBeaconName = getClassByUuidMinor(closestBeacon.getId1().toString(), closestBeacon.getId3().toString());


                            Objects.requireNonNull((DrawActivity) context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (closestBeacon.getDistance() < 0.3) {
                                        nearestBeaconReceive.onNearestBeaconReceive(closestBeaconName);
                                    }

                                    beaconReceive.OnBeaconReceive(beacons);

                                }
                            });
                        }
                    }
                }).start();
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("test", null, null, null));
        } catch (RemoteException e) {
            Log.e(getClass().getName(), "Error: ", e);
        }
    }

    private Beacon getClosestBeacon(List<Beacon> beaconList) {
        double min = Integer.MAX_VALUE;
        Beacon minBeacon = null;
        for (Beacon beacon : beaconList) {
            if (beacon.getDistance() < min) {
                min = beacon.getDistance();
                minBeacon = beacon;
            }
        }

        return minBeacon;
    }


    private BeaconTable getClassByUuidMinor(String uuid, String minor) {
        BeaconTable result;
        BeaconDao beaconDao = database.beaconDao();
        result = beaconDao.getRoom(uuid, minor);


        return result;
    }
}
