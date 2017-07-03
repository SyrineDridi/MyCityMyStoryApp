package com.esprit.mycitymystory.model;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.esprit.mycitymystory.Interfaces.ModelCallbacks;
import com.esprit.mycitymystory.Utils.MyApp;
import com.esprit.mycitymystory.Fragments.FragmentAddEventPage2;

import java.util.ArrayList;

/**
 * Created by Syrine Dridi on 04/12/2016.
 */

public class AddEventPage2Info extends Page {

    public static final String STARTDATE_DATA_KEY = "satrt date";
    public static final String ENDDATE_DATA_KEY = "end date";
    public static final String STARTTIME_DATA_KEY = "start time";
    public static final String ENDTIME_DATA_KEY = "end time";
    Context context ;
    MyApp app;
    public AddEventPage2Info(ModelCallbacks callbacks, String title, Context context) {
        super(callbacks, title);
        this.context = context;
    }

    @Override
    public Fragment createFragment() {
        return FragmentAddEventPage2.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {

        app = (MyApp) context.getApplicationContext();

        app.event.setStartDate((mData.getString(STARTDATE_DATA_KEY))+", "+(mData.getString(STARTTIME_DATA_KEY)));
        app.event.setEndDate((mData.getString(ENDDATE_DATA_KEY))+", "+(mData.getString(ENDTIME_DATA_KEY)));

        dest.add(new ReviewItem("event start date", mData.getString(STARTDATE_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("event end date", mData.getString(ENDDATE_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("event start time", mData.getString(STARTTIME_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("event end time", mData.getString(ENDTIME_DATA_KEY), getKey(), -1));
    }

    @Override
    public boolean isCompleted() {
        return !TextUtils.isEmpty(mData.getString(STARTDATE_DATA_KEY));
    }

}
