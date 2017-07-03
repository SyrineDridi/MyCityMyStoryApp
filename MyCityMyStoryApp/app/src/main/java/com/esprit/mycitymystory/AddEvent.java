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

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.esprit.mycitymystory.Entities.EntityEvent;
import com.esprit.mycitymystory.Entities.User;
import com.esprit.mycitymystory.Utils.MyApp;
import com.esprit.mycitymystory.Utils.psuhNotificationAllUsers;
import com.esprit.mycitymystory.model.AbstractWizardModel;
import com.esprit.mycitymystory.Interfaces.ModelCallbacks;
import com.esprit.mycitymystory.model.Page;
import com.esprit.mycitymystory.Interfaces.PageFragmentCallbacks;
import com.esprit.mycitymystory.Fragments.ReviewFragment;
import com.esprit.mycitymystory.Utils.StepPagerStrip;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class AddEvent extends FragmentActivity implements

        PageFragmentCallbacks,
        ReviewFragment.Callbacks,
        ModelCallbacks {
    ProgressDialog progress;
    String title;
    private ViewPager mPager;
    private MyPagerAdapter mPagerAdapter;
    private boolean mEditingAfterReview;
    String city, country;

    private AbstractWizardModel mWizardModel = new EventWizardModel(this);

    private boolean mConsumePageSelectedEvent;
    /*****
     * Firebase
     *****/
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference imagesRef;
    MyApp app;
    int position = 0;

/* end firebase */

    private EntityEvent event;
    String ur, newUrlImage;


    private Button mNextButton;
    private Button mPrevButton;
    public static final String TITLE_DATA_KEY = "title";
    private List<Page> mCurrentPageSequence;
    private StepPagerStrip mStepPagerStrip;
    Bitmap bmp;

    @Override
    public void onBackPressed() {


        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Cancel adding event");
        alert.setMessage("Are you sur !! you want to cancel ");
        alert.setIcon(R.mipmap.ic_aler_48dp_red);
        alert.setCancelable(false);
        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(AddEvent.this, MainAccueil.class);
                        startActivity(intent);

                    }
                });
        alert.show();


    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);
        app = (MyApp) this.getApplication();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference("images");
        imagesRef = storageRef.child("images_events/");
        getUserCItyCountry();
        if (savedInstanceState != null) {
            mWizardModel.load(savedInstanceState.getBundle("model"));
        }
        mWizardModel.registerListener(this);
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        mStepPagerStrip = (StepPagerStrip) findViewById(R.id.strip);
        mStepPagerStrip.setOnPageSelectedListener(new StepPagerStrip.OnPageSelectedListener() {
            @Override
            public void onPageStripSelected(int position) {
                position = Math.min(mPagerAdapter.getCount() - 1, position);
                if (mPager.getCurrentItem() != position) {
                    mPager.setCurrentItem(position);

                }
            }
        });


        mNextButton = (Button) findViewById(R.id.next_button);
        mPrevButton = (Button) findViewById(R.id.prev_button);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mStepPagerStrip.setCurrentPage(position);
                if (mConsumePageSelectedEvent) {
                    mConsumePageSelectedEvent = false;
                    return;
                }
                mEditingAfterReview = false;
                updateBottomBar();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPager.getCurrentItem() == mCurrentPageSequence.size()) {
                    AddEvent();

                } else {
                    if (mEditingAfterReview) {
                        mPager.setCurrentItem(mPagerAdapter.getCount() - 1);

                    } else {
                        mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                        position += 1;
                    }
                }
            }
        });
        mPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
                position -= 1;
            }
        });

        onPageTreeChanged();
        updateBottomBar();
    }

    @Override
    public void onPageTreeChanged() {
        mCurrentPageSequence = mWizardModel.getCurrentPageSequence();
        recalculateCutOffPage();
        mStepPagerStrip.setPageCount(mCurrentPageSequence.size() + 1); // + 1 = review step
        mPagerAdapter.notifyDataSetChanged();
        updateBottomBar();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void updateBottomBar() {
        position = mPager.getCurrentItem();
        if (mPager.getCurrentItem() == mCurrentPageSequence.size()) {
            mNextButton.setText("Save");
            mNextButton.setBackgroundColor(Color.RED);
            mNextButton.setBackgroundResource(R.drawable.finish_background);
            mNextButton.setTextAppearance(this, R.style.TextAppearanceFinish);
            app = (MyApp) this.getApplication();


        } else {
            mNextButton.setText(mEditingAfterReview
                    ? R.string.review
                    : R.string.next);

            mNextButton.setBackgroundResource(R.drawable.selectable_item_background);
            TypedValue v = new TypedValue();
            getTheme().resolveAttribute(android.R.attr.textAppearanceMedium, v, true);
            mNextButton.setTextAppearance(this, v.resourceId);
            mNextButton.setEnabled(position != mPagerAdapter.getCutOffPage());
        }

        mPrevButton.setVisibility(position <= 0 ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWizardModel.unregisterListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("model", mWizardModel.save());
    }

    @Override
    public AbstractWizardModel onGetModel() {
        return mWizardModel;
    }

    @Override
    public void onEditScreenAfterReview(String key) {
        for (int i = mCurrentPageSequence.size() - 1; i >= 0; i--) {
            if (mCurrentPageSequence.get(i).getKey().equals(key)) {
                mConsumePageSelectedEvent = true;
                mEditingAfterReview = true;
                mPager.setCurrentItem(i);
                updateBottomBar();
                break;
            }
        }
    }

    @Override
    public void onPageDataChanged(Page page) {
        if (page.isRequired()) {
            if (recalculateCutOffPage()) {
                mPagerAdapter.notifyDataSetChanged();
                updateBottomBar();
            }
        }
    }

    @Override
    public Page onGetPage(String key) {
        return mWizardModel.findByKey(key);
    }

    private boolean recalculateCutOffPage() {
        // Cut off the pager adapter at first required page that isn't completed
        int cutOffPage = mCurrentPageSequence.size() + 1;
        for (int i = 0; i < mCurrentPageSequence.size(); i++) {
            Page page = mCurrentPageSequence.get(i);

            if (page.isRequired() && !page.isCompleted()) {
                cutOffPage = i;
                break;
            }
            //  title = mCurrentPageSequence.get(2).getData().getString(TITLE_DATA_KEY);
        }

        if (mPagerAdapter.getCutOffPage() != cutOffPage) {
            mPagerAdapter.setCutOffPage(cutOffPage);
            return true;
        }

        return false;
    }

    public class MyPagerAdapter extends FragmentStatePagerAdapter {
        private int mCutOffPage;
        private Fragment mPrimaryItem;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i >= mCurrentPageSequence.size()) {

                return new ReviewFragment();
            }
            return mCurrentPageSequence.get(i).createFragment();
        }

        @Override
        public int getItemPosition(Object object) {
            // TODO: be smarter about this
            if (object == mPrimaryItem) {
                // Re-use the current fragment (its position never changes)
                return POSITION_UNCHANGED;
            }

            return POSITION_NONE;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            mPrimaryItem = (Fragment) object;
        }

        @Override
        public int getCount() {
            if (mCurrentPageSequence == null) {
                return 0;
            }
            return Math.min(mCutOffPage + 1, mCurrentPageSequence.size() + 1);
        }

        public void setCutOffPage(int cutOffPage) {
            if (cutOffPage < 0) {
                cutOffPage = Integer.MAX_VALUE;
            }
            mCutOffPage = cutOffPage;
        }

        public int getCutOffPage() {
            return mCutOffPage;
        }
    }


    /*********
     * end upload image
     ************/
    public String uploadImage() {
        progress = new ProgressDialog(this);
        progress.setMessage("Uploading dat ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
        storageRef = storage.getReference("images_events/");
        imagesRef = storageRef;

        Bitmap bitmap;
        if (app.bmp != null) {
            bitmap = app.bmp;
        } else {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.no_image_available);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.child(ur).putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                taskSnapshot.getStorage();
                String newUrl = downloadUrl.toString();
                app.event.setCity(city);
                app.event.setCountry(country);
                app.event.setId(ur);
                app.event.setUser_id(mFirebaseAuth.getCurrentUser().getUid());
                app.event.setUrlImage(newUrl);
                mDatabase.child("events").child(ur).setValue(app.event).addOnCompleteListener(AddEvent.this ,new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progress.hide();
                            Toast.makeText(AddEvent.this, "success",
                                    Toast.LENGTH_SHORT).show();
                            SendNotification();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AddEvent.this);
                            builder.setMessage(task.getException().getMessage())
                                    .setTitle("erreur")
                                    .setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                });




                Intent intent = new Intent(AddEvent.this, MainAccueil.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        return "ok";
    }
    /********* end upload image  ************/
    /*********
     * Add event
     ************/
    public void AddEvent() {
        ur = mDatabase.child("events").push().getKey();
        uploadImage();
    }

    public void getUserCItyCountry() {
        String id = mFirebaseUser.getUid();
        DatabaseReference users = mDatabase.child("users").child(id);
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                city = user.getCity();
                country = user.getCountry();
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.d("User", firebaseError.getMessage());
            }
        });


    }
    /***** end ass event *********/

    /**********SendNotification All Users in the same City ***********/

    /*******************************************************************/


    public void SendNotification () {
        mDatabase.child("Tokens").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    //Token model = dataSnapshot.getValue(Token.class);
                    final String token = dataSnapshot.child("Token").getValue().toString();
                    final String keyUserToken = dataSnapshot.getKey();

                    System.out.println(mDatabase.child("users").child(dataSnapshot.getKey()));
                    mDatabase.child("users").child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {


                            if (!(mFirebaseAuth.getCurrentUser().getUid().equals(dataSnapshot.getKey()))) {

                                final String firstname = dataSnapshot.child("firstname").getValue().toString();
                                final String country = dataSnapshot.child("country").getValue().toString().toUpperCase();

                                if (country.equals(app.event.getPlace().toUpperCase())) {

                                    new AsyncTask<Void, Void, Void>() {

                                        @Override
                                        protected Void doInBackground(Void... params) {
                                            psuhNotificationAllUsers.sendAndroidNotification(token,
                                                    app.event.getTitle(), "My City My Story");

                                            return null;
                                        }
                                    }.execute();
                                }
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                } catch (Exception ex) {
                    Log.e("tag", ex.getMessage());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }
    /*******************************************************************/
}
