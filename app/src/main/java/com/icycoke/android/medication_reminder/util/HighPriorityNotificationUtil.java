package com.icycoke.android.medication_reminder.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.icycoke.android.medication_reminder.R;

import java.util.Random;

public class HighPriorityNotificationUtil extends ContextWrapper {

    private static final String TAG = HighPriorityNotificationUtil.class.getSimpleName();
    private static final String CHANNEL_NAME_HIGH_PRIORITY = "High Priority Channel";
    private static final String CHANNEL_ID_HIGH_PRIORITY =
            HighPriorityNotificationUtil.class.getPackage().getName() + CHANNEL_NAME_HIGH_PRIORITY;

    public HighPriorityNotificationUtil(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannels();
        }
    }

    public void sendHighPriorityNotification(String title, String text) {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID_HIGH_PRIORITY)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_notification_black)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle().setBigContentTitle(title).bigText(text))
                .build();

        NotificationManagerCompat.from(this).notify(new Random().nextInt(), notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannels() {
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID_HIGH_PRIORITY, CHANNEL_NAME_HIGH_PRIORITY,
                NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.setDescription("This is " + CHANNEL_NAME_HIGH_PRIORITY);
        notificationChannel.setLightColor(Color.RED);
        notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.createNotificationChannel(notificationChannel);
    }
}