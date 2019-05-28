package com.florinvelesca.beaconapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.florinvelesca.beaconapp.database.AppDatabase;
import com.florinvelesca.beaconapp.database.BeaconDao;
import com.florinvelesca.beaconapp.database.BeaconTable;

import org.altbeacon.beacon.Beacon;

import java.util.List;

public class InsertBeaconsTask extends AsyncTask<List<Beacon>, Void, Void> {
    private Context context;
    private AppDatabase database;
    public InsertBeaconsTask(Context context, AppDatabase database) {
        this.context = context;
        this.database = database;
    }


    @Override
    protected Void doInBackground(List<Beacon>... beacons) {
        BeaconDao beaconDao = database.beaconDao();
        BeaconTable beaconTable = new BeaconTable();
        int i = 1;
        for (Beacon beacon : beacons[0]){
            beaconTable.setUuid(beacon.getId1().toString());
            beaconTable.setMinor(beacon.getId3().toString());
            beaconTable.setType("2");
            beaconTable.setFloor(1);
            beaconTable.setClassRoomName("C10" + String.valueOf(i));
            i += 1;

            beaconDao.insertBeacon(beaconTable);
        }


        return null;
    }

    @Override
    protected void onPreExecute() {
       // Toast.makeText(context,"Approach to the closest classroom to identify your location",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.d("InsertBeaconsTask::","transation finished");
    }

}
