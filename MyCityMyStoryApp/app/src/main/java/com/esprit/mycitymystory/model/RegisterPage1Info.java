package com.esprit.mycitymystory.model;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.esprit.mycitymystory.Interfaces.ModelCallbacks;
import com.esprit.mycitymystory.Utils.MyApp;
import com.esprit.mycitymystory.Fragments.FragmentRegisterPage1;

import java.util.ArrayList;

/**
 * Created by Syrine Dridi on 18/12/2016.
 */

public class RegisterPage1Info extends Page {
    MyApp app;
    public static final String EMAIL_DATA_KEY = "email";
    public static final String PASSWORD_DATA_KEY = "password";
    public static final String FIRSTNAME_DATA_KEY = "firstname";
    public static final String LASTNAME_DATA_KEY = "lastname";

    Context context;

    public RegisterPage1Info(ModelCallbacks callbacks, String title, Context context) {
        super(callbacks, title);
        this.context = context;
    }

    @Override
    public Fragment createFragment() {
        return FragmentRegisterPage1.create(getKey());
    }

    @Override
    public void getReviewItems(ArrayList<ReviewItem> dest) {
        app = (MyApp) context.getApplicationContext();

        app.email = mData.getString(EMAIL_DATA_KEY);
        app.password = mData.getString(PASSWORD_DATA_KEY);
        app.user.setFirstname(mData.getString(FIRSTNAME_DATA_KEY));
        app.user.setLastname(mData.getString(LASTNAME_DATA_KEY));


        dest.add(new ReviewItem("email", mData.getString(EMAIL_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("password", mData.getString(PASSWORD_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("firstname", mData.getString(FIRSTNAME_DATA_KEY), getKey(), -1));
        dest.add(new ReviewItem("lastname", mData.getString(LASTNAME_DATA_KEY), getKey(), -1));
    }

    @Override
    public boolean isCompleted() {
        return ((!TextUtils.isEmpty(mData.getString(EMAIL_DATA_KEY)))
                &&(!TextUtils.isEmpty(mData.getString(PASSWORD_DATA_KEY)))
                &&(!TextUtils.isEmpty(mData.getString(FIRSTNAME_DATA_KEY)))
                &&(!TextUtils.isEmpty(mData.getString(LASTNAME_DATA_KEY)))

        );
    }
}
