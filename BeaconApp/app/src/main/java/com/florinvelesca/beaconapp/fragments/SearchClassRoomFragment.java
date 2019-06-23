package com.florinvelesca.beaconapp.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import com.florinvelesca.beaconapp.MainActivity;
import com.florinvelesca.beaconapp.R;
import com.florinvelesca.beaconapp.adapters.ClassRoomsRecyclerAdapter;
import com.florinvelesca.beaconapp.database.BeaconTable;
import com.florinvelesca.beaconapp.database.ClassroomCoordinates;
import com.florinvelesca.beaconapp.interfaces.OnClassRoomSelected;
import com.florinvelesca.beaconapp.map.DrawActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.altbeacon.beacon.BeaconParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchClassRoomFragment extends Fragment implements OnClassRoomSelected {
    private static final String RUUVI_LAYOUT = "m:0-2=0499,i:4-19,i:20-21,i:22-23,p:24-24"; // TBD
    private static final String IBEACON_LAYOUT = "m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24";
    private static final String ALTBEACON_LAYOUT = BeaconParser.ALTBEACON_LAYOUT;
    private static final String EDDYSTONE_UID_LAYOUT = BeaconParser.EDDYSTONE_UID_LAYOUT;
    private static final String EDDYSTONE_URL_LAYOUT = BeaconParser.EDDYSTONE_URL_LAYOUT;
    private static final String EDDYSTONE_TLM_LAYOUT = BeaconParser.EDDYSTONE_TLM_LAYOUT;

    private RelativeLayout relativeLayout;
    private RecyclerView recyclerView;
    private ClassRoomsRecyclerAdapter adapter;
    private List<String> classRooms = new ArrayList<>();
    public SearchClassRoomFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    private void setUpRecyclerView(){




    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_class_room, container, false);


        classRooms = getClassNames();

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_class_rooms);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        adapter = new ClassRoomsRecyclerAdapter(classRooms,getActivity(),this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        return view;
    }
    private List<String>  getClassNames(){

        InputStream classroomJsonInputStream = null;
        try {
            classroomJsonInputStream = getActivity().getAssets().open("classroom_coordinates_floor2.json");
            List<ClassroomCoordinates> classrooms = new Gson().fromJson(new InputStreamReader(classroomJsonInputStream), new TypeToken<List<ClassroomCoordinates>>() {
            }.getType());
            List<String> classNames = new ArrayList<>();
            for (ClassroomCoordinates classroom : classrooms) {
                classNames.add(classroom.getName());
            }
            return classNames;
        } catch (IOException e) {
            e.printStackTrace();
        }
       return Collections.emptyList();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        getActivity().getMenuInflater().inflate(R.menu.search_menu,menu);

        MenuItem searchItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Toast.makeText(getActivity(),s + "Fragment",Toast.LENGTH_LONG).show();
                adapter.getFilter().filter(s);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClassSelected(int position) {
        MainActivity mainActivity = (MainActivity) getActivity();
        BeaconTable currentBeacon = mainActivity.getCurrentBeacon();
        if(currentBeacon != null){
            Intent mapActivity = new Intent(getActivity(), DrawActivity.class);
            mapActivity.putExtra("ClassName",classRooms.get(position));
            mapActivity.putExtra("CurrentClassName",currentBeacon.getClassRoomName());
            mapActivity.putExtra("CurrentFloor",currentBeacon.getFloor());

            startActivity(mapActivity);

        }
        else {
            Toast.makeText(getActivity(),"You need tot go near a room",Toast.LENGTH_SHORT).show();
        }

    }
}
