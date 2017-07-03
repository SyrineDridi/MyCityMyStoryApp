package com.esprit.mycitymystory.model;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.esprit.mycitymystory.Interfaces.ModelCallbacks;
import com.esprit.mycitymystory.Utils.MyApp;
import com.esprit.mycitymystory.Fragments.FragmentAddEventPage3;

import java.util.ArrayList;

/**
 * Created by Syrine Dridi on 04/12/2016.
 */

public class AddEventPage3Info extends Page {

    public static final String NBPLACE_DATA_KEY = "nombre place";

    Context context ;
    MyApp app;
    public AddEventPage3Info(ModelCallbacks callbacks, String title, Context context) {
        super(callbacks, title);
        this.context = context;
    }

    @Override
    public Fragment createFragment() {
        return FragmentAddEventPage3.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        app = (MyApp) context.getApplicationContext();
        app.event.setNbPlaces(Integer.parseInt((mData.getString(NBPLACE_DATA_KEY))));
        dest.add(new ReviewItem("event nb places", mData.getString(NBPLACE_DATA_KEY), getKey(), -1));

    }

    @Override
    public boolean isCompleted() {
        return !TextUtils.isEmpty(mData.getString(NBPLACE_DATA_KEY));
    }
}
