package com.icycoke.android.medication_reminder.pojo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "saved_location")
public class SavedLocation {

    @PrimaryKey(autoGenerate = true)
    public Integer index;

    @ColumnInfo(name = "latitude")
    public Double latitude;

    @ColumnInfo(name = "longitude")
    public Double longitude;
}
