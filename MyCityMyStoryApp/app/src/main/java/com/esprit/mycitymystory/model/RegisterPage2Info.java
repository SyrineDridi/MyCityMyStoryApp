package com.esprit.mycitymystory.model;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.esprit.mycitymystory.Interfaces.ModelCallbacks;
import com.esprit.mycitymystory.Utils.MyApp;
import com.esprit.mycitymystory.Fragments.FragmentRegisterPage2;

import java.util.ArrayList;

/**
 * Created by Syrine Dridi on 18/12/2016.
 */

public class RegisterPage2Info extends Page {
    MyApp app;
    public static final String COUNTRY_DATA_KEY = "country";
    public static final String CITY_DATA_KEY = "city";
    public static final String PHONE_DATA_KEY = "phone";
    public static final String BIRTHDAY_DATA_KEY = "birthday";
    public static final String SEX_DATA_KEY = "sex";
    Context context;

    public RegisterPage2Info(ModelCallbacks callbacks, String title, Context context) {
        super(callbacks, title);
        this.context = context;
    }

    @Override
    public Fragment createFragment() {
        return FragmentRegisterPage2.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        app = (MyApp) context.getApplicationContext();


        app.user.setPhone(mData.getString(PHONE_DATA_KEY));
        app.user.setCity(mData.getString(CITY_DATA_KEY));
        app.user.setCountry(mData.getString(COUNTRY_DATA_KEY));
        app.user.setBirthday(mData.getString(BIRTHDAY_DATA_KEY));
        app.user.setSexe(mData.getString(SEX_DATA_KEY));

        dest.add(new ReviewItem("country", mData.getString(COUNTRY_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("city", mData.getString(CITY_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("category", mData.getString(PHONE_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("birthday", mData.getString(BIRTHDAY_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("sex", mData.getString(SEX_DATA_KEY), getKey(), -1));
    }

    @Override
    public boolean isCompleted() {
        return
                ((!TextUtils.isEmpty(mData.getString(COUNTRY_DATA_KEY)))
                        && (!TextUtils.isEmpty(mData.getString(CITY_DATA_KEY)))
                        && (!TextUtils.isEmpty(mData.getString(PHONE_DATA_KEY)))
                        && (!TextUtils.isEmpty(mData.getString(BIRTHDAY_DATA_KEY)))
                        && (!TextUtils.isEmpty(mData.getString(SEX_DATA_KEY)))

                );
    }
}
