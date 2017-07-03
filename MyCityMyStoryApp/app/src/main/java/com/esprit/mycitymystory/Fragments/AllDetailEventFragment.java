package com.esprit.mycitymystory.Fragments;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.esprit.mycitymystory.DataSource.ParticipationDataSource;
import com.esprit.mycitymystory.Entities.EntityEvent;
import com.esprit.mycitymystory.Entities.Evaluation;
import com.esprit.mycitymystory.Entities.Participation;
import com.esprit.mycitymystory.R;
import com.esprit.mycitymystory.Utils.MyApp;
import com.esprit.mycitymystory.services.MyReciever;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.AppInviteDialog;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * Fragment for displaying & Managing Venue Information Tab
 *
 * @author luchfilip
 */
public class AllDetailEventFragment extends Fragment {
    ImageView image;
    MyApp app;
    private DatabaseReference mDatabase;
    private String mUserId, mEventId, mEvaluationId, mParticipationId;
    Participation participation;
    public static EntityEvent et;
    FloatingActionButton btnShareFb, btnInvitFb, btnParticipate;
    TextView Tvtitle, TvCat, TvEndDate, TvStartDate, TvNbPlaces, TvDesc, tvNbParticipant;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    Evaluation evaluation;
    private static final String ARG_PARAM1 = "param1";
    private EntityEvent mParam1;
    Configuration config;
    private List<Participation> participations = new ArrayList<>();
    RatingBar rating, ratingMoy;
    ParticipationDataSource partDataSource;


