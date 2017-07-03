package com.esprit.mycitymystory.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.esprit.mycitymystory.Adapter.EventsAdapter;
import com.esprit.mycitymystory.AddEvent;
import com.esprit.mycitymystory.Entities.EntityEvent;
import com.esprit.mycitymystory.R;

import com.esprit.mycitymystory.Utils.RecyclerItemClickListener;
import com.esprit.mycitymystory.Utils.Strings;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Fragment_MyEvents extends Fragment {
    public DatabaseReference mDatabase;

    private FirebaseAuth mFirebaseAuth;

    EventsAdapter adapter;


    private FirebaseUser mFirebaseUser;
    private List<EntityEvent> events;

    RecyclerView recyclerView;
    FloatingActionButton btnAddNewEvent;
    private Paint p = new Paint();

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
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewMyEvent);
        btnAddNewEvent = (FloatingActionButton) view.findViewById(R.id.btnAddNewEvent);

        btnAddNewEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddEvent.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        getAllEvent();


        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                      Toast.makeText(getActivity(),"to delete or update this event you have to swipe left or right",Toast.LENGTH_LONG).show();
                           }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        Toast.makeText(getActivity(), "to delete or update this event you have to swipe left or right", Toast.LENGTH_LONG).show();
                    }
                })
        );
        return view;
    }

    public void getAllEvent() {
        events = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new EventsAdapter(getActivity(), events);
        adapter.notifyDataSetChanged();
        mDatabase.child("events").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {

                    EntityEvent model = dataSnapshot.getValue(EntityEvent.class);
                    if (model.getUser_id().equals(mFirebaseUser.getUid())) {
                        events.add(model);
                    }
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    initSwipe();
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

    private void initSwipe() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        final int position = viewHolder.getAdapterPosition();
                        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                        if (direction == ItemTouchHelper.LEFT) {

                            dialog.setTitle("delete an event ");
                            dialog.setIcon(R.mipmap.ic_trush_48dp_red);
                            dialog.setMessage("Are you sure you want to remove this event !!");
                            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    getAllEvent();

                                }
                            })
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int which) {

                                            String id = events.get(position).getId();
                                            mDatabase.child("events").child(id).removeValue();
                                            adapter.removeItem(position);
                                            dialog.dismiss();
                                            getAllEvent();


                                        }
                                    });
                            dialog.show();


                        } else {

                            dialog.setTitle("update an event ");
                            dialog.setIcon(R.mipmap.ic_update_48dp_red);
                            dialog.setMessage("Are you sure you want to update this event !!");
                            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    getAllEvent();

                                }
                            })

                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int which) {

                                            FragmentUpdateEvent fragment = new FragmentUpdateEvent();
                                            FragmentManager fm = getActivity().getSupportFragmentManager();
                                            FragmentTransaction fragmentTransaction = fm.beginTransaction();
                                            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right,
                                                    R.anim.slide_out_left, R.anim.slide_out_left);
                                            fragmentTransaction.replace(R.id.mdContent, fragment.newInstance(events.get(position)), Strings.VENUE_FRAGMENT_TAG);
                                            fragmentTransaction.addToBackStack(Strings.VENUE_FRAGMENT_TAG);
                                            fragmentTransaction.commit();
                                        }
                                    });
                            dialog.show();
                        }
                    }

                    @Override
                    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                        Bitmap icon;
                        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                            View itemView = viewHolder.itemView;
                            float height = (float) itemView.getBottom() - (float) itemView.getTop();
                            float width = height / 3;

                            if (dX > 0) {
                                p.setColor(Color.parseColor("#388E3C"));
                                RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                                c.drawRect(background, p);
                                icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_edit_white_96dp);
                                RectF icon_dest = new RectF((float) itemView.getLeft() + width, (float) itemView.getTop() + width, (float) itemView.getLeft() + 2 * width, (float) itemView.getBottom() - width);
                                c.drawBitmap(icon, null, icon_dest, p);
                            } else {
                                p.setColor(Color.parseColor("#D32F2F"));
                                RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                                c.drawRect(background, p);
                                icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_delete_white_98dp);
                                RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                                c.drawBitmap(icon, null, icon_dest, p);
                            }
                        }
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    }
                };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

}
