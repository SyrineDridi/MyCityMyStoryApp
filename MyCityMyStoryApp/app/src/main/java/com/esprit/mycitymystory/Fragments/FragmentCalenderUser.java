package com.esprit.mycitymystory.Fragments;


import android.graphics.Color;

import java.text.SimpleDateFormat;

import android.os.Build;

import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.esprit.mycitymystory.Adapter.EventCalendarAdapter;
import com.esprit.mycitymystory.Entities.EntityEvent;
import com.esprit.mycitymystory.Entities.Participation;
import com.esprit.mycitymystory.R;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import java.util.List;
import java.util.Locale;


public class FragmentCalenderUser extends Fragment {


    private DatabaseReference mDatabase;
    private String mUserId, mEventId, mEvaluationId, mParticipationId;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    Participation participation;
    EntityEvent event;
    ArrayList<com.esprit.mycitymystory.Entities.Event> list_event = new ArrayList<>();

    TextView tvMounth;
    CompactCalendarView compactCalendarView;
    private List<Participation> participations;
    private List<Participation> test;
    private List<EntityEvent> listevent;
    RecyclerView recyclerView;
    EventCalendarAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calender_user, container, false);
        compactCalendarView = (CompactCalendarView) view.findViewById(R.id.compactcalendar_view);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewCalendar);
        test = new ArrayList<>();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        getAllParticipation();
        final View v = view;

        tvMounth = (TextView) view.findViewById(R.id.tvMounth);
        Date date = compactCalendarView.getFirstDayOfCurrentMonth();
        SimpleDateFormat df = new SimpleDateFormat("MMMM-yyyy");
        String reportDate = df.format(date);
        Log.d("", "Month was scrolled to: " + date);
        tvMounth = (TextView) v.findViewById(R.id.tvMounth);
        tvMounth.setText(reportDate);
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

                List<Event> events = compactCalendarView.getEvents(dateClicked);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                adapter = new EventCalendarAdapter(getActivity(), events);
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                Log.d("", "Day was clicked: " + dateClicked + " with events " + events);

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                SimpleDateFormat df = new SimpleDateFormat("MMMM-yyyy");
                String reportDate = df.format(firstDayOfNewMonth);
                Log.d("", "Month was scrolled to: " + firstDayOfNewMonth);
                tvMounth.setText(reportDate);
            }
        });
        return view;
    }
    public List<Participation> getAllParticipation() {
        participations = new ArrayList<>();
        mUserId = mFirebaseAuth.getCurrentUser().getUid();
        mParticipationId = (mEventId + "_" + mUserId);
        mDatabase.child("participations").
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot familleSnapshot : dataSnapshot.getChildren()) {
                            if ((familleSnapshot.child("user_id").getValue()).equals(mUserId)) {
                                participation = familleSnapshot.getValue(Participation.class);
                                getEvent(participation.getEvent_id());
                                participations.add(participation);

                            } else
                                System.out.println("coucou");
                        }
                        test = participations;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        System.out.println(test.size());
        return test;
    }

    public EntityEvent getEvent(final String EventId) {

        listevent = new ArrayList<>();
        mUserId = mFirebaseAuth.getCurrentUser().getUid();
        mParticipationId = (mEventId + "_" + mUserId);
        mDatabase.child("events").
                addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                        if ((eventSnapshot.getKey()).equals(EventId)) {
                                event = eventSnapshot.getValue(EntityEvent.class);
                                event.getStartDate();
                                event.getTitle();
                                String format = "dd MMM yyyy, hh:mmaa";
                                SimpleDateFormat formater = new SimpleDateFormat(format, Locale.ENGLISH);
                                long timeInMilliseconds = 0;
                                try {
                                    Date mDate = formater.parse(event.getStartDate());
                                    timeInMilliseconds = mDate.getTime();
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                Event ev = new Event(Color.GREEN, timeInMilliseconds, event);
                                compactCalendarView.addEvent(ev);

                            }
                        }
                        System.out.println("event" + listevent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
        System.out.println("test of " + event);
        return event;
    }
}
