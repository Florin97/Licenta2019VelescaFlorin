package com.florinvelesca.beaconapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.florinvelesca.beaconapp.database.AppDatabase;
import com.florinvelesca.beaconapp.database.BeaconDao;
import com.florinvelesca.beaconapp.database.BeaconTable;
import com.florinvelesca.beaconapp.interfaces.OnBeaconClassRoomNameReceive;

public class GetClassRoomByUuidTask extends AsyncTask<String,Void,BeaconTable> {
    private OnBeaconClassRoomNameReceive beaconClassRoomNameReceive;
    private AppDatabase database;
    private Context context;

    public GetClassRoomByUuidTask( AppDatabase database , Context context) {
        this.database = database;
        this.context = context;
        beaconClassRoomNameReceive = (OnBeaconClassRoomNameReceive) context;
    }




    @Override
    protected BeaconTable doInBackground(String... strings) {
        BeaconTable result;
        BeaconDao beaconDao = database.beaconDao();
            result = beaconDao.getRoom(strings[0],strings[1]);


        return result;
    }

    @Override
    protected void onPreExecute() {
        // Toast.makeText(context,"Approach to the closest classroom to identify your location",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostExecute(BeaconTable s) {

       // Toast.makeText(context,"You are near " + s, Toast.LENGTH_SHORT).show();
        beaconClassRoomNameReceive.OnBeaconNameRetrieve(s);
        super.onPostExecute(s);
    }

}
