package com.icycoke.android.medication_reminder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.MapView;
import com.icycoke.android.medication_reminder.persistence.AppDatabase;
import com.icycoke.android.medication_reminder.pojo.OpenApp;
import com.icycoke.android.medication_reminder.pojo.SavedLocation;
import com.icycoke.android.medication_reminder.welcome.MyWelcomeActivity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private AppDatabase appDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appDatabase = AppDatabase.getInstance(getApplicationContext());

        // Show welcome interface if it's the first open
        Thread queryThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // appDatabase.openAppDao().clear();
                if (appDatabase.openAppDao().getOpenTimes() == 0) {
                    Intent intent = new Intent(MainActivity.this, MyWelcomeActivity.class);
                    startActivity(intent);
                    Log.d(TAG, "run: show the welcome interface");
                } else {
                    Log.d(TAG, "run: skip the welcome interface");
                    Log.d(TAG, "run: last open: " + appDatabase.openAppDao().getLastOpenDate().toString());
                }
                appDatabase.openAppDao().insert(new OpenApp(new Date()));
                Log.d(TAG, "run: latest open date inserted");
            }
        });
        queryThread.start();

        Intent intent = new Intent(this, FunctionsActivity.class);
        startActivity(intent);

    }
}