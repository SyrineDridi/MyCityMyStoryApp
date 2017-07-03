package com.esprit.mycitymystory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.balysv.materialmenu.MaterialMenuDrawable;
import com.esprit.mycitymystory.Fragments.AllDetailEventFragment;
import com.esprit.mycitymystory.Fragments.FragmentUpdateEvent;
import com.esprit.mycitymystory.Fragments.GetDeatilEentsAPi;
import com.esprit.mycitymystory.Fragments.HomeFragment;
import com.esprit.mycitymystory.Fragments.MainFragment;
import com.esprit.mycitymystory.Fragments.MenuDrawerFragment;
import com.esprit.mycitymystory.Utils.DribSearchView;
import com.esprit.mycitymystory.Utils.Strings;

import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.SlidingDrawer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainAccueil extends AppCompatActivity {
    public static boolean direction, isSearchActive;
    public static EditText editview;
List<String> listFiles  = new ArrayList<>();
    Toolbar toolbar;
    DribSearchView dribSearchView;
    public static MaterialMenuDrawable materialMenu;
    public static InputMethodManager keyboardInput;
    public static Animation animationFadeIn, animationFadeOut;
    public static SlidingDrawer mDrawer;
    public static MainAccueil context;
    NetworkInfo activeNetworkInfo;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_accueil);
        toolbar = (Toolbar) findViewById(R.id.toolbarr);


        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1
        );

        dribSearchView = (DribSearchView) toolbar.findViewById(R.id.dribSearchView);
        editview = (EditText) toolbar.findViewById(R.id.editview);
        keyboardInput = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        editview.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        context = this;
/* test if there is a network *************************************/
        setSupportActionBar(toolbar);
        final ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null) {


            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Problem of connection");
            alert.setMessage("there is no connection ! Please ... try to connect your device on network then open the application");
            alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    System.exit(0);
                }
            });
            alert.show();
        }
        /******************** end of test ******/
        inflateMainFragment();
        inflateMenuDrawerFragment();
        setAnimations();
        initMenuDrawer();
        setClickListeners();
    }

    public void initMenuDrawer() {
        // initialize menu Drawer
        mDrawer = (SlidingDrawer) findViewById(R.id.drawer);
        mDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);


        mDrawer.setOnDrawerStateChangeListener(new MenuDrawer.OnDrawerStateChangeListener() {
            @Override
            public void onDrawerStateChange(int i, int i1) {

            }

            @Override
            public void onDrawerSlide(float slideOffset, int i) {
                direction = mDrawer.isMenuVisible();
                materialMenu.setTransformationOffset(
                        MaterialMenuDrawable.AnimationState.BURGER_ARROW,
                        direction ? 2 - slideOffset : slideOffset
                );

            }
        });
        materialMenu = new MaterialMenuDrawable(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN);
        toolbar.setNavigationIcon(materialMenu);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dribSearchView.changeSearch();

                if (isSearchActive) {
                    materialMenu.animateIconState(MaterialMenuDrawable.IconState.BURGER);
                } else {
                    if (mDrawer.isMenuVisible()) {
                        mDrawer.closeMenu();
                    } else {
                        mDrawer.openMenu();
                    }
                }
            }
        });
    }

    public void setClickListeners() {

        dribSearchView.setOnClickSearchListener(new DribSearchView.OnClickSearchListener() {
            @Override
            public void onClickSearch() {
                dribSearchView.changeLine();
                materialMenu.animateIconState(MaterialMenuDrawable.IconState.X);
                if (isSearchActive) {

                } else {

                }
            }
        });

        dribSearchView.setOnChangeListener(new DribSearchView.OnChangeListener() {
            @Override
            public void onChange(DribSearchView.State state) {
                switch (state) {
                    case LINE:
                        editview.setVisibility(View.VISIBLE);
                        editview.setFocusable(true);
                        editview.setFocusableInTouchMode(true);
                        editview.requestFocus();
                        keyboardInput.showSoftInput(editview, InputMethodManager.SHOW_IMPLICIT);
                        isSearchActive = true;
                        break;
                    case SEARCH:
                        System.out.println("test of search");
                        editview.setVisibility(View.GONE);
                        keyboardInput.hideSoftInputFromWindow(editview.getWindowToken(), 0);
                        isSearchActive = false;
                        break;
                }
            }
        });

    }

    public void setAnimations() {
        animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        animationFadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        animationFadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        animationFadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    public static void inflateMainFragment() {
        MainFragment fragment = new MainFragment();
        FragmentManager fm = context.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.mdContent, fragment, Strings.MAIN_FRAGMENT_TAG);
        fragmentTransaction.addToBackStack(Strings.MAIN_FRAGMENT_TAG);
        fragmentTransaction.commit();
    }

    public void inflateMenuDrawerFragment() {
        MenuDrawerFragment fragment = new MenuDrawerFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.menuFrame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.mdContent);
        if (f != null && f instanceof AllDetailEventFragment || f instanceof GetDeatilEentsAPi || f instanceof FragmentUpdateEvent ) {
       super.onBackPressed();
        }
        else if ( f instanceof MainFragment || f instanceof HomeFragment) {
           finish();
        }
        else {
            return;
        }}



}




