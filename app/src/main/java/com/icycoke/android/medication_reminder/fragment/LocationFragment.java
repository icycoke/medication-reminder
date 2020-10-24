package com.icycoke.android.medication_reminder.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.icycoke.android.medication_reminder.FunctionsActivity;
import com.icycoke.android.medication_reminder.R;
import com.icycoke.android.medication_reminder.persistence.AppDatabase;
import com.icycoke.android.medication_reminder.pojo.SavedLocation;

public class LocationFragment extends Fragment
        implements OnMapReadyCallback, FunctionsActivity.SetHomeOnClickListener {

    private static String TAG = LocationFragment.class.getSimpleName();
    private static int DEFAULT_ZOOM = 15;

    private GoogleMap googleMap;
    private GeofencingClient geofencingClient;

    private AppDatabase appDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: map is ready");
        this.googleMap = googleMap;
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

    @Override
    public void onStart() {
        super.onStart();

        appDatabase = AppDatabase.getInstance(getActivity().getApplicationContext());
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        supportMapFragment.getMapAsync(this);

        geofencingClient = LocationServices.getGeofencingClient(getActivity());
    }

    @Override
    public void showHome(LatLng latLng) {
        Log.d(TAG, "showHome: showing current home location");
        googleMap.clear();
        googleMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(getResources().getString(R.string.home)))
                .showInfoWindow();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
    }
}
