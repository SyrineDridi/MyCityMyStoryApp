package com.esprit.mycitymystory.Fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esprit.mycitymystory.Adapter.EventsAdapter;
import com.esprit.mycitymystory.Entities.EntityEvent;
import com.esprit.mycitymystory.R;
import com.esprit.mycitymystory.Utils.RecyclerItemClickListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;


public class Fragment_List_EventNearMe extends Fragment {
    private List<EntityEvent> events_search;
    RecyclerView recyclerView;
    FloatingActionButton btnAddNewEvent;

    private Double longitude, latitude;
    public DatabaseReference mDatabase;
    private String mUserId;
    private FirebaseAuth mFirebaseAuth;
    EventsAdapter adapter;
    private FirebaseUser mFirebaseUser;
    private List<EntityEvent> events = new ArrayList<>();
    private List<EntityEvent> eventsNearMe;
    GoogleMap googlemap;
    Query query;

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
        View view = inflater.inflate(R.layout.fragment_my_event, container, false);



        btnAddNewEvent = (FloatingActionButton) view.findViewById(R.id.btnAddNewEvent);
        events = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewMyEvent);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new EventsAdapter(getActivity(), events);
        recyclerView.setAdapter(adapter);
        getEventNearMe();
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                 //       getFragmentManager().beginTransaction().replace(R.id.container_add_event,
                          //      DetailEventFragment.newInstance(events.get(position)))
                 //               .commit();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
        return view;
    }
    public void getEventNearMe() {

     /*   GetLocation getloc = new GetLocation(getActivity(),getActivity());

       longitude = getloc.getMylocation().getLongitude();
        latitude=getloc.getMylocation().getLatitude();
*/
        longitude = 9.887951798737049;
        latitude = 37.24520511815063;
        System.out.println("ttttttt");
        mDatabase.child("events").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    System.out.println("ttttttt");
                    final EntityEvent model = dataSnapshot.getValue(EntityEvent.class);
                    if ((Math.abs(model.getLongitudeEvent()- longitude)> 0 )&&(Math.abs(model.getLongitudeEvent()- longitude)<Math.abs(2.058777)) &&
                            (Math.abs(model.getLatitudeEvent() - latitude) > 0) && (Math.abs(model.getLatitudeEvent() - latitude) < Math.abs(2.055034))){
                        System.out.println("this is "+model);
                        System.out.println("latitude");
                        events.add(model);
                    }
                    recyclerView.setAdapter(adapter);
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


    }


}
