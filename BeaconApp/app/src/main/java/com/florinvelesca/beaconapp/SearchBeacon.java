package com.florinvelesca.beaconapp;


import android.Manifest;
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

import com.florinvelesca.beaconapp.BeaconsRecylerAdapter;
import com.florinvelesca.beaconapp.R;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchBeacon extends Fragment implements BeaconConsumer {
    private RelativeLayout relativeLayout;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManagerRecycler;
    private RecyclerView.Adapter adapter;

    private BeaconManager beaconManager;
    private ProgressBar progressBar;
    private static String TAG = "SearchBeaconFragment";
    public SearchBeacon() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beaconManager = BeaconManager.getInstanceForApplication(Objects.requireNonNull(getActivity()));

        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT));
        beaconManager.bind(this);
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

    @Override
    public void onBeaconServiceConnect() {
        final Region region = new Region("Beacons",null,null,null);

        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                Log.d(TAG,"Enter ::::::::::::::::::>");
                try {
                    beaconManager.startRangingBeaconsInRegion(region);
                }catch (RemoteException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void didExitRegion(Region region) {
                Log.d(TAG,"EXIT :::::::::::::::::::>");
                try {
                    beaconManager.stopRangingBeaconsInRegion(region);
                }catch (RemoteException e){
                    e.printStackTrace();
                }

            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {
                Log.d(TAG,"Switch visibility for beacons in range");

            }
        });

        beaconManager.addRangeNotifier(new RangeNotifier() {
            /*
            Get info about detected beacons
             */
            @Override
            public void didRangeBeaconsInRegion(final Collection<Beacon> beacons, Region region) {
                if (beacons.size() > 0) {
                    final List<Beacon> beaconList = new ArrayList<>(beacons.size());
                    beaconList.addAll(beacons);
                    try {
                        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.INVISIBLE);
                                relativeLayout.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);

                                layoutManagerRecycler = new LinearLayoutManager(getActivity());
                                recyclerView.setLayoutManager(layoutManagerRecycler);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    try {
                        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter = new BeaconsRecylerAdapter(beaconList,SearchBeacon.this.getActivity().getApplicationContext());
                                recyclerView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else { //else if(beacons.size() == 0)
                    try{
                        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.INVISIBLE);
                                relativeLayout.setVisibility(View.VISIBLE);
                                recyclerView.setVisibility(View.GONE);
                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
        try {
            beaconManager.startMonitoringBeaconsInRegion(region);
        }catch (RemoteException e){
            e.printStackTrace();
        }

    }

    @Override
    public Context getApplicationContext() {
        return Objects.requireNonNull(getActivity()).getApplicationContext();
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
            Objects.requireNonNull(getActivity()).unbindService(serviceConnection);
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return Objects.requireNonNull(getActivity()).bindService(intent, serviceConnection,i);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }
}
