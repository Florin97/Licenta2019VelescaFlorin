package com.florinvelesca.beaconapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.florinvelesca.beaconapp.adapters.ClassRoomsRecyclerAdapter;
import com.florinvelesca.beaconapp.database.AppDatabase;
import com.florinvelesca.beaconapp.database.BeaconTable;
import com.florinvelesca.beaconapp.database.DatabaseHolder;
import com.florinvelesca.beaconapp.fragments.SearchBeaconFragment;
import com.florinvelesca.beaconapp.fragments.SearchClassRoomFragment;
import com.florinvelesca.beaconapp.interfaces.OnBeaconClassRoomNameReceive;
import com.florinvelesca.beaconapp.interfaces.OnBeaconReceive;
import com.florinvelesca.beaconapp.tasks.GetClassRoomByUuidTask;
import com.florinvelesca.beaconapp.tasks.InsertBeaconsTask;


import org.altbeacon.beacon.Beacon;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnBeaconReceive, OnBeaconClassRoomNameReceive {
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private TextView NearClassTextView;


    private BeaconTable currentBeacon;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        linkUI(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertBeacons(List<Beacon> beacons) {
        final AppDatabase database = DatabaseHolder.getDatabase(MainActivity.this);
        InsertBeaconsTask insertBeaconsTask = new InsertBeaconsTask(this, database);


        if (beacons != null) {
            Log.d(getLocalClassName(),beacons.get(0).getId3().toString());
            try {
                insertBeaconsTask.execute(beacons);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
    private void getBeaconName(Beacon beacon){
        final AppDatabase database = DatabaseHolder.getDatabase(MainActivity.this);
        GetClassRoomByUuidTask task = new GetClassRoomByUuidTask(database,MainActivity.this);
        task.execute(beacon.getId1().toString(),beacon.getId3().toString());

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();


                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("app can't run without permissions");
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            checkPermission();
                        }
                    });
                    builder.show();
                }
                return;
            }
        }
    }

    private void linkUI(Bundle savedInstanceState) {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        drawerLayout = findViewById(R.id.draw_layout);
        NearClassTextView = findViewById(R.id.near_class_text_view);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (savedInstanceState == null) {

            navigationView.setCheckedItem(R.id.beacon_item_drawable);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter, new SearchBeaconFragment()).commit();

        }

    }


    public void checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Location Permission");
                builder.setPositiveButton("OK", null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                    }
                });
                builder.show();
            }
        }

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_clasroom_item:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter, new SearchClassRoomFragment()).commit();
                break;
            case R.id.beacon_item_drawable:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter, new SearchBeaconFragment()).commit();
                break;
            case R.id.test_item:
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setCurrentBeacon(BeaconTable currentBeacon) {
        this.currentBeacon = currentBeacon;
    }
    public BeaconTable getCurrentBeacon() {
        return currentBeacon;
    }

    @Override
    public void OnBeaconReceive(List<Beacon> beaconList) {
     //   insertBeacons(beaconList);

    }
    @Override
    public void OnNearBeacon(Beacon beacon){
            getBeaconName(beacon);
    }

    @Override
    public void OnBeaconNameRetrieve(BeaconTable beaconTable) {
        if(beaconTable != null){
            NearClassTextView.setText(beaconTable.getClassRoomName());
            setCurrentBeacon(beaconTable);
        }

    }



}
