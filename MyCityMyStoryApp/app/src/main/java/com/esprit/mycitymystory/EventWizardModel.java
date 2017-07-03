/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.esprit.mycitymystory;

import android.content.Context;

import com.esprit.mycitymystory.model.AbstractWizardModel;
import com.esprit.mycitymystory.model.AddEventPage1Info;
import com.esprit.mycitymystory.model.AddEventPage2Info;
import com.esprit.mycitymystory.model.AddEventPage3Info;
import com.esprit.mycitymystory.model.PageList;

public class EventWizardModel extends AbstractWizardModel {
    public EventWizardModel(Context context) {
        super(context);
    }

    @Override
    protected PageList onNewRootPageList() {
        return new PageList
                (
                        new AddEventPage2Info(this, "Date", mContext)
                                .setRequired(true),
                        new AddEventPage3Info(this, "Image", mContext)
                                .setRequired(true),
                        new AddEventPage1Info(this, "Imformation", mContext)
                                .setRequired(true));


    }


}
