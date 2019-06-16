package com.florinvelesca.beaconapp.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.florinvelesca.beaconapp.R;
import com.florinvelesca.beaconapp.adapters.BeaconsRecylerAdapter;
import com.florinvelesca.beaconapp.interfaces.OnBeaconClassRoomNameReceive;
import com.florinvelesca.beaconapp.interfaces.OnBeaconReceive;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchBeaconFragment extends Fragment{
    private static final String RUUVI_LAYOUT = "m:0-2=0499,i:4-19,i:20-21,i:22-23,p:24-24"; // TBD
    private static final String IBEACON_LAYOUT = "m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24";
    private static final String ALTBEACON_LAYOUT = BeaconParser.ALTBEACON_LAYOUT;
    private static final String EDDYSTONE_UID_LAYOUT = BeaconParser.EDDYSTONE_UID_LAYOUT;
    private static final String EDDYSTONE_URL_LAYOUT = BeaconParser.EDDYSTONE_URL_LAYOUT;
    private static final String EDDYSTONE_TLM_LAYOUT = BeaconParser.EDDYSTONE_TLM_LAYOUT;

    private RelativeLayout relativeLayout;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManagerRecycler;
    private RecyclerView.Adapter adapter;
    private OnBeaconReceive onBeaconReceive;
    private OnBeaconClassRoomNameReceive onBeaconClassRoomNameReceive;
    private BeaconConsumer beaconConsumer;
    private BeaconManager beaconManager;
    private ProgressBar progressBar;

    private static String TAG = "SearchBeaconFragment";


    public SearchBeaconFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        beaconManager = BeaconManager.getInstanceForApplication(getContext());

        createBeaconConsumer();
        // Sets the delay between each scans according to the settings
        //instance.foregroundBetweenScanPeriod = prefs.getScanDelay()

        beaconManager.setForegroundBetweenScanPeriod(10);
        // Add all the beacon types we want to discover
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(IBEACON_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(EDDYSTONE_UID_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(EDDYSTONE_URL_LAYOUT));
        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(EDDYSTONE_TLM_LAYOUT));

        beaconManager.bind(beaconConsumer);
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
                if(!beacons.isEmpty() && getActivity() != null){
                    Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            displayBeacons(beacons);
                        }
                    });
                }
            }
        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("test", null, null, null));
        } catch (RemoteException e) {
            Log.e("dfsdf", "sfsdf", e);
        }
    }
    private void displayBeacons(List<Beacon> beacons){
        progressBar.setVisibility(View.INVISIBLE);
        relativeLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        layoutManagerRecycler = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManagerRecycler);

        adapter = new BeaconsRecylerAdapter(beacons, getContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
//min beacon
        onBeaconReceive.OnBeaconReceive(beacons);
        for (Beacon beacon : beacons) {
            if(isNearClassRoom(beacon.getDistance())){
                onBeaconReceive.OnNearBeacon(beacon);
                //TextView with Name of the class taken from Database
               // Toast.makeText(getContext(),"you are near" + beacon.getId1().toString(),Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onBeaconReceive = (OnBeaconReceive) context;


    }

    private  int getCount(Beacon beacon, List<Beacon> list){
        int count = 0;
        for(Beacon compareBeacon : list){

            if(compareBeacon.getId1().toString().contains(beacon.getId1().toString())){
                count += 1;
            }
        }

        return count;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.search_beacon_fragment, container, false);

        relativeLayout = view.findViewById(R.id.Relative_One);

        recyclerView = view.findViewById(R.id.search_recycler);

        progressBar = view.findViewById(R.id.pb);
        return view;
    }
    private boolean isNearClassRoom(Double distance){
        return distance < 0.5;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(beaconConsumer);
    }
}
