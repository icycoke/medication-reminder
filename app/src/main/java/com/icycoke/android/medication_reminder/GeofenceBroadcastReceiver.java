package com.icycoke.android.medication_reminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.GeofencingRequest;
import com.icycoke.android.medication_reminder.util.HighPriorityNotificationUtil;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = GeofencingRequest.class.getSimpleName();
    private HighPriorityNotificationUtil highPriorityNotificationUtil;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: geofence triggered");

        if (highPriorityNotificationUtil == null) {
            highPriorityNotificationUtil = new HighPriorityNotificationUtil(context);
        }

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.d(TAG, "onReceive: error receiving geofence event");
            return;
        }

        int transitionType = geofencingEvent.getGeofenceTransition();

        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                highPriorityNotificationUtil.sendHighPriorityNotification(
                        context.getResources().getString(R.string.notification_title_exit_geofence),
                        context.getResources().getString(R.string.notification_text_exit_geofence)
                );
                break;
        }
    }
}
