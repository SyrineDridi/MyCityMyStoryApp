package com.esprit.mycitymystory.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.esprit.mycitymystory.R;

import com.esprit.mycitymystory.Utils.MyApp;
import com.esprit.mycitymystory.model.AddEventPage3Info;
import com.esprit.mycitymystory.Interfaces.PageFragmentCallbacks;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;


public class FragmentAddEventPage3 extends Fragment {

    private static final String ARG_KEY = "key";

    private PageFragmentCallbacks mCallbacks;
    private String mKey;
    private AddEventPage3Info mPage;
    private Button btnSelect;
    private TextView eNbPlaceView;
    private ImageView eImageView;

    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private Uri filePath;
    MyApp app;

    public static FragmentAddEventPage3 create(String key) {
        Bundle args = new Bundle();
        args.putString(ARG_KEY, key);

        FragmentAddEventPage3 fragment = new FragmentAddEventPage3();
        fragment.setArguments(args);
        return fragment;
    }

    public FragmentAddEventPage3() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        mKey = args.getString(ARG_KEY);
        mPage = (AddEventPage3Info) mCallbacks.onGetPage(mKey);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_event_page3, container, false);

        ((TextView) rootView.findViewById(android.R.id.title)).setText(mPage.getTitle());


        eNbPlaceView = ((TextView) rootView.findViewById(R.id.txtNbPlaces));
        eNbPlaceView.setText(mPage.getData().getString(AddEventPage3Info.NBPLACE_DATA_KEY));

        /**********select image ***************/
        eImageView = ((ImageView) rootView.findViewById(R.id.ImgEvent));
        btnSelect = ((Button) rootView.findViewById(R.id.btnchosefile));
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        /*************** end select image ************************/
        app = (MyApp) getActivity().getApplication();
        app.image = eImageView;
        app.imageEvent = eImageView;



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

        eNbPlaceView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1,
                                          int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mPage.getData().putString(AddEventPage3Info.NBPLACE_DATA_KEY,
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
        if (eNbPlaceView != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            if (!menuVisible) {
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        }
    }

    /*********************
     * select image
     ***********************************/
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    /***********************
     * end select image
     ***************************************************/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                eImageView.setImageBitmap(bitmap);
                app.bmp=bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
