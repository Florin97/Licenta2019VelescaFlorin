package com.florinvelesca.beaconapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.florinvelesca.beaconapp.beaconServices.ViewPagerAdapter;
import com.florinvelesca.beaconapp.api.Hello;
import com.florinvelesca.beaconapp.api.RetrofitBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        linkUI();



        Hello hello = new Hello("hell", "dasda");
        RetrofitBuilder.createApi().createUser(hello).enqueue(new Callback<Hello>() {
            @Override
            public void onResponse(Call<Hello> call, Response<Hello> response) {
                Toast.makeText(MainActivity.this, response.body().getDistance() + ' ' +  response.body().getUuid(), Toast.LENGTH_LONG).show();
            }


            @Override
            public void onFailure(Call<Hello> call, Throwable t) {

            }
        });


    }

   private void makeGetRequest(){
       RetrofitBuilder.createApi().hello().enqueue(new Callback<Hello>() {
           @Override
           public void onResponse(Call<Hello> call, Response<Hello> response) {
               Toast.makeText(MainActivity.this, response.body().getDistance(), Toast.LENGTH_LONG).show();
           }

           @Override
           public void onFailure(Call<Hello> call, Throwable t) {

           }
       });
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

    private void linkUI() {
//        toolbar = findViewById(R.id.toolbar);
//        this.setSupportActionBar(toolbar);
//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        viewPager = findViewById(R.id.viewpager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SearchBeacon(), "Search");
        viewPager.setAdapter(adapter);


        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

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

}
