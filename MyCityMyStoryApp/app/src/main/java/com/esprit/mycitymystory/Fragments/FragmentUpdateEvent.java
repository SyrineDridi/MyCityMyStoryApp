package com.esprit.mycitymystory.Fragments;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.esprit.mycitymystory.Entities.EntityEvent;
import com.esprit.mycitymystory.R;
import com.esprit.mycitymystory.Utils.Strings;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import static android.app.Activity.RESULT_OK;


public class FragmentUpdateEvent extends Fragment implements OnMapReadyCallback {

    ImageButton btnEditTitle, btnEditStartDate,
            btnEditEndDate, btnEditPlace, btnEditNbrPlace, btnEditDesc,
            btnEditCat, btnSave, btnEditImg, btnEditEndTime, btnEditStartTime;
    EditText tvTitle, tvStartDate, tvEndDate, tvNbrPlaces, tvPlace,
            tvDescription, tvStartTime, tvEndTime;

    ImageView imageEvent;
    String newUrlImage;
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private EntityEvent event;

    private double longitude, latitude;
    private double LongitudeEvent, LatitudeEvent;
    List events;
    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference imagesRef;
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private Uri filePath;
    private int yearStart, monthStart, dayStart;
    private int year, month, day;
    private int hour, minute, second;
    Spinner spinnerCat;
    LinearLayout f;
    private static final String ARG_PARAM1 = "param1";
    private EntityEvent mParam1;


