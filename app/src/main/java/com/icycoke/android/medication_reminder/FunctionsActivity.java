package com.icycoke.android.medication_reminder;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.icycoke.android.medication_reminder.fragment.LocationFragment;
import com.icycoke.android.medication_reminder.fragment.PeriodicFragment;
import com.icycoke.android.medication_reminder.persistence.AppDatabase;
import com.icycoke.android.medication_reminder.pojo.SavedLocation;

public class FunctionsActivity extends AppCompatActivity {

    private static final String TAG = FunctionsActivity.class.getSimpleName();
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private SetHomeOnClickListener setHomeOnClickListener;

    private AppDatabase appDatabase;

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()) {
                        case R.id.navigation_periodic:
                            selectedFragment = new PeriodicFragment();
                            break;
                        case R.id.navigation_location:
                            selectedFragment = new LocationFragment();
                            setHomeOnClickListener = (SetHomeOnClickListener) selectedFragment;
                            break;
                    }

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();

                    return true;
                }
            };

    public void setHomeOnClick(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationRequest locationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(1000)
                    .setFastestInterval(500);
            LocationCallback locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult != null) {
                        final Location lastLocation = locationResult.getLastLocation();
                        setHomeOnClickListener.showHome(new LatLng(lastLocation.getLatitude(),
                                lastLocation.getLongitude()));
                        fusedLocationProviderClient.removeLocationUpdates(this);

                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                appDatabase.savedLocationDao().clear();
                                appDatabase.savedLocationDao().insert(new SavedLocation(lastLocation.getLatitude(),
                                        lastLocation.getLongitude()));
                                Log.d(TAG, "run: new home location inserted");
                            }
                        });
                        thread.start();
                    }
                }
            };
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_functions);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new PeriodicFragment())
                .commit();

        appDatabase = AppDatabase.getInstance(getApplicationContext());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    public interface SetHomeOnClickListener {
        void showHome(LatLng latLng);
    }
}