package com.icycoke.android.medication_reminder.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.icycoke.android.medication_reminder.pojo.SavedLocation;

@Dao
public interface SavedLocationDao {

    @Query("SELECT COUNT(*) FROM saved_location")
    int getSavedCount();

    @Query("SELECT * FROM saved_location ORDER BY `index` DESC LIMIT 0,1")
    SavedLocation getCurrentHome();

    @Query("DELETE FROM SAVED_LOCATION")
    int clear();

    @Insert
    void insert(SavedLocation currentLocation);
}
