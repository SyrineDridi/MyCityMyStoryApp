package com.esprit.mycitymystory.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationManagerCompat;

import com.esprit.mycitymystory.MainAccueil;
import com.esprit.mycitymystory.R;

/**
 * Created by Syrine Dridi on 29/11/2016.
 */

public class NotificationService extends IntentService {
    private static final int NOTIFICATION_ID = 3;

    public NotificationService() {
        super("MyNewIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("My Titel");
        builder.setContentText("This is the Body");
        builder.setSmallIcon(R.drawable.ic_audiotrack);
        builder.setAutoCancel(true);
        builder.setLights(Color.GREEN, 500, 500);

        Intent notifyIntent = new Intent(this, MainAccueil.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(NOTIFICATION_ID, notificationCompat);

    }
}
