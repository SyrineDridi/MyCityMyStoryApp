package com.esprit.mycitymystory.Fragments;
/**
 * Created by Syrine Dridi on 24/12/2016.
 */

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.esprit.mycitymystory.DataSource.ParticipationDataSource;
import com.esprit.mycitymystory.Entities.Evaluation;
import com.esprit.mycitymystory.Entities.Event;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Locale;

public class GetDeatilEentsAPi extends Fragment {
    ImageView image;
    LinearLayout l;
    MyApp app;
    private DatabaseReference mDatabase;
    private String mUserId, mEventId, mEvaluationId, mParticipationId;
    Participation participation;
    public static Event et;
    FloatingActionButton btnShareFb, btnInvitFb, btnParticipate;
    TextView Tvtitle, TvCat, TvEndDate, TvStartDate, TvNbPlaces, TvDesc;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    Evaluation evaluation;
    private static final String ARG_PARAM1 = "param1";
    private Event mParam1;
    Configuration config;
    RatingBar rating, ratingMoy;
    ParticipationDataSource partDataSource;

    public static GetDeatilEentsAPi newInstance(Event param1) {
        GetDeatilEentsAPi fragment = new GetDeatilEentsAPi();
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
        System.out.println("wisth" + view.getWidth());
        partDataSource = new ParticipationDataSource(getActivity());
        partDataSource.open();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        /***** get all the view  ****/
        l = (LinearLayout) view.findViewById(R.id.container_participating);
        l.setVisibility(View.GONE);
        image = (ImageView) view.findViewById(R.id.image);
        rating = (RatingBar) view.findViewById(R.id.rating);
        ratingMoy = (RatingBar) view.findViewById(R.id.ratingMoy);
        ratingMoy.setVisibility(View.GONE);
        rating.setVisibility(View.GONE);
        Tvtitle = (TextView) view.findViewById(R.id.Tvtitle);
        TvCat = (TextView) view.findViewById(R.id.TvCat);

        TvEndDate = (TextView) view.findViewById(R.id.TvEndDate);
        TvStartDate = (TextView) view.findViewById(R.id.TvStartDate);
        TvDesc = (TextView) view.findViewById(R.id.TvDesc);
        btnInvitFb = (FloatingActionButton) view.findViewById(R.id.btnInvitfb);
        btnParticipate = (FloatingActionButton) view.findViewById(R.id.btnParticipate);
        btnShareFb = (FloatingActionButton) view.findViewById(R.id.btnShareFb);
        /******* end get all the view *****/
        btnParticipate.setVisibility(View.GONE);
        /***** get detail event ****/
        Tvtitle.setText(mParam1.getEventName().toString());
        System.out.println(mParam1.getEventName());
        //TvDesc.setText();
        System.out.println("mParam1.getEndTimeDisplay() " + mParam1.getEndTimeDisplay());
        TvEndDate.setText(mParam1.getEndTimeDisplay());
        TvStartDate.setText(mParam1.getStartTimeDisplay());
        TvDesc.setText(mParam1.getDescription());
        TvCat.setVisibility(View.GONE);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        Picasso.with(getActivity()).load(mParam1.getThumbUrlLarge()).resize(width, width).into(image);
        /***end get detail event ***/
        /***** call map fragment ******/
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.map_detail, MapDetailEventAPI.newInstance(mParam1)).commit();
        System.out.println("Map +++++++++++++++++++++++++++++++++++++ " + mParam1.getLatitude() + "     " + mParam1.getLongitude());
        /***** end call map fragment ******/
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

        appLinkUrl = mParam1.getShareUrl();
        previewImageUrl = mParam1.getThumbUrlLarge();
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
                .setContentUrl(Uri.parse(mParam1.getShareUrl()))
                .setImageUrl(Uri.parse(mParam1.getThumbUrlLarge()))
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
    /****
     * end method rating event
     ****/
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
}