    public static FragmentUpdateEvent newInstance(EntityEvent param1) {
        FragmentUpdateEvent fragment = new FragmentUpdateEvent();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getParcelable(ARG_PARAM1);
        }

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_event, container, false);
        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());


        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference("images");
        imagesRef = storageRef.child("images_events");


        System.out.println(longitude);
        System.out.println(latitude);


        btnEditTitle = (ImageButton) view.findViewById(R.id.btnEditTitle);
        btnEditStartDate = (ImageButton) view.findViewById(R.id.btnEditStartDate);
        btnEditStartTime = (ImageButton) view.findViewById(R.id.btnEditStartTime);
        btnEditEndTime = (ImageButton) view.findViewById(R.id.btnEditEndTime);
        btnEditEndDate = (ImageButton) view.findViewById(R.id.btnEditEndDate);
        btnEditPlace = (ImageButton) view.findViewById(R.id.btnEditPlace);
        btnEditNbrPlace = (ImageButton) view.findViewById(R.id.btnEditNbrPlace);
        btnEditDesc = (ImageButton) view.findViewById(R.id.btnEditDesc);
        btnEditCat = (ImageButton) view.findViewById(R.id.btnEditCat);
        btnEditImg = (ImageButton) view.findViewById(R.id.btnEditImg);
        btnSave = (ImageButton) view.findViewById(R.id.btnSave);
        f = (LinearLayout) view.findViewById(R.id.map_container);


        tvPlace = (EditText) view.findViewById(R.id.tvPlace);
        tvPlace.setInputType(InputType.TYPE_NULL);
        tvTitle = (EditText) view.findViewById(R.id.tvTitle);
        tvDescription = (EditText) view.findViewById(R.id.tvDescription);
        tvStartDate = (EditText) view.findViewById(R.id.tvStartDate);
        tvStartDate.setInputType(InputType.TYPE_NULL);
        tvEndDate = (EditText) view.findViewById(R.id.tvEndDate);
        tvEndDate.setInputType(InputType.TYPE_NULL);
        tvStartTime = (EditText) view.findViewById(R.id.tvStartTime);
        tvStartTime.setInputType(InputType.TYPE_NULL);
        tvEndTime = (EditText) view.findViewById(R.id.tvEndTime);
        tvEndTime.setInputType(InputType.TYPE_NULL);
        tvNbrPlaces = (EditText) view.findViewById(R.id.tvNbrPlaces);
        spinnerCat = (Spinner) view.findViewById(R.id.spinner_cat);
        imageEvent = (ImageView) view.findViewById(R.id.imageEvent);


        /******* get event detail ******************************/
        String date = "dd MMMM yyyy, hh:mmaa";
        SimpleDateFormat formater = new SimpleDateFormat(date , Locale.ENGLISH);
        try {
            Date dateStart = formater.parse(mParam1.getStartDate());
            Date dateEnd = formater.parse(mParam1.getEndDate());

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM " +
                    " yyyy");
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mmaa");

            tvStartDate.setText(dateFormat.format(dateStart));
            tvEndDate.setText(dateFormat.format(dateEnd));
            tvStartTime.setText(timeFormat.format(dateStart));
            tvEndTime.setText(timeFormat.format(dateEnd));

        } catch (ParseException e) {
            e.printStackTrace();
        }




        System.out.println("event to pass " + mParam1);
        tvTitle.setText(mParam1.getTitle().toString());
        tvNbrPlaces.setText(String.valueOf(mParam1.getNbPlaces()));
        tvDescription.setText(mParam1.getDescription().toString());
        tvPlace.setText(mParam1.getPlace());



        Picasso.with(getActivity()).load(mParam1.getUrlImage())
                .into(imageEvent);


        /****** enabled all the edittext ****/
        tvTitle.setEnabled(false);
        tvNbrPlaces.setEnabled(false);
        tvDescription.setEnabled(false);
        tvPlace.setEnabled(false);
        tvStartDate.setEnabled(false);
        tvEndTime.setEnabled(false);
        tvStartTime.setEnabled(false);
        tvEndDate.setEnabled(false);
        spinnerCat.setEnabled(false);
        /**** end enable all the view ****/

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_update);
        mapFragment.getMapAsync(this);
        f = (LinearLayout) view.findViewById(R.id.map_container);
        f.setVisibility(View.GONE);




        /************ end get event detail ********************


         /************ set data ***********/

        /****** enable all the view with the click listener of button ****/
        btnEditImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("test img");
                showFileChooser();
            }
        });


        btnEditTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("test touch");
                tvTitle.setEnabled(true);
            }
        });

        btnEditDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("test touch");
                tvDescription.setEnabled(true);
            }
        });
        btnEditNbrPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("test touch");
                tvNbrPlaces.setEnabled(true);
            }
        });
        btnEditCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("test touch");
                spinnerCat.setEnabled(true);
            }
        });
        btnEditPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvPlace.setEnabled(true);
                f.setVisibility(View.VISIBLE);
            }
        });
        btnEditStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("test touch");
                tvStartDate.setEnabled(true);
            }
        });

        tvStartDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new DatePickerDialog(getActivity(), myStartDateListener,
                            year, month, day).show();
                }
            }
        });

        /********** edit end date ****/
        btnEditEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("test touch");
                tvEndDate.setEnabled(true);
            }
        });
        btnEditEndDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    new DatePickerDialog(getActivity(), myEndDateListener,
                            year, month, day).show();
                }
            }
        });

        /***** end edit end date ****/
        /***** edit start time ****/
        btnEditStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("test touch");
                tvStartTime.setEnabled(true);
            }
        });
        tvStartTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    new TimePickerDialog(getActivity(), myStartTimeListener,
                            hour, minute, true).show();
                }
            }
        });
        /*********** end edit start time *************/

        /********** edit end time **********************/
        btnEditEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("test touch");
                tvEndTime.setEnabled(true);
            }
        });
        tvEndTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    new TimePickerDialog(getActivity(), myEndTimeListener,
                            hour, minute, true).show();
                }
            }
        });
        /********** end edit end time ***********/

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
                UpdateEvent();
            }
        });

        /****** end setting dataa ***********/


        return view;
    }


    public void uploadImage() {

        storageRef = storage.getReference("images_events");
        imagesRef = storageRef.child(mParam1.getId());
        imageEvent.setDrawingCacheEnabled(true);
        imageEvent.buildDrawingCache();
        Bitmap bitmap = imageEvent.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        })}).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
            Uri downloadUrl = taskSnapshot.getDownloadUrl();

        }
    });
        System.out.println("new url " + newUrlImage);

    }

    public void UpdateEvent() {
        event = new EntityEvent();
        String Title = tvTitle.getText().toString();
        String Desc = tvDescription.getText().toString();
        String categorie = String.valueOf(spinnerCat.getSelectedItem());
        String place = tvPlace.getText().toString();
        int nbPlace = Integer.parseInt(tvNbrPlaces.getText().toString());
        String DateStart = tvStartDate.getText().toString();
        String DateEnd = tvEndDate.getText().toString();
        String Endtime = tvEndTime.getText().toString();
        String StartTime = tvStartTime.getText().toString();
        event.setTitle(Title);
        event.setNbPlaces(nbPlace);
        event.setCategory(categorie);
        event.setUrlImage(mParam1.getUrlImage());
        event.setDescription(Desc);
        event.setPlace(place);
        event.setStartDate(DateStart + ", " + StartTime);
        event.setEndDate(DateEnd + ", " + Endtime);
        event.setLatitudeEvent(LatitudeEvent);
        event.setLongitudeEvent(LongitudeEvent);
        event.setUser_id(mFirebaseAuth.getCurrentUser().getUid());
        mDatabase.child("events").child(mParam1.getId()).setValue(event);

        Fragment_MyEvents fragment = new Fragment_MyEvents();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right,
                R.anim.slide_out_left, R.anim.slide_out_left);
        fragmentTransaction.replace(R.id.mdContent, fragment, Strings.VENUE_FRAGMENT_TAG);
        fragmentTransaction.addToBackStack(Strings.VENUE_FRAGMENT_TAG);
        fragmentTransaction.commit();


    }


    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageEvent.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onMapReady(final GoogleMap map) {

        LatLng place = new LatLng(mParam1.getLatitudeEvent(), mParam1.getLongitudeEvent());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(place).title("Place of event " + place);
        map.addMarker(markerOptions);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(place, 12));

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {

                Location location = new Location("Test");
                location.setLatitude(point.latitude);
                location.setLongitude(point.longitude);

                LatLng newLatLng = new LatLng(latitude, longitude);
                MarkerOptions marker = new MarkerOptions()
                        .position(newLatLng)
                        .title(newLatLng.toString());
                map.addMarker(marker);
                try {
                    Geocoder geocoder = new Geocoder(getActivity());
                    List<Address> addresses = null;
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    LongitudeEvent = location.getLongitude();
                    LatitudeEvent = location.getLatitude();
                    String cityName = addresses.get(0).getAddressLine(0);
                    String stateName = addresses.get(0).getAddressLine(1);
                    String countryName = addresses.get(0).getAddressLine(2);
                    System.out.println("this is your place " + cityName + stateName + countryName);
                    tvPlace.setText(cityName + " " + " " + stateName + " " + countryName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /******************
     * select end date
     **************/
    private DatePickerDialog.OnDateSetListener myStartDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker p, int selectedYear,
                                      int selectedMonth, int selectedDay) {
                    yearStart = selectedYear;
                    monthStart = selectedMonth;
                    dayStart = selectedDay;
                    showDateEnd(yearStart, monthStart, dayStart);

                }
            };

    private void showDateEnd(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date date1 = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM " +
                " yyyy");
        String dateString = sdf.format(date1);
        tvEndDate.setText(dateString);
    }
    /******************end select end date ***********/

    /******************
     * select start date
     **************/
    private DatePickerDialog.OnDateSetListener myEndDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker p, int selectedYear,
                                      int selectedMonth, int selectedDay) {

                    yearStart = selectedYear;
                    monthStart = selectedMonth;
                    dayStart = selectedDay;
                    showDateStart(yearStart, monthStart + 1, dayStart);

                }
            };

    private void showDateStart(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date date1 = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        String dateString = sdf.format(date1);
        tvEndDate.setText(dateString);

    }
    /******************end select start date ***********/

    /******************
     * select start time
     **************/
    private TimePickerDialog.OnTimeSetListener myStartTimeListener = new
            TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker p, int hourOfDay, int minuteOfDay) {
                    hour = hourOfDay;
                    minute = minuteOfDay;
                    showTimeStart(hour, minute);
                }
            };

    private void showTimeStart(int hourOfDay, int minuteOfDay) {
        String aMpM = "AM";
        if (hourOfDay > 11) {
            aMpM = "PM";
        }
        int currentHour;
        if (hourOfDay > 11) {
            currentHour = hourOfDay - 12;
        } else {
            currentHour = hourOfDay;
        }

        tvStartTime.setText(currentHour + ":" + minute + aMpM);

    }
    /******************end select start date ***********/
    /******************
     * select end time
     **************/
    private TimePickerDialog.OnTimeSetListener myEndTimeListener = new
            TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker p, int hourOfDay, int minuteOfDay) {
                    hour = hourOfDay;
                    minute = minuteOfDay;
                    showTimeEnd(hour, minute);
                }
            };

    private void showTimeEnd(int hourOfDay, int minuteOfDay) {
        String aMpM = "AM";
        if (hourOfDay > 11) {
            aMpM = "PM";
        }
        int currentHour;
        if (hourOfDay > 11) {
            currentHour = hourOfDay - 12;
        } else {
            currentHour = hourOfDay;
        }

        tvEndTime.setText(currentHour + ":" + minute + aMpM);

    }
    /******************end select start date ***********/
}