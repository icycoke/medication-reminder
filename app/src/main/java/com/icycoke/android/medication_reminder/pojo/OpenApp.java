package com.icycoke.android.medication_reminder.pojo;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "open_app")
public class OpenApp {

    @PrimaryKey(autoGenerate = true)
    public Integer index;

    @ColumnInfo(name = "date")
    public Date date;

    public OpenApp(Date date) {
        this.date = date;
    }
}
