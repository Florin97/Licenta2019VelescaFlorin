package com.florinvelesca.beaconapp.map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.florinvelesca.beaconapp.R;
import com.florinvelesca.beaconapp.database.AppDatabase;
import com.florinvelesca.beaconapp.database.ClassroomCoordinates;
import com.florinvelesca.beaconapp.database.DatabaseHolder;
import com.florinvelesca.beaconapp.interfaces.OnBeaconClassRoomNameReceive;
import com.florinvelesca.beaconapp.interfaces.OnBeaconReceive;
import com.florinvelesca.beaconapp.interfaces.OnPathReceive;
import com.florinvelesca.beaconapp.pathalgorithm.BeaconTest;
import com.florinvelesca.beaconapp.tasks.GetClassRoomByUuidTask;
import com.florinvelesca.beaconapp.tasks.GetPathTask;
import com.florinvelesca.beaconapp.tasks.SearchBeaconsTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.altbeacon.beacon.Beacon;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DrawActivity extends Activity implements OnBeaconReceive, OnBeaconClassRoomNameReceive, OnPathReceive {
    private MapView mapView;
    private List<Beacon> beaconList;
    private String destination;
    List<String> pathToFollow;
    Map<String, ClassroomCoordinates> classroomMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draw_activity);
        SearchBeaconsTask searchBeaconsTask = new SearchBeaconsTask(this);
        try {
            searchBeaconsTask.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            destination = extras.getString("ClassName", "nill");
            Log.d(getLocalClassName(), destination);
        }


        mapView = findViewById(R.id.map_view);

        try {
            InputStream classroomJsonInputStream = getAssets().open("classroom_coordinates_floor2.json");
            List<ClassroomCoordinates> classrooms = new Gson().fromJson(new InputStreamReader(classroomJsonInputStream), new TypeToken<List<ClassroomCoordinates>>() {
            }.getType());
            classroomMap = new HashMap<>(classrooms.size());
            for (ClassroomCoordinates classroom : classrooms) {
                classroomMap.put(classroom.getName(), classroom);
            }
            GetPathTask getPathTask = new GetPathTask(this,this,"C300","C303");
            getPathTask.execute();

        } catch (IOException e) {
            e.printStackTrace();
        }



    }


    private void sendClassroomsWithDelay(final List<ClassroomCoordinates> classrooms) {
        if (classrooms.isEmpty()) {
            return;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mapView.addVisitedRoom(classrooms.get(0).getName());
                classrooms.remove(0);
                sendClassroomsWithDelay(classrooms);
            }
        }, 2000);
    }

    private void sendNamesClassroomsWithDelay(final List<String> classrooms) {
        if (classrooms.isEmpty()) {
            return;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mapView.addVisitedRoom(classrooms.get(0));
                classrooms.remove(0);
                sendNamesClassroomsWithDelay(classrooms);
            }
        }, 2000);
    }


    @Override
    public void OnBeaconReceive(List<Beacon> beaconList) {
        Log.d(getLocalClassName(), beaconList.toString());
        this.beaconList = beaconList;

        for(Beacon beacon : beaconList){
            if(beacon.getDistance() < 0.5){

            }
        }


    }

    @Override
    public void OnNearBeacon(Beacon beacon) {

    }

    private void getBeaconName(Beacon beacon) {
        final AppDatabase database = DatabaseHolder.getDatabase(DrawActivity.this);
        GetClassRoomByUuidTask task = new GetClassRoomByUuidTask(database, DrawActivity.this);
        task.execute(beacon.getId1().toString(), beacon.getId3().toString());

    }

    @Override
    public void OnBeaconNameRetrieve(String name) {


    }


    @Override
    public void onPathReceive(List<String> path) {
        pathToFollow = path;
        mapView.setClassroomCoordinates(classroomMap);
        mapView.setPathToFollow(pathToFollow);
        sendNamesClassroomsWithDelay(pathToFollow);

    }
}
