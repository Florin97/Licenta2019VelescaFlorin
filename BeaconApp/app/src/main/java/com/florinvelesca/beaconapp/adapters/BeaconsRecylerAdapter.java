package com.florinvelesca.beaconapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.florinvelesca.beaconapp.R;
import com.florinvelesca.beaconapp.api.Hello;
import com.florinvelesca.beaconapp.api.RetrofitBuilder;

import org.altbeacon.beacon.Beacon;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BeaconsRecylerAdapter extends RecyclerView.Adapter<BeaconsRecylerAdapter.ViewHolder> {
    private List<Beacon> beacons;
    private Context context;
    private static final String TAG = "BeaconsRecylerAdapter";

    public BeaconsRecylerAdapter(List<Beacon> beacons, Context context) {
        this.beacons = beacons;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_beacon_item, viewGroup, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int index) {
         
        Beacon beacon = beacons.get(index);

        makePostRequest(new Hello(beacon.getId1().toString(),String.valueOf(beacon.getDistance())));

        viewHolder.uuid.setText(beacon.getId1().toString());
        viewHolder.major.setText(beacon.getId2().toString());
        viewHolder.minor.setText(beacon.getId2().toString());
        viewHolder.distance.setText(String.valueOf(beacon.getDistance()));
        Log.d(TAG,viewHolder.uuid.getText().toString());

    }

    @Override
    public int getItemCount() {
        return beacons.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView uuid;
        private TextView major;
        private TextView minor;
        private TextView distance;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            uuid = itemView.findViewById(R.id.uuid);
            major = itemView.findViewById(R.id.major);
            minor = itemView.findViewById(R.id.minor);
            distance = itemView.findViewById(R.id.distance);


        }
    }
    private void makePostRequest(Hello hello){
        RetrofitBuilder.createApi().createUser(hello).enqueue(new Callback<Hello>() {
            @Override
            public void onResponse(Call<Hello> call, Response<Hello> response) {
                Toast.makeText(context, response.body().getDistance(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Hello> call, Throwable t) {

            }
        });
    }
}
