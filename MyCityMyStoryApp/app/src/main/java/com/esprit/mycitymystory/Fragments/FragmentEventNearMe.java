package com.esprit.mycitymystory.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esprit.mycitymystory.DataSource.ParticipationDataSource;
import com.esprit.mycitymystory.Entities.EntityEvent;
import com.esprit.mycitymystory.Entities.Participation;
import com.esprit.mycitymystory.R;
import com.esprit.mycitymystory.Utils.GPSTracker;
import com.esprit.mycitymystory.Utils.Strings;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class FragmentEventNearMe extends Fragment implements OnMapReadyCallback {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    GPSTracker mGPS;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private Double longitude, latitude;
    public DatabaseReference mDatabase;
    private String mUserId;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private List<EntityEvent> events = new ArrayList<>();
    private List<EntityEvent> events_nearby;
    GoogleMap googlemap;
    private boolean mLocationPermissionGranted;
    ParticipationDataSource partDataSource;
    EntityEvent model;
    int i = 0;
    GPSTracker getloc;

    public static FragmentEventNearMe newInstance(String param1, String param2) {
        FragmentEventNearMe fragment = new FragmentEventNearMe();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_near_me, container, false);
        partDataSource = new ParticipationDataSource(getActivity());
        partDataSource.open();
        mGPS = new GPSTracker(getActivity());
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_event_near_me);
        mapFragment.getMapAsync(this);

        //    showSettingsAlert();


        return view;

    }

    @Override
    public void onMapReady(final GoogleMap map) {

        googlemap = map;
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1
        );

        if (!mGPS.canGetLocation) {
            mGPS.showSettingsAlert();
            mGPS = new GPSTracker(getActivity());
        }

        if (mGPS.canGetLocation()) {
            longitude = mGPS.getLongitude();
            latitude = mGPS.getLatitude();

            events = new ArrayList<>();
            mDatabase.child("events").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    try {
                        model = dataSnapshot.getValue(EntityEvent.class);

                        LatLng place = new LatLng(model.getLatitudeEvent(), model.getLongitudeEvent());
                        if ((Math.abs(model.getLongitudeEvent() - longitude) > 0) && (Math.abs(model.getLongitudeEvent() - longitude) < Math.abs(0.048777)) &&
                                (Math.abs(model.getLatitudeEvent() - latitude) > 0) && (Math.abs(model.getLatitudeEvent() - latitude) < Math.abs(0.045034))) {
                            if (place != null) {
                                events.add(model);
                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(place).title("Marker in " + place);

                                googlemap.addMarker(markerOptions);
                                googlemap.animateCamera(CameraUpdateFactory.newLatLngZoom(place, 12));
                                System.out.println("++++++++++++++++++++++++++++++++++++++++++++++" + model.getPlace() + model.getTitle());

                            }
                        }
                    } catch (Exception ex) {
                        Log.e("tagii", ex.getMessage());
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
            getEvents();
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(latitude, longitude)).
                    title("Marker in " + "MyPlace");
            map.addMarker(markerOptions);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));


            // Instantiates a new CircleOptions object and defines the center and radius
            CircleOptions circleOptions = new CircleOptions()
                    .center(new LatLng(latitude, longitude))
                    .radius(1000)
                    .fillColor(Color.parseColor("#33000059"))
                    .strokeColor(Color.RED);
            map.addCircle(circleOptions);


        }

    }

    public void getEvents() {
        googlemap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                i = 0;
                if (events.size() > 0) {


                    for (i = 0; i < events.size(); i++) {

                        if (events.get(i).getLatitudeEvent() == marker.getPosition().latitude && events.get(i).getLongitudeEvent() == marker.getPosition().longitude) {
                            final int j = i;
                            final AlertDialog.Builder dialogmodifsupp = new AlertDialog.Builder(getActivity());
                            dialogmodifsupp.setIcon(R.mipmap.ic_aler_48dp_red);
                            dialogmodifsupp.setPositiveButton("View", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Code to run on older devices

                                    AllDetailEventFragment fragment = new AllDetailEventFragment();
                                    FragmentManager fm = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                                    fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right,
                                            R.anim.slide_out_left, R.anim.slide_out_left);
                                    fragmentTransaction.add(R.id.mdContent, fragment.newInstance(events.get(j)), Strings.VENUE_FRAGMENT_TAG);
                                    fragmentTransaction.addToBackStack(Strings.VENUE_FRAGMENT_TAG);
                                    fragmentTransaction.commit();
                                }
                            });
                            dialogmodifsupp.setNegativeButton("Participate", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialog, int which) {

                                    final String mydate = java.text.SimpleDateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                                    mUserId = mFirebaseAuth.getCurrentUser().getUid();
                                    final String mEventId = events.get(j).getId();
                                    final String mParticipationId = (mEventId + "_" + mUserId);
                                    final Participation participation = new Participation();
                                    participation.setEvent_id(mEventId);
                                    participation.setId(mParticipationId);
                                    participation.setDate_participation(mydate);
                                    participation.setUser_id(mUserId);
                                    System.out.println("participation" + mParticipationId);

                                    mDatabase.child("participations").addChildEventListener(new ChildEventListener() {

                                        @Override
                                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                            Participation part = dataSnapshot.getValue(Participation.class);
                                            if (mParticipationId.equals(part.getId())) {
                                                System.out.println("already participated");
                                                final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                                alert.setTitle("Participate");
                                                alert.setMessage("you have already particpate on this event ");
                                                alert.setIcon(R.mipmap.ic_aler_48dp_red);
                                                alert.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                    }
                                                });
                                                alert.show();
                                            } else
                                                System.out.println("participation created");
                                            partDataSource = new ParticipationDataSource(getActivity());
                                            partDataSource.open();
                                            mDatabase.child("events").child(mEventId).child("nbAvailablePlaces").setValue(events.get(j).getNbAvailablePlaces() - 1);
                                            mDatabase.child("participations").child(mParticipationId).setValue(participation);
//                                            partDataSource.createParticipation(mUserId, mEventId, mydate,events.get(j).getStartDate());
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
                            });
                            dialogmodifsupp.setTitle(events.get(i).getTitle());
                            dialogmodifsupp.show();
                        }
                    }
                }
                return false;

            }


        });

    }


}


