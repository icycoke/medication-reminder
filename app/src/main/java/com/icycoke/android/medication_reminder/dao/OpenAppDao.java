package com.icycoke.android.medication_reminder.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.icycoke.android.medication_reminder.pojo.OpenApp;

import java.util.Date;

@Dao
public interface OpenAppDao {

    @Query("SELECT COUNT(*) FROM open_app")
    int getOpenTimes();

    @Query("SELECT date FROM open_app ORDER BY `index` DESC LIMIT 0,1")
    Date getLastOpenDate();

    @Query("DELETE FROM open_app")
    int clear();

    @Insert
    void insert(OpenApp... openApp);
}
