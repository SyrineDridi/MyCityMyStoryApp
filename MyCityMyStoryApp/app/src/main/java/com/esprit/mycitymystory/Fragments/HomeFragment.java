package com.esprit.mycitymystory.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.icu.text.DateFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.esprit.mycitymystory.Adapter.EventRecycleviewAdapter;
import com.esprit.mycitymystory.Adapter.ExploreListviewAdapter;
import com.esprit.mycitymystory.Adapter.ExploreRecycleviewAdapter;
import com.esprit.mycitymystory.DataSource.ParticipationDataSource;
import com.esprit.mycitymystory.Entities.EntityEvent;
import com.esprit.mycitymystory.Entities.User;
import com.esprit.mycitymystory.Handler.EventHandler;
import com.esprit.mycitymystory.Handler.getStateOfCityHandler;
import com.esprit.mycitymystory.R;
import com.esprit.mycitymystory.Utils.AllEvent.ListEventsbyCity;
import com.esprit.mycitymystory.Utils.DribSearchView;
import com.esprit.mycitymystory.Utils.HttpHandler;
import com.esprit.mycitymystory.Utils.RecyclerItemClickListener;
import com.esprit.mycitymystory.Utils.Strings;
import com.esprit.mycitymystory.Utils.getStateOfCity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class HomeFragment extends Fragment {

    ArrayList<com.esprit.mycitymystory.Entities.Event> eventsAPI;
    public DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private ArrayList<EntityEvent> events;
    CheckBox cbToday, cbTomorrow, cbThisWeak;
    ExploreRecycleviewAdapter adapter;
    EventRecycleviewAdapter adapter_hor;
    private List<EntityEvent> events_search = new ArrayList<>();
    private List<com.esprit.mycitymystory.Entities.Event> events_api_search = new ArrayList<>();
    ParticipationDataSource partDataSource;
    ProgressBar mProgressBar, mProgressBarEvent;
    RecyclerView RecycLeViewEvent, recycler_view_horisatal;
    ;
    LinearLayoutManager horizontalLayoutManagaer;
    ExploreListviewAdapter exploreListviewAdapter;
    HomeFragment exploreFragment;
    View view;
    public static EditText editview;
    Toolbar toolbar;
    EntityEvent model;
    DribSearchView dribSearchView;
    NetworkInfo activeNetworkInfo;
    String city, country, state, city_u, country_u;

    String new_city, new_country, new_state;
    CountryPicker picker;
    Spinner city_spinner;
    String id;
    ArrayAdapter<String> adapter_city;
    ArrayList<String> cityList;
    private String TAG = "";
    TextView tvEvent, tvEventApi;


    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstance) {
        view = inflater.inflate(R.layout.home_fragment, viewGroup, false);

        partDataSource = new ParticipationDataSource(getActivity());
        partDataSource.open();
        tvEvent = (TextView) view.findViewById(R.id.tvEvent);
        tvEventApi = (TextView) view.findViewById(R.id.tvEventApi);
        tvEventApi.setVisibility(View.GONE);
        tvEvent.setVisibility(View.GONE);
        exploreFragment = this;
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        cityList = new ArrayList<String>();
        picker = CountryPicker.newInstance("Select Country");
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbarr);
        cbToday = (CheckBox) view.findViewById(R.id.cbToday);
        cbThisWeak = (CheckBox) view.findViewById(R.id.cbThisWeak);
        cbTomorrow = (CheckBox) view.findViewById(R.id.cbTommorow);
        eventsAPI = new ArrayList<>();
        RecycLeViewEvent = (RecyclerView) view.findViewById(R.id.RecycLeViewEvent);
        recycler_view_horisatal = (RecyclerView) view.findViewById(R.id.recycler_view_horisatal);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mProgressBarEvent = (ProgressBar) view.findViewById(R.id.progress_bar_event);
        id = mFirebaseUser.getUid();


        dribSearchView = (DribSearchView) toolbar.findViewById(R.id.dribSearchView);
        editview = (EditText) toolbar.findViewById(R.id.editview);

        editview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                SearchEvent();
                SearchEventApi();
            }
        });
        horizontalLayoutManagaer = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

