package com.esprit.mycitymystory.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReciever extends BroadcastReceiver {

    public MyReciever() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {


        Intent intent1 = new Intent(context, NotificationService.class);

        context.startService(intent1);

    }
}