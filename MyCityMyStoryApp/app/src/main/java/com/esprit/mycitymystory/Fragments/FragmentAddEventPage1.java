package com.esprit.mycitymystory.Fragments;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.esprit.mycitymystory.R;
import com.esprit.mycitymystory.Utils.GPSTracker;
import com.esprit.mycitymystory.Utils.MyApp;
import com.esprit.mycitymystory.model.AddEventPage1Info;
import com.esprit.mycitymystory.Interfaces.PageFragmentCallbacks;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Date;
import java.util.List;


public class FragmentAddEventPage1 extends Fragment implements OnMapReadyCallback {

    LinearLayout f;
    private double longitude, latitude;
    private double LongitudeEvent, LatitudeEvent;
    private static final String ARG_KEY = "key";
    MyApp app;
    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private AddEventPage1Info mPage;
    private TextView eTitleView;
    private TextView ePlaceView;
    private TextView eDescView;
    private Spinner eCatView;
    private Button btnChoosePlace;

    public static FragmentAddEventPage1 create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);
        FragmentAddEventPage1 fragment = new FragmentAddEventPage1();
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentAddEventPage1() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = (AddEventPage1Info) mCallbacks.onGetPage(mKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_event_page1, container, false);

        btnChoosePlace = (Button) rootView.findViewById(R.id.btnChoosePlace);
        eTitleView = ((TextView) rootView.findViewById(R.id.txtTitle));
        eTitleView.setText(mPage.getData().getString(AddEventPage1Info.TITLE_DATA_KEY));


        eDescView = ((TextView) rootView.findViewById(R.id.txtDesc));
        eDescView.setText(mPage.getData().getString(AddEventPage1Info.DESC_DATA_KEY));
/*******get place with map ***************/
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
        f = (LinearLayout) rootView.findViewById(R.id.map_container);
        f.setVisibility(View.GONE);
        ePlaceView = ((TextView) rootView.findViewById(R.id.txtPlace));
        ePlaceView.setInputType(InputType.TYPE_NULL);
        ePlaceView.setVisibility(View.GONE);
        btnChoosePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f.setVisibility(View.VISIBLE);
            }
        });
        ePlaceView.setText(mPage.getData().getString(AddEventPage1Info.PLACE_DATA_KEY));
/************ end get palce with map ***************/
        eCatView = ((Spinner) rootView.findViewById(R.id.spinner_cat));
        String.valueOf(eCatView.getSelectedItem());
        eCatView.setSelection(((ArrayAdapter<String>) eCatView.getAdapter()).
                getPosition(mPage.getData().getString(AddEventPage1Info.CATEGORY_DATA_KEY)));

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof PageFragmentCallbacks)) {
            throw new ClassCastException("Activity must implement PageFragmentCallbacks");
        }

        mCallbacks = (PageFragmentCallbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        eTitleView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(AddEventPage1Info.TITLE_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });


        eDescView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(AddEventPage1Info.DESC_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });
        eCatView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String item = parent.getItemAtPosition(pos).toString();
                mPage.getData().putString(AddEventPage1Info.CATEGORY_DATA_KEY,
                        item);
                mPage.notifyDataChanged();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ePlaceView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(AddEventPage1Info.PLACE_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });


    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        // In a future update to the support library, this should override setUserVisibleHint
        // instead of setMenuVisibility.
        if (eTitleView != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            if (!menuVisible) {
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        }
    }

    /*****************
     * get selected place on map
     ******************/
    @Override
    public void onMapReady(final GoogleMap map) {
        GPSTracker getloc = new GPSTracker(getActivity());
        longitude = getloc.getLongitude();
        latitude = getloc.getLatitude();

        //   longitude = 9.887951798737049;
        //    latitude=37.24520511815063;
        Geocoder coder = new Geocoder(getActivity());
        List<Address> addresses;
        try {

            String address = "19 rue Palestine Bizerte";
            addresses = coder.getFromLocationName(address, 5);
            if (addresses == null) {
            }
            android.location.Address location = addresses.get(0);
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            Log.i("Lat", "" + lat);
            Log.i("Lng", "" + lng);
            LatLng place = new LatLng(latitude, longitude);
            System.out.println("Latitude= " + lat + "Lontitude= " + lng);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(place).title("Marker in " + place);
            map.addMarker(markerOptions);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(place, 12));
        } catch (IOException e) {
            e.printStackTrace();
        }
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                map.clear();
                Location location = new Location("Test");
                location.setLatitude(point.latitude);
                location.setLongitude(point.longitude);
                location.setTime(new Date().getTime());

                //Set time as current Date

                Toast.makeText(getActivity(), "Longitude" + location.toString(), Toast.LENGTH_SHORT);
                //Convert Location to LatLng
                LatLng newLatLng = new LatLng(latitude, longitude);
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(newLatLng)
                        .title(newLatLng.toString());
                map.addMarker(markerOptions);
                try {
                    Geocoder geocoder = new Geocoder(getActivity());
                    List<Address> addresses = null;
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    LongitudeEvent = location.getLongitude();
                    LatitudeEvent = location.getLatitude();
                    LatLng EventPlace = new LatLng(LatitudeEvent, LongitudeEvent);
                    MarkerOptions markerOptions1 = new MarkerOptions()
                            .position(EventPlace)
                            .title(EventPlace.toString());
                    map.addMarker(markerOptions1);
                    app = (MyApp) getActivity().getApplicationContext();


                    String countryName = addresses.get(0).getCountryName();
                    ePlaceView.setText(countryName);
                    ePlaceView.setVisibility(View.VISIBLE);
                    //    app.event.setPlace(countryName);
                    app.event.setLongitudeEvent(LongitudeEvent);
                    app.event.setLatitudeEvent(LatitudeEvent);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /*******************end get selected place on map ******************/

}
