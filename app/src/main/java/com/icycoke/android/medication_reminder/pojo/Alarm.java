package com.icycoke.android.medication_reminder.pojo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "alarm")
public class Alarm {

    @PrimaryKey(autoGenerate = true)
    public Integer index;

    // TODO
}
