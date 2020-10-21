package com.icycoke.android.medication_reminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.icycoke.android.medication_reminder.persistence.AppDatabase;
import com.icycoke.android.medication_reminder.pojo.OpenApp;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";

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
                if (appDatabase.openAppDao().getOpenTimes() == 0) {
                    Log.d(TAG, "run: show the welcome interface");
                    Intent intent = new Intent(MainActivity.this, MyWelcomeActivity.class);
                    startActivity(intent);

                } else {
                    Log.d(TAG, "run: skip the welcome interface");
                    Log.d(TAG, "run: last open: " + appDatabase.openAppDao().getLastOpenDate().toString());
                }
                appDatabase.openAppDao().insert(new OpenApp(new Date()));
            }
        });
        queryThread.start();
    }
}