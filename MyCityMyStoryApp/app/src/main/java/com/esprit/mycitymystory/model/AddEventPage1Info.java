package com.esprit.mycitymystory.model;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.esprit.mycitymystory.Interfaces.ModelCallbacks;
import com.esprit.mycitymystory.Utils.MyApp;
import com.esprit.mycitymystory.Fragments.FragmentAddEventPage1;

import java.util.ArrayList;

/**
 * Created by Syrine Dridi on 04/12/2016.
 */

public class AddEventPage1Info extends Page {
    MyApp app;
    public static final String TITLE_DATA_KEY = "title";
    public static final String DESC_DATA_KEY = "description";
    public static final String CATEGORY_DATA_KEY = "category";
    public static final String PLACE_DATA_KEY = "place";
    public static final String LONGITUDE_DATA_KEY = "longitude";
    public static final String LATITUDE_DATA_KEY = "latitude";
    Context context ;

    public AddEventPage1Info(ModelCallbacks callbacks, String title, Context context) {
        super(callbacks, title);
        this.context = context;
    }

    @Override
    public Fragment createFragment() {
        return FragmentAddEventPage1.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        app = (MyApp) context.getApplicationContext();


        app.event.setTitle(mData.getString(TITLE_DATA_KEY));
        app.event.setDescription(mData.getString(DESC_DATA_KEY));
        app.event.setCategory(mData.getString(CATEGORY_DATA_KEY));
        app.event.setPlace(mData.getString(PLACE_DATA_KEY));


        dest.add(new ReviewItem("title", mData.getString(TITLE_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("description", mData.getString(DESC_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("category", mData.getString(CATEGORY_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("place", mData.getString(PLACE_DATA_KEY), getKey(), -1));
    }

    @Override
    public boolean isCompleted() {
        return
                ((!TextUtils.isEmpty(mData.getString(TITLE_DATA_KEY)))
                && (!TextUtils.isEmpty(mData.getString(DESC_DATA_KEY)))
                        && (!TextUtils.isEmpty(mData.getString(CATEGORY_DATA_KEY)))
                        && (!TextUtils.isEmpty(mData.getString(PLACE_DATA_KEY)))
                );
    }
}
