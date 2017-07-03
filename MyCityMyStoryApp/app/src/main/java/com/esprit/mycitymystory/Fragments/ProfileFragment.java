package com.esprit.mycitymystory.Fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.esprit.mycitymystory.Entities.EntityEvent;
import com.esprit.mycitymystory.Entities.Participation;
import com.esprit.mycitymystory.Entities.User;
import com.esprit.mycitymystory.Handler.getStateOfCityHandler;
import com.esprit.mycitymystory.R;
import com.esprit.mycitymystory.Utils.HttpHandler;
import com.esprit.mycitymystory.Utils.getStateOfCity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;
import com.sa90.materialarcmenu.ArcMenu;
import com.sa90.materialarcmenu.StateChangeListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Fragment for displaying & Managing Profile Tab
 *
 * @author Syrine
 */
public class ProfileFragment extends Fragment {
    FloatingActionButton btnResetPassword, btnRestCountry, btnResetProfil;
    CircleImageView profileImage;
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    FirebaseStorage storage;
    StorageReference storageRef, imagesRef;
    private String FirstName, LastName, Email, id;
    private User user;
    String new_Sex;
    String new_Phone;
    private List<EntityEvent> events = new ArrayList<>();
    private List<Participation> participations = new ArrayList<>();
    private int yearBirth, monthBirth, dayBirth;
    private int year, month, day;
    ArcMenu arcMenuAndroid;
    CountryPicker picker;
    RadioButton RbMen, RbWomen;
    String Birthday ,Sex ,Phone ;
    Spinner city_spinner;
    ArrayAdapter<String> adapter;
    String country, city, state;
    ArrayList<String> cityList;
    EditText edtBirthday;
    private String TAG = "";
    EditText tvPhone;
    TextView TvName, TvPlace, TvEmail, TvBirthday, TvSex, TvPhone, tvNbEvents, tvNbParticipations;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstance) {
        View view = inflater.inflate(R.layout.profile_fragment, viewGroup, false);
        cityList = new ArrayList<String>();
        picker = CountryPicker.newInstance("Select Country");

        profileImage = (CircleImageView) view.findViewById(R.id.profile_image);
        tvNbParticipations = (TextView) view.findViewById(R.id.tvNbParticipations);
        tvNbEvents = (TextView) view.findViewById(R.id.tvNbEvents);
        TvName = (TextView) view.findViewById(R.id.TvName);
        TvPlace = (TextView) view.findViewById(R.id.TvPlace);
        TvEmail = (TextView) view.findViewById(R.id.TvEmail);
        TvBirthday = (TextView) view.findViewById(R.id.TvBirthday);

        TvSex = (TextView) view.findViewById(R.id.TvSex);
        TvPhone = (TextView) view.findViewById(R.id.TvPhone);
        getAllEventUser();
        getAllParticipationUser();
        btnResetPassword = (FloatingActionButton) view.findViewById(R.id.btnResetPassword);
        btnRestCountry = (FloatingActionButton) view.findViewById(R.id.btnRestCountry);
        btnResetProfil = (FloatingActionButton) view.findViewById(R.id.btnResetProfil);
        arcMenuAndroid = (ArcMenu) view.findViewById(R.id.arcmenu_android_example_layout);
        arcMenuAndroid.setStateChangeListener(new StateChangeListener() {
            @Override
            public void onMenuOpened() {
                //TODO something when menu is opened
            }

            @Override
            public void onMenuClosed() {
                //TODO something when menu is closed
            }
        });
        id = mFirebaseUser.getUid();
        DatabaseReference users = mDatabase.child("users").child(id);
        storageRef = storage.getReference("images_users");
        String id = mFirebaseAuth.getCurrentUser().getUid();
        imagesRef = storageRef.child(id);
        imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                FirstName = user.getFirstname();
                LastName = user.getLastname();
                if (FirstName.equals("NONE")) {
                    FirstName = "";
                }
                if (LastName.equals("NONE")) {
                    LastName = "";
                }
                city = user.getCity();
                country = user.getCountry();
                Email = mFirebaseAuth.getCurrentUser().getEmail();
                Phone = user.getPhone();
                Birthday = user.getBirthday();
                Sex = user.getSexe();
                if (user.getFirstname() == null)  {
                   FirstName = "";
                }
                if (user.getLastname() == null) {
                    LastName = "";
                }
                TvName.setText(FirstName + " " + LastName);
                TvPlace.setText(city + " " + country);
                TvBirthday.setText(Birthday);
                TvPhone.setText(Phone);
                TvSex.setText(Sex);
                TvEmail.setText(Email);
                Picasso.with(getActivity()).load(user.getUrlImageUser())
                        .into(profileImage);
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.d("User", firebaseError.getMessage());
            }
        });

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogResetPassword();
            }
        });


        btnRestCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogResetCountry();
            }
        });

        btnResetProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogResetProfil();
            }
        });

        return view;
    }

    public void DialogResetPassword() {
        // Create Object of Dialog class
        final Dialog ResetPassword = new Dialog(getActivity());
        // Set GUI of login screen
        ResetPassword.setContentView(R.layout.reset_password_dialog);
        ResetPassword.setTitle("Reset password");

// Init button of login GUI
        Button btnLogin = (Button) ResetPassword.findViewById(R.id.btnReset);
        Button btnCancel = (Button) ResetPassword.findViewById(R.id.btnCancelReset);
        final EditText txtUsername = (EditText) ResetPassword.findViewById(R.id.txtEmail);
// Attached listener for login GUI button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtUsername.getText().toString().trim().length() > 0 && txtUsername.getText().toString().equals(mFirebaseUser.getEmail())
                        ) {
                    // Validate Your login credential here than display message

                    mFirebaseAuth.sendPasswordResetEmail(txtUsername.getText().toString());

                    Toast.makeText(getActivity(), "Verifier votre boite de reception", Toast.LENGTH_LONG).show();
                    /*******************************************/
                    ResetPassword.dismiss();

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("verifier votre email")
                            .setTitle("erreur")
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResetPassword.dismiss();
            }
        });
        // Make dialog box visible.
        ResetPassword.show();
    }


    /*************
     * reset email
     ****/

    public void DialogResetCountry() {
// Create Object of Dialog class

        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());

        final Dialog ResetCountry = new Dialog(getActivity());
        // Set GUI of login screen
        ResetCountry.setContentView(R.layout.reset_country_city);
        ResetCountry.setTitle("Reset country");

