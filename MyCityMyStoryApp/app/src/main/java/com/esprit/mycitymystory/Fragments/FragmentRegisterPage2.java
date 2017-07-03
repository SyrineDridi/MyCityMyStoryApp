package com.esprit.mycitymystory.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.esprit.mycitymystory.R;
import com.esprit.mycitymystory.Utils.HttpHandler;
import com.esprit.mycitymystory.Utils.MyApp;
import com.esprit.mycitymystory.model.RegisterPage2Info;
import com.esprit.mycitymystory.Interfaces.PageFragmentCallbacks;
import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;

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
import java.util.Locale;


public class FragmentRegisterPage2 extends Fragment {


    private static final String ARG_KEY = "key";

    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    public RegisterPage2Info mPage;

    private TextView etxtCountry;
    private Spinner city_spinner;
    private TextView etxtBirthday;
    private TextView etxtPhone;
    MyApp app;
    ViewGroup v;
    RadioButton RbMen;
    RadioButton RbWomen;

    CountryPicker picker;

    private int yearBirth, monthBirth, dayBirth;
    private int year, month, day;


    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    String country;
    ArrayAdapter<String> adapter;
    private Uri filePath;
    private String TAG = "";
    private ListView lv;
    ArrayList<String> cityList;
    Configuration config;


    public static FragmentRegisterPage2 create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        FragmentRegisterPage2 fragment = new FragmentRegisterPage2();
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentRegisterPage2() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = (RegisterPage2Info) mCallbacks.onGetPage(mKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        config = new Configuration(getResources().getConfiguration());
        config.locale = Locale.ENGLISH;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        View rootView = inflater.inflate(R.layout.fragment_register_page2, container, false);
        ((TextView) rootView.findViewById(android.R.id.title)).setText(mPage.getTitle());


        RbMen = (RadioButton) rootView.findViewById(R.id.RbMen);
        RbWomen = (RadioButton) rootView.findViewById(R.id.RbWomen);

        if (RbMen.isChecked()) {

            RbMen.setText(mPage.getData().getString(RegisterPage2Info.SEX_DATA_KEY));
        } else if (RbWomen.isChecked()) {
            RbWomen.setText(mPage.getData().getString(RegisterPage2Info.SEX_DATA_KEY));

        }
        cityList = new ArrayList<String>();
        picker = CountryPicker.newInstance("Select Country");

        etxtCountry = ((EditText) rootView.findViewById(R.id.txtCountry));
        etxtCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCountry();
            }
        });

        etxtCountry.setText(mPage.getData().getString(RegisterPage2Info.COUNTRY_DATA_KEY));
        etxtCountry.setInputType(InputType.TYPE_NULL);
        etxtBirthday = ((TextView) rootView.findViewById(R.id.txtBirthday));
        etxtBirthday.setInputType(InputType.TYPE_NULL);
        etxtBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), myBirthdayListener,
                        year, month, day).show();
            }
        });
    /*    etxtBirthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    new DatePickerDialog(getActivity(), myBirthdayListener,
                            year, month, day).show();
                }
            }
        }); */
        etxtBirthday.setText(mPage.getData().getString(RegisterPage2Info.BIRTHDAY_DATA_KEY));

        etxtPhone = ((TextView) rootView.findViewById(R.id.txtPhone));
        etxtPhone.setText(mPage.getData().getString(RegisterPage2Info.PHONE_DATA_KEY));

        city_spinner = ((Spinner) rootView.findViewById(R.id.city_spinner));
        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, cityList);
        // Drop down layout style - list view with radio button
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        city_spinner.setAdapter(adapter);
        String.valueOf(city_spinner.getSelectedItem());

        city_spinner.setSelection(((ArrayAdapter<String>) city_spinner.getAdapter()).
                getPosition(mPage.getData().getString(RegisterPage2Info.CITY_DATA_KEY)));


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

        RbMen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPage.getData().putString(RegisterPage2Info.SEX_DATA_KEY,
                        "men");
                mPage.notifyDataChanged();
            }
        });
        RbWomen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPage.getData().putString(RegisterPage2Info.SEX_DATA_KEY,
                        "women");
                mPage.notifyDataChanged();
            }
        });

        city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String item = parent.getItemAtPosition(pos).toString();

                System.out.println("ciiity " + item);
                mPage.getData().putString(RegisterPage2Info.CITY_DATA_KEY,
                        item);
                mPage.notifyDataChanged();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        etxtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(RegisterPage2Info.PHONE_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        etxtCountry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(RegisterPage2Info.COUNTRY_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        etxtBirthday.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(RegisterPage2Info.BIRTHDAY_DATA_KEY,
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
        if (etxtCountry != null && etxtBirthday != null && etxtPhone != null && city_spinner.getSelectedItem() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            if (!menuVisible) {
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        }
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
        etxtBirthday.setText(dateString);
    }

    /******************
     * end Birthday end date
     ***********/

    public void selectCountry() {

        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());

        picker.show(getActivity().getSupportFragmentManager(), "COUNTRY_PICKER");

        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                // Implement your code here
                etxtCountry.setText(name);
                country = name;
                cityList.clear();
                picker.setCancelable(true);
                picker.onHiddenChanged(true);

                selectcity();
            }

        });
    }

    /*******
     * select city with parsing of json file which is in the directory assets
     *************/
    public void selectcity() {


        ArrayList<HashMap<String, String>> contactList;

        new FragmentRegisterPage2.GetContacts().execute();
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
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
    /**** end select city ******/

}

