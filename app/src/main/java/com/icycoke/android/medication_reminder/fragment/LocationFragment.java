package com.icycoke.android.medication_reminder.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.icycoke.android.medication_reminder.FunctionsActivity;
import com.icycoke.android.medication_reminder.MainActivity;
import com.icycoke.android.medication_reminder.R;
import com.icycoke.android.medication_reminder.persistence.AppDatabase;
import com.icycoke.android.medication_reminder.pojo.SavedLocation;
import com.icycoke.android.medication_reminder.util.GeofenceUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LocationFragment extends Fragment
        implements OnMapReadyCallback, FunctionsActivity.SetHomeOnClickListener {

    private static final String TAG = LocationFragment.class.getSimpleName();
    private static final int DEFAULT_ZOOM = 15;
    private static final float GEOFENCE_RADIUS_DEFAULT = 200;
    private static final String GEOFENCE_ID = "MY_GEOFENCE";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int PERMISSIONS_REQUEST_ACCESS_BACKGROUND_LOCATION = 2;

    private GoogleMap googleMap;

    private AppDatabase appDatabase;

    private GeofencingClient geofencingClient;
    private GeofenceUtil geofenceUtil;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: map is ready");
        this.googleMap = googleMap;
    }

    @Override
    public void onStart() {
        super.onStart();

        appDatabase = AppDatabase.getInstance(getActivity().getApplicationContext());
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        supportMapFragment.getMapAsync(this);

        geofencingClient = LocationServices.getGeofencingClient(getActivity());
        geofenceUtil = new GeofenceUtil(getActivity());

        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (appDatabase.savedLocationDao().getSavedCount() != 0) {
                    final SavedLocation currentHome = appDatabase.savedLocationDao().getCurrentHome();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            showHome(new LatLng(currentHome.latitude,
                                    currentHome.longitude));
                        }
                    });
                }
            }
        });
        thread.start();
    }

    @SuppressLint("MissingPermission")
    @Override
    public void showHome(LatLng latLng) {
        Log.d(TAG, "showHome: showing current home location");
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(getResources().getString(R.string.home)))
                .showInfoWindow();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
        addCircle(latLng, GEOFENCE_RADIUS_DEFAULT);
        googleMap.setMyLocationEnabled(true);

        addGeofence(latLng, GEOFENCE_RADIUS_DEFAULT);

        Thread collectDataThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: writing data to file");
                File file = new File(getActivity().getFilesDir().getAbsolutePath() + "/home_location_history.csv");
                try {
                    FileWriter fileWriter = new FileWriter(file, true);
                    SavedLocation currentHome = appDatabase.savedLocationDao().getCurrentHome();

                    StringBuilder sb = new StringBuilder();
                    sb.append("lat: ").append(currentHome.latitude).append('\t')
                            .append("lng: ").append(currentHome.longitude).append('\n');
                    fileWriter.write(sb.toString());
                    fileWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        collectDataThread.start();
    }

    private void addCircle(LatLng latLng, float radius) {
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(radius);
        circleOptions.strokeColor(Color.argb(255, 0, 0, 255));
        circleOptions.fillColor(Color.argb(64, 0, 0, 255));
        circleOptions.strokeWidth(4);
        googleMap.addCircle(circleOptions);
    }

    private void addGeofence(LatLng latLng, float radius) {
        Geofence geofence = geofenceUtil.getGeofence(GEOFENCE_ID, latLng, radius,
                Geofence.GEOFENCE_TRANSITION_EXIT);
        GeofencingRequest geofencingRequest = geofenceUtil.getGeofencingRequest(geofence);
        PendingIntent pendingIntent = geofenceUtil.getPendingIntent();

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_BACKGROUND_LOCATION);
        }
        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: geofence added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: adding geofence failed");
                        String errorMessage = geofenceUtil.getErrorString(e);
                        Log.d(TAG, "onFailure: error message: " + errorMessage);
                        e.printStackTrace();
                    }
                });
    }
}