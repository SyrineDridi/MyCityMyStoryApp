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

package com.esprit.mycitymystory.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.esprit.mycitymystory.Entities.EntityEvent;
import com.esprit.mycitymystory.R;
import com.esprit.mycitymystory.Utils.MyApp;
import com.esprit.mycitymystory.model.AbstractWizardModel;
import com.esprit.mycitymystory.Interfaces.ModelCallbacks;
import com.esprit.mycitymystory.model.Page;
import com.esprit.mycitymystory.model.ReviewItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ReviewFragment extends ListFragment implements ModelCallbacks {
    private Callbacks mCallbacks;
    private AbstractWizardModel mWizardModel;
    private List<ReviewItem> mCurrentReviewItems;
    /*****
     * Firebase
     *****/
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference imagesRef;
    ImageView imgEvent;

/* end firebase */

    private EntityEvent event;
    String ur, newUrlImage;
  MyApp app;
    private ReviewAdapter mReviewAdapter;

    public ReviewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReviewAdapter = new ReviewAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_page, container, false);
        imgEvent = (ImageView) rootView.findViewById(R.id.ImgEvent);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference("images");
        imagesRef = storageRef.child("images_events");
        app = (MyApp) getActivity().getApplication();
//        app.image.setDrawingCacheEnabled(true);
   //     app.image.buildDrawingCache();
   //     Bitmap scaledBitmap = app.image.getDrawingCache();
     imgEvent.setImageBitmap(app.bmp);


        TextView titleView = (TextView) rootView.findViewById(android.R.id.title);
        titleView.setText("Your Detail");
        titleView.setTextColor(getResources().getColor(R.color.cpb_red_dark));

        ListView listView = (ListView) rootView.findViewById(android.R.id.list);
        setListAdapter(mReviewAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        return rootView;
    }



    /***** end ass event *********/
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof Callbacks)) {
            throw new ClassCastException("Activity must implement fragment's callbacks");
        }

        mCallbacks = (Callbacks) activity;

        mWizardModel = mCallbacks.onGetModel();
        mWizardModel.registerListener(this);
        onPageTreeChanged();
    }

    @Override
    public void onPageTreeChanged() {
        onPageDataChanged(null);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;

        mWizardModel.unregisterListener(this);
    }

    @Override
    public void onPageDataChanged(Page changedPage) {
        ArrayList<ReviewItem> reviewItems = new ArrayList<ReviewItem>();
        for (Page page : mWizardModel.getCurrentPageSequence()) {
            page.getReviewItems(reviewItems);
        }
        Collections.sort(reviewItems, new Comparator<ReviewItem>() {
            @Override
            public int compare(ReviewItem a, ReviewItem b) {
                return a.getWeight() > b.getWeight() ? +1 : a.getWeight() < b.getWeight() ? -1 : 0;
            }
        });
        mCurrentReviewItems = reviewItems;

        if (mReviewAdapter != null) {
            mReviewAdapter.notifyDataSetInvalidated();
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mCallbacks.onEditScreenAfterReview(mCurrentReviewItems.get(position).getPageKey());
    }

    public interface Callbacks {
        AbstractWizardModel onGetModel();

        void onEditScreenAfterReview(String pageKey);
    }

    private class ReviewAdapter extends BaseAdapter {
        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public Object getItem(int position) {
            return mCurrentReviewItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mCurrentReviewItems.get(position).hashCode();
        }

        @Override
        public View getView(int position, View view, ViewGroup container) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View rootView = inflater.inflate(R.layout.list_item_review, container, false);

            ReviewItem reviewItem = mCurrentReviewItems.get(position);
            System.out.println("yeees" + mCurrentReviewItems.get(position).getDisplayValue());
            String value = reviewItem.getDisplayValue();
            String title = reviewItem.getTitle();
            if (TextUtils.isEmpty(value)) {
                value = "(None)";
            }

            ((TextView) rootView.findViewById(android.R.id.text1)).setText(title);
            ((TextView) rootView.findViewById(android.R.id.text2)).setText(value);
            if (title.toString().equals(""))


                System.out.println(value);
            return rootView;
        }

        @Override
        public int getCount() {
            return mCurrentReviewItems.size();
        }
    }


    /********* end upload image ************/

}