// Init button of GUI
        Button btnLogin = (Button) ResetCountry.findViewById(R.id.btnReset);
        Button btnCancel = (Button) ResetCountry.findViewById(R.id.btnCancelReset);
        city_spinner = (Spinner) ResetCountry.findViewById(R.id.city_spinner);


        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, cityList);
        // Drop down layout style - list view with radio button
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        city_spinner.setAdapter(adapter);


        final EditText txtUsername = (EditText) ResetCountry.findViewById(R.id.txtEmail);
        txtUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Locale locale = new Locale("en");
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());picker.show(getActivity().getSupportFragmentManager(), "COUNTRY_PICKER");

                picker.setListener(new CountryPickerListener() {
                    @Override
                    public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                        // Implement your code here
                        txtUsername.setText(name);
                        country = name;
                        cityList.clear();
                        picker.setCancelable(true);
                        picker.onHiddenChanged(true);

                        selectcity();
                    }

                });
            }
        });
   /*     txtUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                }
            }
        });*/

// Attached listener for login GUI button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtUsername.getText().toString().trim().length() > 0) {
                    city = String.valueOf(city_spinner.getSelectedItem());
                    if (
                            city_spinner.getSelectedItem() == null) {
                        Toast.makeText(getActivity(), "you have to choose your city now", Toast.LENGTH_LONG).show();
                    }

                    new AsyncTask<Void, Void, Void>() {

                        @Override
                        protected Void doInBackground(Void... params) {
                            String jsonState = getStateOfCity.getStateOfCity(city.replace(" ", "%20"));
                            state = getStateOfCityHandler.getAllFormJson(jsonState);
                            mDatabase.child("users").child(id).child("state").setValue(state);
                            mDatabase.child("users").child(id).child("country").setValue(country);
                            mDatabase.child("users").child(id).child("city").setValue(city);

                            return null;
                        }
                    }.execute();

                    TvPlace.setText(city + " " + country);
                    ResetCountry.dismiss();


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
    /*******
     * select city with parsing of json file which is in the directory assets
     *************/
    public void selectcity() {


        ArrayList<HashMap<String, String>> contactList;

        new ProfileFragment.GetContacts().execute();
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getActivity(), "Json Data is downloading", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
                try {
                    String jsonLocation = AssetJSONFile("countriesToCities.json", getActivity().getApplicationContext());
                    JSONObject jsonObj = new JSONObject(jsonLocation);
                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray(country);

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
            adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, cityList);
            // Drop down layout style - list view with radio button
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // attaching data adapter to spinner
            city_spinner.setAdapter(adapter);
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


    public void DialogResetProfil() {

        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());

        final Dialog ResetProfil = new Dialog(getActivity());
        // Set GUI of login screen
        ResetProfil.setContentView(R.layout.dialog_edit_profile);
        ResetProfil.setTitle("Edit Profil");

// Init button of GUI
        Button btnLogin = (Button) ResetProfil.findViewById(R.id.btnReset);
        Button btnCancel = (Button) ResetProfil.findViewById(R.id.btnCancelReset);
        tvPhone = (EditText) ResetProfil.findViewById(R.id.edtPhone);
        RbMen = (RadioButton) ResetProfil.findViewById(R.id.RbMen);
        RbWomen = (RadioButton) ResetProfil.findViewById(R.id.RbWomen);



        if (RbMen.isChecked()) {
            new_Sex = "Men";
        } else if (RbWomen.isChecked()) {
            new_Sex = "Women";

        }
        edtBirthday = (EditText) ResetProfil.findViewById(R.id.edtBirthday);
        edtBirthday.setInputType(InputType.TYPE_NULL);
        tvPhone.setText(Phone);
        edtBirthday.setText(Birthday);

        if ( Sex.equals("Men"))
        {
            RbMen.setChecked(true);
        }
        if (Sex.equals("Women"))
        {
            RbWomen.setChecked(true);
        }

        edtBirthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                new DatePickerDialog(getActivity(), myBirthdayListener,
                        year, month, day).show();
            }
        });