    public static AllDetailEventFragment newInstance(EntityEvent param1) {
        AllDetailEventFragment fragment = new AllDetailEventFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);
        et = param1;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        config = new Configuration(getResources().getConfiguration());
        config.locale = Locale.ENGLISH;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getParcelable(ARG_PARAM1);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstance) {
        View view = inflater.inflate(R.layout.all_detail_event_layout, viewGroup, false);
        partDataSource = new ParticipationDataSource(getActivity());
        partDataSource.open();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();


/***** get all the view  ****/
        image = (ImageView) view.findViewById(R.id.image);
        rating = (RatingBar) view.findViewById(R.id.rating);
        ratingMoy = (RatingBar) view.findViewById(R.id.ratingMoy);
        ratingMoy.setEnabled(false);
        Tvtitle = (TextView) view.findViewById(R.id.Tvtitle);
        TvCat = (TextView) view.findViewById(R.id.TvCat);
        TvEndDate = (TextView) view.findViewById(R.id.TvEndDate);
        TvStartDate = (TextView) view.findViewById(R.id.TvStartDate);
        tvNbParticipant = (TextView) view.findViewById(R.id.tvNbParticipant);
        TvDesc = (TextView) view.findViewById(R.id.TvDesc);
        btnInvitFb = (FloatingActionButton) view.findViewById(R.id.btnInvitfb);
        btnParticipate = (FloatingActionButton) view.findViewById(R.id.btnParticipate);
        btnShareFb = (FloatingActionButton) view.findViewById(R.id.btnShareFb);
/******* end get all the view *****/

        /***** get detail event ****/
        Tvtitle.setText(mParam1.getTitle().toString());
        TvDesc.setText(mParam1.getDescription().toString());
        TvCat.setText(mParam1.getCategory().toString());
        TvEndDate.setText(mParam1.getEndDate().toString());
        TvStartDate.setText(mParam1.getStartDate().toString());
        getAllParticipationUser();
        calcMoyRating();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Picasso.with(getActivity()).load(mParam1.getUrlImage()).resize(width, width / 2).into(image);
        /***end get detail event ***/

        /***** call map fragment ******/
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1
        );
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.map_detail, new MapDetailEvent()).commit();
        /***** end call map fragment ******/

        /**** call method participate *****/
        btnParticipate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                ParticipateEvent();
            }
        });

        /**** end call method participate *****/
        /**** call method rating *****/
        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                RatingEvent();
            }
        });

        /**** end call method rating *****/


        /**** if rating exist ********/
        RatingExist();
        /**** end if rating exist ****/

        /**** if participation exist ***/
        participationExist();
        /**** end if participation exist ****/


        /**********************  Notification ************************************/

        Calendar now = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            now = Calendar.getInstance();

            Calendar current = Calendar.getInstance();
            //   now.set(now.get( datePicker.getYear()), now.get(datePicker.getMonth()), now.get(datePicker.getDayOfMonth()), the hour, and the minute);
            if (now.compareTo(current) <= -1) {
                Toast.makeText(getActivity(), "invalid time", Toast.LENGTH_LONG).show();
            } else {
                Intent notifyIntent = new Intent(getActivity(), MyReciever.class);
                notifyIntent.putExtra("title", "mmmmm");
                PendingIntent pendingIntent = PendingIntent.getBroadcast
                        (getActivity(), 111, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC, now.getTimeInMillis() + 10000, pendingIntent);
            }
        }
        /************************************************************************/
        /****************************** Inviter freinds ************************************************/

        final String appLinkUrl, previewImageUrl;

        appLinkUrl = "https://www.facebook.com/moi.737";
        previewImageUrl = mParam1.getUrlImage();
        btnInvitFb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                if (AppInviteDialog.canShow()) {
                    AppInviteContent content = new AppInviteContent.Builder()
                            .setApplinkUrl(appLinkUrl)
                            .setPreviewImageUrl(previewImageUrl)
                            .build();
                    AppInviteDialog.show(getActivity(), content);
                }
            }
        });
        /*****************************end inviter freinds **********************************************/

        /******************share with facebook *************************/
        final ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
                .setContentTitle("just try")
                .setContentDescription("Sorry !! but i'm trying to share an event of my android application ! ")
                .setContentUrl(Uri.parse("https://www.facebook.com/moi.737"))
                .setImageUrl(Uri.parse(mParam1.getUrlImage()))
                .build();
        btnShareFb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ShareDialog shareDialog = new ShareDialog(getActivity());
                shareDialog.show(getActivity(), shareLinkContent);
            }
        });
        /********************End share with facebook ************************************/

        return view;
    }

    /******
     * begin method rating event
     ******/
    public void RatingEvent() {
        evaluation = new Evaluation();

        float note = rating.getRating();
        mUserId = mFirebaseAuth.getCurrentUser().getUid();
        mEventId = mParam1.getId();
        mEvaluationId = (mEventId + "_" + mUserId);
        evaluation.setEvent_id(mEventId);
        evaluation.setId(mEvaluationId);
        evaluation.setNote(note);
        evaluation.setUser_id(mUserId);
        mDatabase.child("evaluations").child(mEvaluationId).setValue(evaluation);
        calcMoyRating();

    }

    /*****
     * end method rating event
     ****/

    public void RatingExist() {
        evaluation = new Evaluation();
        mUserId = mFirebaseAuth.getCurrentUser().getUid();
        mEventId = mParam1.getId();
        System.out.println("yes this an event id "+mParam1.getId());
        mEvaluationId = (mEventId + "_" + mUserId);

        mDatabase.child("evaluations").addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Evaluation evaluation = dataSnapshot.getValue(Evaluation.class);
                if (mEvaluationId.equals(evaluation.getId())) {
                    rating.setRating(evaluation.getNote());
                    System.out.println("rating existe " + dataSnapshot.getValue());
                } else
                    // btnParticipate.setVisibility(View.GONE);
                    System.out.println("coucou");
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
    /****** end method participate *****/
    /*****
     * requierment calendar
     ****/
    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    /**** and requierment calendar *****/


    /****** begin method participate on event *******/
    public void ParticipateEvent() {

        partDataSource = new ParticipationDataSource(getActivity());
        partDataSource.open();
        String mydate = java.text.SimpleDateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        mUserId = mFirebaseAuth.getCurrentUser().getUid();
        mEventId = mParam1.getId();
        mParticipationId = (mEventId + "_" + mUserId);
        participation = new Participation();
        participation.setEvent_id(mEventId);
        participation.setId(mParticipationId);
        participation.setDate_participation(mydate);
        participation.setUser_id(mUserId);
        mDatabase.child("events").child(mEventId).child("nbAvailablePlaces").setValue(mParam1.getNbAvailablePlaces() - 1);
        mDatabase.child("participations").child(mParticipationId).setValue(participation);
        String date_strat = mParam1.getStartDate().toString() ;
//        partDataSource.createParticipation(mUserId, mEventId, mydate ,date_strat );
    }
/***** end method participate event ******/


    /*******
     * begin method participation exist to verify if user have already paticipated
     *****/
    public void participationExist() {
        mUserId = mFirebaseAuth.getCurrentUser().getUid();
        mEventId = mParam1.getId();
        mParticipationId = (mEventId + "_" + mUserId);
        mDatabase.child("participations").addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Participation participation = dataSnapshot.getValue(Participation.class);
                if (mParticipationId.equals(participation.getId())) {

                    System.out.println("participation existe" + dataSnapshot.getValue());
                    btnParticipate.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                    btnParticipate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDatabase.child("participations").child(mParticipationId).removeValue();
                            btnParticipate.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                        }
                    });

                } else
                    // btnParticipate.setVisibility(View.GONE);
                    System.out.println("coucou");
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

    /******
     * end method participate
     *****/
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity().getApplicationContext());

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog
                .setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        getActivity().getApplicationContext().startActivity(intent);
                    }
                });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // Showing Alert Message
        alertDialog.show();
    }

    public void calcMoyRating() {

        mDatabase.child("evaluations").addChildEventListener(new ChildEventListener() {
            List<Evaluation> evaluations = new ArrayList<>();
            float sumR = 0;

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Evaluation evaluation = dataSnapshot.getValue(Evaluation.class);
                if (mParam1.getId().equals(evaluation.getEvent_id())) {
                    sumR += evaluation.getNote();
                    evaluations.add(evaluation);

                }
                float moy = sumR / evaluations.size();
                ratingMoy.setRating(moy);
                System.out.println("moyenne " + moy);

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

    public void getAllParticipationUser() {
        participations = new ArrayList<>();
        mDatabase.child("participations").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {

                    Participation model = dataSnapshot.getValue(Participation.class);
                    if (model.getEvent_id().equals(mParam1.getId())) {
                        participations.add(model);
                    }
                } catch (Exception ex) {
                    Log.e("tag", ex.getMessage());
                }
                tvNbParticipant.setText(String.valueOf(participations.size()) + "\nParticipations");
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


}


