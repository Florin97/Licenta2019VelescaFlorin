package com.florinvelesca.beaconapp.map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.florinvelesca.beaconapp.R;
import com.florinvelesca.beaconapp.database.AppDatabase;
import com.florinvelesca.beaconapp.database.BeaconTable;
import com.florinvelesca.beaconapp.database.ClassroomCoordinates;
import com.florinvelesca.beaconapp.database.DatabaseHolder;
import com.florinvelesca.beaconapp.interfaces.OnBeaconClassRoomNameReceive;
import com.florinvelesca.beaconapp.interfaces.OnBeaconReceive;
import com.florinvelesca.beaconapp.interfaces.OnNearestBeaconReceive;
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


public class DrawActivity extends Activity implements OnBeaconReceive, OnBeaconClassRoomNameReceive, OnPathReceive, OnNearestBeaconReceive {
    private MapView mapView;
    private List<Beacon> beaconList;
    private String destination;
    private String currentLocation;
    private int currentFloor = -1;
    List<BeaconTable> pathToFollow;
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
            currentLocation = extras.getString("CurrentClassName","nill");
            currentFloor = extras.getInt("CurrentFloor",-1);
        }

        mapView = findViewById(R.id.map_view);
        mapView.setCurrentFloor(currentFloor);
        try {
            InputStream classroomJsonInputStream = getAssets().open("classroom_coordinates_floor2.json");
            List<ClassroomCoordinates> classrooms = new Gson().fromJson(new InputStreamReader(classroomJsonInputStream), new TypeToken<List<ClassroomCoordinates>>() {
            }.getType());
            classroomMap = new HashMap<>(classrooms.size());
            for (ClassroomCoordinates classroom : classrooms) {
                classroomMap.put(classroom.getName(), classroom);
            }

            GetPathTask getPathTask = new GetPathTask(this, this, currentLocation, destination);
            getPathTask.execute();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    @Override
    public void OnBeaconReceive(List<Beacon> beaconList) {
        Log.d(getLocalClassName(), beaconList.toString());
        this.beaconList = beaconList;
    }

    @Override
    public void OnNearBeacon(Beacon beacon) { }

    @Override
    public void OnBeaconNameRetrieve(BeaconTable name) { }


    @Override
    public void onPathReceive(List<BeaconTable> path) {
        pathToFollow = path;
        mapView.setClassroomCoordinates(classroomMap);
        mapView.setPathToFollow(pathToFollow);
    }

    @Override
    public void onNearestBeaconReceive(BeaconTable beaconName) {
        if(mapView.isVisited(beaconName)){
            Toast.makeText(DrawActivity.this,"You Are going in the wrong direction",Toast.LENGTH_SHORT).show();

        }
        mapView.setCurrentFloor(beaconName.getFloor());
        mapView.addVisitedRoom(beaconName);

        mapView.invalidate();
    }
}
