package com.esprit.mycitymystory;

import android.content.Context;

import com.esprit.mycitymystory.model.AbstractWizardModel;
import com.esprit.mycitymystory.model.PageList;
import com.esprit.mycitymystory.model.RegisterPage1Info;
import com.esprit.mycitymystory.model.RegisterPage2Info;

/**
 * Created by Syrine Dridi on 18/12/2016.
 */

public class UserWizardModel  extends AbstractWizardModel {
    public UserWizardModel(Context context) {
        super(context);
    }

    @Override
    protected PageList onNewRootPageList() {
        return new PageList
                (
                        new RegisterPage1Info(this, "User ", mContext)
                                .setRequired(true),
                        new RegisterPage2Info(this, "More", mContext)
                                .setRequired(true));
    }



    }