/* call method to show all events *****/

        TestInformation();

        getAllParticiaptionUser();
        getEventByCityCountry();
        getAllEvent();
        final AsyncGetEvent getEventsApi = new AsyncGetEvent();
        if (getEventsApi == null) {
            getEventsApi.execute();
        }

        /******* end call methode *****/
        /****** when click on recycle view ******/
        recycler_view_horisatal.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recycler_view_horisatal, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {


                        ConnectivityManager connectivityManager
                                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                        activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

                        if (activeNetworkInfo == null) {
                            final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setTitle("Problem of connection");
                            alert.setMessage("there is no connection ! Please ... try to connect your device on network then open the application");
                            alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getActivity().finish();
                                    System.exit(0);
                                }
                            });
                            alert.show();
                        } else {
                            inflateDetailApiFragment(events_api_search.get(position));
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );
        RecycLeViewEvent.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), RecycLeViewEvent, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        if (activeNetworkInfo == null) {


                            final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setTitle("Problem of connection");
                            alert.setMessage("there is no connection ! Please ... try to connect your device on network then open the application");
                            alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getActivity().finish();
                                    System.exit(0);
                                }
                            });
                            alert.show();
                        } else {
                            if (events_search.size() > 0)
                                inflateVenueFragment(events_search.get(position));
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );

        /**** end *****/

        /**** check today events ****/
        cbToday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getTodayEvent();
                } else {
                    getAllEvent();
                    getEventByCityCountry();
                    final AsyncGetEvent getEventsApi = new AsyncGetEvent();
                    if (getEventsApi == null) {
                        getEventsApi.execute();
                    }
                }

            }
        });
        /********* end check today event *****/

        /****** check tomorrow events ****/
        cbTomorrow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getTommorowEvent();
                } else {
                    getEventByCityCountry();
                    getAllEvent();
                    final AsyncGetEvent getEventsApi = new AsyncGetEvent();
                    if (getEventsApi == null) {
                        getEventsApi.execute();
                    }
                }

            }
        });
        /****** end check tomorrow events *******/

/****** check this weak events *****/
        cbThisWeak.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    getThisWeakEvent();
                } else {
                    getAllEvent();
                    getEventByCityCountry();
                    final AsyncGetEvent getEventsApi = new AsyncGetEvent();
                    if (getEventsApi == null) {
                        getEventsApi.execute();
                    }
                }
            }
        });
