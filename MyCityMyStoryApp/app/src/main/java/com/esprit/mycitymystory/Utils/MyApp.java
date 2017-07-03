package com.esprit.mycitymystory.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.widget.ImageView;

import com.esprit.mycitymystory.Entities.EntityEvent;
import com.esprit.mycitymystory.Entities.User;
import com.facebook.FacebookSdk;

/**
 * Created by Syrine on 15/11/2016.
 */

public class MyApp extends MultiDexApplication {

    public EntityEvent event;
    public User user;
    public ImageView image;
    public ImageView imageEvent;
    public Bitmap bmp;
    public String email, password;

    public EntityEvent getEvent() {
        return event;
    }

    public void setEvent(EntityEvent event) {
        this.event = event;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        event = new EntityEvent();
        user = new User();


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(MyApp.this);
    }


}