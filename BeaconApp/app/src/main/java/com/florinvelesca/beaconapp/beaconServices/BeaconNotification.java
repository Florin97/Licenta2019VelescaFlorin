package com.florinvelesca.beaconapp.beaconServices;

import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

public class BeaconNotification extends Application implements BootstrapNotifier {
    public static BeaconManager beaconManager;
    private RegionBootstrap regionBootstrap;
    public static Region region;
    public BackgroundPowerSaver backgroundPowerSaver;
    @Override
    public void onCreate() {
        super.onCreate();
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(!bluetoothAdapter.isEnabled()){
            Intent bletoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            bletoothIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(bletoothIntent);
        }

        beaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(this);

        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));

        beaconManager.setForegroundScanPeriod(1100L);
        beaconManager.setForegroundBetweenScanPeriod(0L);
        //beaconManager.setAndroidLScanningDisabled(true);
        beaconManager.setBackgroundBetweenScanPeriod(0L);
        beaconManager.setForegroundScanPeriod(1100L);

        try {
            beaconManager.updateScanPeriods();
        }catch (Exception e){
            e.printStackTrace();
        }
        region = new Region("backgroundRegion",null,null,null);
        regionBootstrap = new RegionBootstrap(this,region);

        backgroundPowerSaver = new BackgroundPowerSaver(this);

    }

    @Override
    public void didEnterRegion(Region region) {
        try {
            Intent beaconServiceIntent = new Intent(getApplicationContext(),BeaconService.class);
            startService(beaconServiceIntent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void didExitRegion(Region region) {
        try {
            Intent beaconServiceIntent = new Intent(getApplicationContext(),BeaconService.class);
            startService(beaconServiceIntent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /*
      This override method will Determine the state for the device , whether device is in range
      of beacon or not , if yes then i = 1 and if no then i = 0
     */
    @Override
    public void didDetermineStateForRegion(int i, Region region) {
        try {
            Intent beaconServiceIntent = new Intent(getApplicationContext(),BeaconService.class);
            startService(beaconServiceIntent);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