// Attached listener for login GUI button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtBirthday.getText().length() > 0) {
                    if (RbMen.isChecked()) {
                        new_Sex = "Men";
                        RbWomen.setChecked(false);
                    } else if (RbWomen.isChecked()) {
                        new_Sex = "Women";
                        RbMen.setChecked(false);

                    }

                    mDatabase.child("users").child(id).child("sexe").setValue(new_Sex);
                    mDatabase.child("users").child(id).child("phone").setValue(tvPhone.getText().toString());
                    mDatabase.child("users").child(id).child("birthday").setValue(edtBirthday.getText().toString());
                    TvPhone.setText(tvPhone.getText().toString());
                    TvBirthday.setText(edtBirthday.getText().toString());
                    TvSex.setText(new_Sex);

                    ResetProfil.dismiss();
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
                ResetProfil.dismiss();
            }
        });
        // Make dialog box visible.
        ResetProfil.show();
    }


    /******************
     * select Birthday date
     **************/
    private DatePickerDialog.OnDateSetListener myBirthdayListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker p, int selectedYear,
                                      int selectedMonth, int selectedDay) {
                    yearBirth = selectedYear;
                    monthBirth = selectedMonth;
                    dayBirth = selectedDay;
                    showDateBirthday(yearBirth, monthBirth, dayBirth);

                }
            };

    private void showDateBirthday(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date date1 = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(date1);
        edtBirthday.setText(dateString);
    }

    /******************
     * end Birthday end date
     ***********/
    public void getAllEventUser() {
        events = new ArrayList<>();
        mDatabase.child("events").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {

                    EntityEvent model = dataSnapshot.getValue(EntityEvent.class);
                    if (model.getUser_id().equals(mFirebaseUser.getUid())) {
                        events.add(model);
                    }

                } catch (Exception ex) {
                    Log.e("tag", ex.getMessage());
                }
                tvNbEvents.setText(String.valueOf(events.size()) + "\nEvents");

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
                    if (model.getUser_id().equals(mFirebaseUser.getUid())) {
                        participations.add(model);
                    }

                } catch (Exception ex) {
                    Log.e("tag", ex.getMessage());
                }
                tvNbParticipations.setText(String.valueOf(participations.size()) + "\nParticipations");

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
