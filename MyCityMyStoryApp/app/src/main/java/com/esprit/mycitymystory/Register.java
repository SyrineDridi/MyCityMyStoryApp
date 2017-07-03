package com.esprit.mycitymystory;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.esprit.mycitymystory.Entities.EntityEvent;
import com.esprit.mycitymystory.Handler.getStateOfCityHandler;
import com.esprit.mycitymystory.Utils.MyApp;
import com.esprit.mycitymystory.Utils.getStateOfCity;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class Register extends FragmentActivity implements

        PageFragmentCallbacks,
        ReviewFragment.Callbacks,
        ModelCallbacks {
        ProgressDialog progress;

    String state;

    private ViewPager mPager;
    private Register.MyPagerAdapter mPagerAdapter;
    private boolean mEditingAfterReview;

    private AbstractWizardModel mWizardModel = new UserWizardModel(this);

    private boolean mConsumePageSelectedEvent;
    /*****
     * Firebase
     *****/
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    Configuration config;
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
        alert.setMessage("Are you sur !! you want to cancel this page ");
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
                        Intent intent = new Intent(Register.this, AboutAppActivity.class);
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
        imagesRef = storageRef.child("images_events");
        if (savedInstanceState != null) {
            mWizardModel.load(savedInstanceState.getBundle("model"));
        }
        mWizardModel.registerListener(this);
        mPagerAdapter = new Register.MyPagerAdapter(getSupportFragmentManager());
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
                    getStateOfuser();
                    Register();
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
        //  position = mPager.getCurrentItem();
        if (mPager.getCurrentItem() == mCurrentPageSequence.size()) {
            mNextButton.setText("Save");
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


    public String uploadImageUser() {


        storageRef = storage.getReference("images_users");
        String id = mFirebaseAuth.getCurrentUser().getUid();
        imagesRef = storageRef.child(id);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Bitmap bitmap;
        if (app.bmp != null) {
            bitmap = app.bmp;
        } else {
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.no_mage);
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imagesRef.putBytes(data);
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
                System.out.println(downloadUrl);
                ur = downloadUrl.toString();
                AddUserDetail(ur);
                Intent intent = new Intent(Register.this, MainAccueil.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });
        return ur;
    }

    public void AddUserDetail(String urlImage) {

        if (app.user.getFirstname().isEmpty() || app.user.getLastname().isEmpty()
                || app.user.getBirthday().isEmpty() || app.user.getCountry().isEmpty()
                || app.user.getCity().isEmpty() || app.user.getSexe().isEmpty()
                || app.user.getPhone().isEmpty()
                ) {

            Toast.makeText(this, "all fields are required", Toast.LENGTH_LONG).show();
            return;

        }

        app.user.setUrlImageUser(urlImage);
        app.user.setState(state);
        System.out.println("state of user " + app.user.getState());
        String email = mFirebaseAuth.getCurrentUser().getUid();
        mDatabase.child("users").child(email).setValue(app.user).addOnCompleteListener(Register.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progress.hide();
                    Toast.makeText(Register.this, "success",
                            Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    builder.setMessage(task.getException().getMessage())
                            .setTitle("erreur")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    public void Register() {
        progress = new ProgressDialog(this);
        progress.setMessage("Uploading dat ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
        System.out.println("test");

        if (app.email.isEmpty() || app.password.isEmpty()) {

            Toast.makeText(this, "you have to enter your email and password ", Toast.LENGTH_LONG).show();
        } else {
            mFirebaseAuth.createUserWithEmailAndPassword(app.email, app.password)
                    .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Register.this, "success",
                                        Toast.LENGTH_SHORT).show();
                                /*******************************************/
                                String token = FirebaseInstanceId.getInstance().getToken();
                                mDatabase.child("Tokens").child(mFirebaseAuth.getCurrentUser().getUid()).child("Token").setValue(token);
                                /*******************************************/
                                uploadImageUser();

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                builder.setMessage(task.getException().getMessage())
                                        .setTitle("erreur")
                                        .setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    });
            System.out.println("test");
        }
    }

    public void getStateOfuser() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                String jsonState = getStateOfCity.getStateOfCity(app.user.getCity().replace(" ", "%20"));
                state = getStateOfCityHandler.getAllFormJson(jsonState);
                return null;
            }
        }.execute();


    }


}
