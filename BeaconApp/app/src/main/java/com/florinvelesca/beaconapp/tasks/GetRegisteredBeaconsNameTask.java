package com.florinvelesca.beaconapp.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.florinvelesca.beaconapp.database.AppDatabase;
import com.florinvelesca.beaconapp.database.BeaconDao;
import com.florinvelesca.beaconapp.database.BeaconTable;
import com.florinvelesca.beaconapp.database.DatabaseHolder;
import com.florinvelesca.beaconapp.interfaces.GetRegisteredBeaconNames;

import java.util.ArrayList;
import java.util.List;

public class GetRegisteredBeaconsNameTask extends AsyncTask<Void,Void, List<String>> {
    Context context;
    GetRegisteredBeaconNames registeredBeaconNames;
    public GetRegisteredBeaconsNameTask(Context context, GetRegisteredBeaconNames registeredBeaconNames) {
        this.context = context;
        this.registeredBeaconNames = registeredBeaconNames;
    }



    @Override
    protected List<String> doInBackground(Void... voids) {
        List<String> returnList = new ArrayList<>();
        AppDatabase database = DatabaseHolder.getDatabase(context);
        BeaconDao beaconDao = database.beaconDao();
        List<BeaconTable> beacons = beaconDao.getAllBeacons();
        for(BeaconTable beacon : beacons){
            returnList.add(beacon.getClassRoomName());
        }
        registeredBeaconNames.getRegisteredNames(returnList);
        return returnList;
    }
}
