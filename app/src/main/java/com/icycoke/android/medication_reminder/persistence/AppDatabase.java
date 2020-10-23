package com.icycoke.android.medication_reminder.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.icycoke.android.medication_reminder.dao.OpenAppDao;
import com.icycoke.android.medication_reminder.dao.SavedLocationDao;
import com.icycoke.android.medication_reminder.pojo.OpenApp;
import com.icycoke.android.medication_reminder.pojo.SavedLocation;

@Database(entities = {OpenApp.class, SavedLocation.class}, version = 1)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class,
                            "app_data.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract OpenAppDao openAppDao();

    public abstract SavedLocationDao savedLocationDao();
}
