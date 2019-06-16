package com.florinvelesca.beaconapp.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.florinvelesca.beaconapp.database.AppDatabase;
import com.florinvelesca.beaconapp.database.BeaconTable;
import com.florinvelesca.beaconapp.database.DatabaseHolder;
import com.florinvelesca.beaconapp.interfaces.OnBeaconClassRoomNameReceive;
import com.florinvelesca.beaconapp.interfaces.OnPathReceive;
import com.florinvelesca.beaconapp.pathalgorithm.BeaconTest;

import java.util.ArrayList;
import java.util.List;

public class GetPathTask extends AsyncTask<Void, Void, Void> {
    private OnPathReceive onPathReceive;

    private Context context;
    private String currentClass;
    private String destinationClass;
    private List<BeaconTable> path;


    public GetPathTask(Context context, OnPathReceive onPathReceive, String currentClass, String destinationClass) {
        this.context = context;
        this.onPathReceive = onPathReceive;
        this.currentClass = currentClass;
        this.destinationClass = destinationClass;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        AppDatabase database = DatabaseHolder.getDatabase(context);
        path = BeaconTest.start(database, currentClass, destinationClass);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        onPathReceive.onPathReceive(path);
        super.onPostExecute(aVoid);
    }
}
