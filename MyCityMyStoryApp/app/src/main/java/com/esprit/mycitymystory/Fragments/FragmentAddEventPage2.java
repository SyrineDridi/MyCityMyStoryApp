package com.esprit.mycitymystory.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.esprit.mycitymystory.R;

import com.esprit.mycitymystory.model.AddEventPage2Info;
import com.esprit.mycitymystory.Interfaces.PageFragmentCallbacks;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class FragmentAddEventPage2 extends Fragment {


    private static final String ARG_KEY = "key";

    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private AddEventPage2Info mPage;

    private EditText eStartDateView;
    private TextView eEndDateView;
    private TextView eStartTimeView;
    private TextView eEndTimeView;
    String dateStart, dateEnd;
    Date startDate , endDate ;

    private int yearStart, monthStart, dayStart;
    private int hour, minute;

    public static FragmentAddEventPage2 create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        FragmentAddEventPage2 fragment = new FragmentAddEventPage2();
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentAddEventPage2() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = (AddEventPage2Info) mCallbacks.onGetPage(mKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_event_page2, container, false);
        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());


        ((TextView) rootView.findViewById(android.R.id.title)).setText(mPage.getTitle());


        eStartDateView = ((EditText) rootView.findViewById(R.id.txtDateStart));
        eStartDateView.setInputType(InputType.TYPE_NULL);
        eStartDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(getActivity(), myStartDateListener,
                        mYear, mMonth, mDay).show();
            }
        });
    /*    eStartDateView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);
                    new DatePickerDialog(getActivity(), myStartDateListener,
                            mYear, mMonth, mDay).show();
                }
            }
        });
*/
        eStartDateView.setText(mPage.getData().getString(AddEventPage2Info.STARTDATE_DATA_KEY));

        eEndDateView = ((TextView) rootView.findViewById(R.id.txtDateEnd));
        eEndDateView.setInputType(InputType.TYPE_NULL);
        eEndDateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(getActivity(), myEndDateListener,
                        mYear, mMonth, mDay).show();
            }
        });
     /*   eEndDateView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);
                    new DatePickerDialog(getActivity(), myEndDateListener,
                            mYear, mMonth, mDay).show();
                }
            }
        }); */
        eEndDateView.setText(mPage.getData().getString(AddEventPage2Info.ENDDATE_DATA_KEY));
        eStartTimeView = ((TextView) rootView.findViewById(R.id.txtTimeStart));
        eStartTimeView.setInputType(InputType.TYPE_NULL);
        eStartTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getActivity(), myStartTimeListener,
                        hour, minute, true).show();
            }
        });
     /*   eStartTimeView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                    new TimePickerDialog(getActivity(), myStartTimeListener,
                            hour, minute, true).show();
                }
            }
        }); */
        eStartTimeView.setText(mPage.getData().getString(AddEventPage2Info.STARTTIME_DATA_KEY));
        eEndTimeView = ((TextView) rootView.findViewById(R.id.txtTimeEnd));
        eEndTimeView.setInputType(InputType.TYPE_NULL);
        eEndTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getActivity(), myEndTimeListener,
                        hour, minute, true).show();
            }
        });
        eEndTimeView.setText(mPage.getData().getString(AddEventPage2Info.ENDTIME_DATA_KEY));
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

        eStartDateView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(AddEventPage2Info.STARTDATE_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        eStartTimeView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(AddEventPage2Info.STARTTIME_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        eEndDateView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(AddEventPage2Info.ENDDATE_DATA_KEY,
                        (editable != null) ? editable.toString() : null);
                mPage.notifyDataChanged();
            }
        });

        eEndTimeView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(AddEventPage2Info.ENDTIME_DATA_KEY,
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
        if (eStartDateView != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            if (!menuVisible) {
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        }
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
        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        startDate = calendar.getTime();
        if (new Date().after(startDate)) {
            System.out.println("you have to choose an available date after the current date ");
            Toast.makeText(getActivity().getApplicationContext(), "you have to choose an available date after the current date ", Toast.LENGTH_LONG).show();
            eStartDateView.setText("");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
            dateStart = sdf.format(startDate);
            eStartDateView.setText(dateStart);
        }
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
                    showDateStart(yearStart, monthStart, dayStart);

                }
            };

    private void showDateStart(int year, int month, int day) {
        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());


        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        endDate= calendar.getTime();
        if (startDate== null ){
            Toast.makeText(getActivity().getApplicationContext(), "you have to choose the start date before ", Toast.LENGTH_LONG).show();

        }else {
          if (startDate.after(endDate)) {
            System.out.println("you have to choose an available date after the start date ");
            Toast.makeText(getActivity().getApplicationContext(), "you have to choose an available date after the current date ", Toast.LENGTH_LONG).show();
            eEndDateView.setText("");
        } else {

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
            dateEnd = sdf.format(endDate);

            eEndDateView.setText(dateEnd);
        }
    }
    }
    /******************end select start date ***********/

    /******************
     * select start time
     **************/
    private TimePickerDialog.OnTimeSetListener myStartTimeListener = new
            TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker p, int hourOfDay, int minuteOfDay) {
                    Locale locale = new Locale("en");
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());

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

        eStartTimeView.setText(currentHour + ":" + minute + aMpM);

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

        eEndTimeView.setText(currentHour + ":" + minute + aMpM);

    }
    /******************end select start date ***********/
}