/****** end check this weak events *****/
        return view;
    }

    /***********
     * get User data
     ***********/
    public void getUserData() {
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
        if (city != null & country != null) {
            city_u = city;
            country_u = country;
        }

    }
    /********** end get user data ***********/

    /*******
     * call detail event created fragment
     ******/
    public void inflateVenueFragment(EntityEvent e) {
        // Code to run on older devices
        AllDetailEventFragment fragment = new AllDetailEventFragment();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right,
                R.anim.slide_out_left, R.anim.slide_out_left);
        fragmentTransaction.add(R.id.mdContent, fragment.newInstance(e), Strings.VENUE_FRAGMENT_TAG);
        fragmentTransaction.addToBackStack(Strings.VENUE_FRAGMENT_TAG);
        fragmentTransaction.commit();

        /***** end call detail event created fragment ******/

    }

    /*******
     * call detail event api  fragment
     ******/
    public void inflateDetailApiFragment(final com.esprit.mycitymystory.Entities.Event e) {
        // Code to run on older devices
        final GetDeatilEentsAPi fragment = new GetDeatilEentsAPi();
        final FragmentManager fm = getActivity().getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right,
                R.anim.slide_out_left, R.anim.slide_out_left);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                String jsonResultatDescriptionEvent = ListEventsbyCity.getDescriptionEvent(e.getEventIdFb());
                System.out.println("jsonResultatDescriptionEvent     ++++++++++++++++++++++++++++++++++++++" + jsonResultatDescriptionEvent);
                fragmentTransaction.add(R.id.mdContent, fragment.newInstance(EventHandler.getDescriptionEventJSON(e, jsonResultatDescriptionEvent)), Strings.VENUE_FRAGMENT_TAG);
                fragmentTransaction.addToBackStack(Strings.VENUE_FRAGMENT_TAG);
                fragmentTransaction.commit();
                return null;
            }
        }.execute();
    }
    /***** end call detail event created fragment ******/

    /******
     * get event created and put them in the recycle view
     *********/

    public void getAllEvent() {

        events = new ArrayList<>();
        exploreListviewAdapter = new ExploreListviewAdapter(events, getActivity());
        RecycLeViewEvent.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecycLeViewEvent.setItemAnimator(new DefaultItemAnimator());
        adapter = new ExploreRecycleviewAdapter(getActivity(), events);
        adapter.notifyDataSetChanged();
        final String id = mFirebaseUser.getUid();
        mDatabase.child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    if (id.equals(dataSnapshot.getKey())){
                    User user = dataSnapshot.getValue(User.class);
                        city_u = user.getCity();
                        country_u = user.getCountry();
                        System.out.println("cityy " + city + "country" + country);
                        mDatabase.child("events").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                try {
                                    model = dataSnapshot.getValue(EntityEvent.class);
                                    System.out.println("cityyyy xxxxxxxxxx" + city_u + "country  " + country_u);

                                    if (model.getCity().equals(city_u) && model.getCountry().equals(country_u)) {
                                        events.add(model);
                                    }
                                    if (events == null) {
                                        tvEvent.setVisibility(View.VISIBLE);
                                    }
                                    if (events != null)
                                        tvEvent.setVisibility(View.GONE);

                                } catch (Exception ex) {
                                    Log.e("tag", ex.getMessage());
                                }
                                RecycLeViewEvent.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                RecycLeViewEvent.setVisibility(View.VISIBLE);
                                mProgressBarEvent.setVisibility(View.GONE);
                                events_search = events;
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

    /**************************************************************/
    class AsyncGetEvent extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            String jsontStream = ListEventsbyCity.ListEventsbyCity(city, state, country);
            eventsAPI = EventHandler.getAllFormJson(jsontStream);
            events_api_search = eventsAPI;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            recycler_view_horisatal.setLayoutManager(horizontalLayoutManagaer);
            recycler_view_horisatal.setItemAnimator(new DefaultItemAnimator());
            adapter_hor = new EventRecycleviewAdapter(getActivity(), eventsAPI);
            recycler_view_horisatal.setAdapter(adapter_hor);
            adapter_hor.notifyDataSetChanged();
            recycler_view_horisatal.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);


        }
    }

    /***************************************************************/


    public void SearchEvent() {
        List<EntityEvent> midle = new ArrayList<>();
        RecycLeViewEvent.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecycLeViewEvent.setItemAnimator(new DefaultItemAnimator());
        adapter = new ExploreRecycleviewAdapter(getActivity(), midle);
        String input_search = editview.getText().toString();
        if (input_search.equals("")) {
            getAllEvent();
            return;
        }
        if (events_search.size() > 0) {
            for (EntityEvent ev : events_search) {
                if (ev.getTitle().toLowerCase().contains(input_search.toLowerCase())) {
                    midle.add(ev);
                }
            }
        }
        events_search = midle;
        RecycLeViewEvent.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public void SearchEventApi() {
        List<com.esprit.mycitymystory.Entities.Event> midle = new ArrayList<>();
        System.out.println("ttttttttt" + midle);
        recycler_view_horisatal.setLayoutManager(horizontalLayoutManagaer);
        recycler_view_horisatal.setItemAnimator(new DefaultItemAnimator());
        adapter_hor = new EventRecycleviewAdapter(getActivity(), midle);
        String input_search = editview.getText().toString();
        if (input_search.equals("")) {
            getEventByCityCountry();
            return;
        }
        if (events_api_search.size() > 0) {
            for (com.esprit.mycitymystory.Entities.Event ev : events_api_search) {
                if (ev.getEventName().toLowerCase().contains(input_search.toLowerCase())) {
                    midle.add(ev);
                }
            }
        }
        events_api_search = midle;
        recycler_view_horisatal.setAdapter(adapter_hor);
        adapter_hor.notifyDataSetChanged();

    }

    public void getEventByCityCountry() {
        String id = mFirebaseUser.getUid();
        DatabaseReference users = mDatabase.child("users").child(id);
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                city = user.getCity();
                country = user.getCountry();
                state = user.getState();

                System.out.println("current user " + city + state + country);
                final AsyncGetEvent getEventsApi = new AsyncGetEvent();
                getEventsApi.execute();
                if (eventsAPI != null) {
                    tvEventApi.setVisibility(View.GONE);
                } else {
                    tvEventApi.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.d("User", firebaseError.getMessage());
            }
        });
    }


    public void DialogResetCountry() {
        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());

        final Dialog ResetCountry = new Dialog(getActivity());
        // Set GUI of login screen
        ResetCountry.setContentView(R.layout.reset_country_city);
        ResetCountry.setTitle("To show the events on your city you have to choose your country and city ! ");


