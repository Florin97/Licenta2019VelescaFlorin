package com.florinvelesca.beaconapp.beaconServices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class BeaconBroadCast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("BeaconBroadCast","Again Starting the service");
        //Again starting the service
        context.startService(new Intent(context, BeaconService.class));
    }
}