// Init button of GUI
        Button btnLogin = (Button) ResetCountry.findViewById(R.id.btnReset);
        Button btnCancel = (Button) ResetCountry.findViewById(R.id.btnCancelReset);
        city_spinner = (Spinner) ResetCountry.findViewById(R.id.city_spinner);
        adapter_city = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, cityList);
        // Drop down layout style - list view with radio button
        adapter_city.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        final EditText txtUsername = (EditText) ResetCountry.findViewById(R.id.txtEmail);
        txtUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    picker.show(getActivity().getSupportFragmentManager(), "COUNTRY_PICKER");
                    picker.isCancelable();
                    picker.setListener(new CountryPickerListener() {
                        @Override
                        public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                            // Implement your code here
                            txtUsername.setText(name);
                            new_country = name;
                            cityList.clear();
                            selectcity();
                        }
                    });
                }
            }
        });
// Attached listener for login GUI button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtUsername.getText().toString().trim().length() > 0) {
                    new_city = String.valueOf(city_spinner.getSelectedItem());
                    if (
                            city_spinner.getSelectedItem() == null) {
                        Toast.makeText(getActivity(), "you have to choose your city now", Toast.LENGTH_LONG).show();
                    }
                    new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected Void doInBackground(Void... params) {
                            String jsonState = getStateOfCity.getStateOfCity(new_city.replace(" ", "%20"));
                            new_state = getStateOfCityHandler.getAllFormJson(jsonState);
                            mDatabase.child("users").child(id).child("state").setValue(new_state);
                            mDatabase.child("users").child(id).child("country").setValue(new_country);
                            mDatabase.child("users").child(id).child("city").setValue(new_city);

                            return null;
                        }
                    }.execute();
                    ResetCountry.dismiss();
                    getAllEvent();
                    getEventByCityCountry();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("check your country")
                            .setTitle("Eror")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetCountry.dismiss();
            }
        });
        // Make dialog box visible.
        ResetCountry.show();
    }

    public void selectcity() {


        ArrayList<HashMap<String, String>> contactList;
        new HomeFragment.GetContacts().execute();
    }


    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getActivity(), "Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response

                try {
                    String jsonLocation = AssetJSONFile("countriesToCities.json", getActivity().getApplicationContext());
                    JSONObject jsonObj = new JSONObject(jsonLocation);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray(new_country);

                    for (int i = 0; i < contacts.length(); i++) {
                        String k = contacts.get(i).toString();
                        cityList.add(k);
                        System.out.println("just the result" + k);
                    }


                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    new Thread() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    };

                } catch (IOException e) {
                    e.printStackTrace();
                }



            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            System.out.print(cityList);
            adapter_city = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, cityList);
            // Drop down layout style - list view with radio button
            adapter_city.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // attaching data adapter to spinner
            city_spinner.setAdapter(adapter_city);
        }
    }

    public static String AssetJSONFile(String filename, Context context) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream file = manager.open(filename);
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();

        return new String(formArray);
    }

    /****
     * end select city
     ******/


    public void TestInformation() {
        String id = mFirebaseUser.getUid();
        DatabaseReference users = mDatabase.child("users").child(id);
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                if (user.getCity().equals("NONE") && user.getCountry().equals("NONE")
                        && user.getState().equals("NONE")) {
                    DialogResetCountry();
                } else {

                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.d("User", firebaseError.getMessage());
            }
        });
    }

    private void getAllParticiaptionUser() {

        long time = System.currentTimeMillis();
/*        List<Participation> listParticipation = partDataSource.getAllEvents();
        System.out.println("ttttttttttt" + listParticipation);
        for (Participation i : listParticipation) {

            i.getEvent_id();
            i.getDate_event_start();
            String format = "dd MMM yyyy, hh:mmaa";
            SimpleDateFormat formater = new SimpleDateFormat(format, Locale.ENGLISH);
            long timeInMilliseconds = 0;
            try {
                Date mDate = formater.parse(i.getDate_event_start());
                timeInMilliseconds = mDate.getTime();
                System.out.println("Date in milli :: " + timeInMilliseconds);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if ((time - timeInMilliseconds == TimeUnit.MINUTES.toMillis(58))) {
                showNotification();
                System.out.println("true");
            }
        }
    }

    public void showNotification() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 18);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getActivity())
                        .setSmallIcon(R.drawable.logo_my_city_my_story)
                        .setContentTitle("Notifications Example")
                        .setContentText("This is a test notification");

        Intent intent = new Intent(getActivity(), MainAccueil.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 0, pendingIntent);


        builder.setContentIntent(pendingIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());*/
    }

    public void getTodayEvent() {
        String format = "dd MMM yyyy, hh:mmaa";
        SimpleDateFormat formater = new SimpleDateFormat(format, Locale.ENGLISH);
        List<com.esprit.mycitymystory.Entities.Event> midleapi = new ArrayList<>();
        List<EntityEvent> midle = new ArrayList<>();

        recycler_view_horisatal.setLayoutManager(horizontalLayoutManagaer);
        recycler_view_horisatal.setItemAnimator(new DefaultItemAnimator());
        adapter_hor = new EventRecycleviewAdapter(getActivity(), midleapi);

        RecycLeViewEvent.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecycLeViewEvent.setItemAnimator(new DefaultItemAnimator());
        adapter = new ExploreRecycleviewAdapter(getActivity(), midle);
        Calendar c = Calendar.getInstance();
        if (events_search.size() > 0) {
            for (EntityEvent ev : events_search) {
                Date mDate = null;
                try {
                    mDate = formater.parse(ev.getStartDate());

                    if (mDate.getDay() == c.getTime().getDay() && mDate.getMonth() == c.getTime().getMonth() && mDate.getYear() == c.getTime().getYear()) {
                        midle.add(ev);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        if (events_api_search.size() > 0) {
            for (com.esprit.mycitymystory.Entities.Event evi : events_api_search) {
                //    midleapi.add(ev);
                String format1 = "EEE MMM d yyyy 'at' hh:mm aa";
                SimpleDateFormat formater1 = new SimpleDateFormat(format1, Locale.ENGLISH);
                try {
                    Date mDate2 = formater1.parse(evi.getStartTimeDisplay());

                    if (mDate2.getDay() == c.getTime().getDay() && mDate2.getMonth() == c.getTime().getMonth() && mDate2.getYear() == c.getTime().getYear()) {
                        midleapi.add(evi);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        events_search = midle;
        events_api_search = midleapi;
        RecycLeViewEvent.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recycler_view_horisatal.setAdapter(adapter_hor);
        adapter_hor.notifyDataSetChanged();
    }

    public void getTommorowEvent() {
        String format = "dd MMM yyyy, hh:mmaa";
        SimpleDateFormat formater = new SimpleDateFormat(format, Locale.ENGLISH);
        Calendar c = Calendar.getInstance();

        List<com.esprit.mycitymystory.Entities.Event> midleapi = new ArrayList<>();
        List<EntityEvent> midle = new ArrayList<>();

        recycler_view_horisatal.setLayoutManager(horizontalLayoutManagaer);
        recycler_view_horisatal.setItemAnimator(new DefaultItemAnimator());
        adapter_hor = new EventRecycleviewAdapter(getActivity(), midleapi);

        RecycLeViewEvent.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecycLeViewEvent.setItemAnimator(new DefaultItemAnimator());
        adapter = new ExploreRecycleviewAdapter(getActivity(), midle);
        if (events_search.size() > 0) {
            for (EntityEvent ev : events_search) {
                Date mDate = null;
                try {
                    mDate = formater.parse(ev.getStartDate());
                    Calendar c2 = Calendar.getInstance();
                    c2.add(Calendar.DAY_OF_YEAR, 1);
                    System.out.println("tomorrow day" + c2.getTime().getDay() + "kk" + mDate.getDay());
                    if (mDate.getDay() == c2.getTime().getDay() && mDate.getMonth() == c.getTime().getMonth() && mDate.getYear() == c.getTime().getYear()) {
                        midle.add(ev);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        if (events_api_search.size() > 0) {
            for (com.esprit.mycitymystory.Entities.Event evi : events_api_search) {
                String format1 = "EEE MMM d yyyy 'at' hh:mm aa";
                SimpleDateFormat formater1 = new SimpleDateFormat(format1, Locale.ENGLISH);
                try {
                    Date mDate2 = formater1.parse(evi.getStartTimeDisplay());
                    Calendar c2 = Calendar.getInstance();
                    c2.add(Calendar.DAY_OF_YEAR, 1);
                    if (mDate2.getDay() == c2.getTime().getDay() && mDate2.getMonth() == c.getTime().getMonth() && mDate2.getYear() == c.getTime().getYear()) {
                        midleapi.add(evi);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        events_search = midle;
        events_api_search = midleapi;
        RecycLeViewEvent.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recycler_view_horisatal.setAdapter(adapter_hor);
        adapter_hor.notifyDataSetChanged();

    }


    public void getThisWeakEvent() {
        String format = "dd MMM yyyy, hh:mmaa";
        SimpleDateFormat formater = new SimpleDateFormat(format, Locale.ENGLISH);
        Calendar c = Calendar.getInstance();

        List<com.esprit.mycitymystory.Entities.Event> midleapi = new ArrayList<>();
        List<EntityEvent> midle = new ArrayList<>();

        recycler_view_horisatal.setLayoutManager(horizontalLayoutManagaer);
        recycler_view_horisatal.setItemAnimator(new DefaultItemAnimator());
        adapter_hor = new EventRecycleviewAdapter(getActivity(), midleapi);

        RecycLeViewEvent.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecycLeViewEvent.setItemAnimator(new DefaultItemAnimator());
        adapter = new ExploreRecycleviewAdapter(getActivity(), midle);

        if (events_search.size() > 0) {
            for (EntityEvent ev : events_search) {
                Date mDate = null;
                try {
                    mDate = formater.parse(ev.getStartDate());
                    Calendar c1 = Calendar.getInstance();
                    c1.setTime(mDate);
                    if (c1.get(Calendar.WEEK_OF_YEAR) == c.get(Calendar.WEEK_OF_YEAR) && mDate.getMonth() == c.getTime().getMonth() && mDate.getYear() == c.getTime().getYear()) {
                        // this weak's events
                        midle.add(ev);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        if (events_api_search.size() > 0) {
            for (com.esprit.mycitymystory.Entities.Event evi : events_api_search) {
                //    midleapi.add(ev);
                String format1 = "EEE MMM d yyyy 'at' hh:mm aa";
                SimpleDateFormat formater1 = new SimpleDateFormat(format1, Locale.ENGLISH);
                try {
                    Date mDate2 = formater1.parse(evi.getStartTimeDisplay());
                    Calendar c1 = Calendar.getInstance();
                    c1.setTime(mDate2);
                    if (c1.get(Calendar.WEEK_OF_YEAR) == c.get(Calendar.WEEK_OF_YEAR) && mDate2.getMonth() == c.getTime().getMonth() && mDate2.getYear() == c.getTime().getYear()) {
                        // this weak's events
                        midleapi.add(evi);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        events_search = midle;
        events_api_search = midleapi;
        RecycLeViewEvent.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        recycler_view_horisatal.setAdapter(adapter_hor);
        adapter_hor.notifyDataSetChanged();

    }

}




