package com.esprit.mycitymystory;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.esprit.mycitymystory.Fragments.GetStarttedFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.app.NavigationPolicy;
import com.heinrichreimersoftware.materialintro.app.OnNavigationBlockedListener;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;
import com.heinrichreimersoftware.materialintro.slide.Slide;
public class AboutAppActivity extends IntroActivity {
    /********
     * variable firebase
     *****/
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    /********
     * end variable firebase
     *****/
    public static final String EXTRA_FULLSCREEN = "com.heinrichreimersoftware.materialintro.demo.EXTRA_FULLSCREEN";
    public static final String EXTRA_SCROLLABLE = "com.heinrichreimersoftware.materialintro.demo.EXTRA_SCROLLABLE";
    public static final String EXTRA_CUSTOM_FRAGMENTS = "com.heinrichreimersoftware.materialintro.demo.EXTRA_CUSTOM_FRAGMENTS";
    public static final String EXTRA_PERMISSIONS = "com.heinrichreimersoftware.materialintro.demo.EXTRA_PERMISSIONS";
    public static final String EXTRA_SHOW_BACK = "com.heinrichreimersoftware.materialintro.demo.EXTRA_SHOW_BACK";
    public static final String EXTRA_SHOW_NEXT = "com.heinrichreimersoftware.materialintro.demo.EXTRA_SHOW_NEXT";
    public static final String EXTRA_SKIP_ENABLED = "com.heinrichreimersoftware.materialintro.demo.EXTRA_SKIP_ENABLED";
    public static final String EXTRA_FINISH_ENABLED = "com.heinrichreimersoftware.materialintro.demo.EXTRA_FINISH_ENABLED";
    public static final String EXTRA_GET_STARTED_ENABLED = "com.heinrichreimersoftware.materialintro.demo.EXTRA_GET_STARTED_ENABLED";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        /***** if user is already connected recirect to the main activity ***********/
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        if (mFirebaseUser != null) {
            Intent intent1 = new Intent(AboutAppActivity.this, MainAccueil.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);    // Flag ???????????????????????
            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent1);
        }
        /****** end redirection *****/
        boolean fullscreen = intent.getBooleanExtra(EXTRA_FULLSCREEN, false);
        boolean scrollable = intent.getBooleanExtra(EXTRA_SCROLLABLE, true);
        boolean customFragments = intent.getBooleanExtra(EXTRA_CUSTOM_FRAGMENTS, true);
        boolean permissions = intent.getBooleanExtra(EXTRA_PERMISSIONS, true);
        boolean showBack = intent.getBooleanExtra(EXTRA_SHOW_BACK, true);
        boolean showNext = intent.getBooleanExtra(EXTRA_SHOW_NEXT, true);
        boolean skipEnabled = intent.getBooleanExtra(EXTRA_SKIP_ENABLED, true);
        boolean finishEnabled = intent.getBooleanExtra(EXTRA_FINISH_ENABLED, true);
        boolean getStartedEnabled = intent.getBooleanExtra(EXTRA_GET_STARTED_ENABLED, true);
        setFullscreen(fullscreen);
        super.onCreate(savedInstanceState);
        setButtonBackFunction(skipEnabled ? BUTTON_BACK_FUNCTION_SKIP : BUTTON_BACK_FUNCTION_BACK);
        setButtonNextFunction(finishEnabled ? BUTTON_NEXT_FUNCTION_NEXT_FINISH : BUTTON_NEXT_FUNCTION_NEXT);
        setButtonBackVisible(showBack);
        setButtonNextVisible(showNext);
        //setButtonCtaVisible(getStartedEnabled);
        setButtonCtaTintMode(BUTTON_CTA_TINT_MODE_TEXT);
        /**
         * Standard slide (like Google's intros)
         */
         /*
        *   If user install app my city my story for the first time
         */
        // SHARED PREFERENCSE GET
        SharedPreferences prefs = getSharedPreferences("MYCITYMYSTORY", MODE_PRIVATE);
        String restoredText = prefs.getString("FIRST_TIME_INSTALLED", null);
        if (restoredText == null) {
            // SHARED PREFERENCSE PUT
            SharedPreferences.Editor editor = getSharedPreferences("MYCITYMYSTORY", MODE_PRIVATE).edit();
            editor.putString("FIRST_TIME_INSTALLED", "YES");
            editor.commit();
            addSlide(new SimpleSlide.Builder()
                    .title("My City My Story")
                    .description("My City My story an application which make you flowing upcoming events happening in your city.\n" +
                            "Get event recommendations based on events you have attended and never miss out on your favorite artist’s concerts, conferences, festivals, and music gigs or sports events.\n" +
                            " It gives you an idea about the new events, their location. It also shows you the events around.\n")
                    .image(R.mipmap.ic_launcher)
                    .background(R.color.cpb_red_dark)
                    .backgroundDark(R.color.white)
                    .canGoBackward(false)
                    .canGoForward(true)
                    .scrollable(true)
                    .build());
            /**
             * Custom fragment slide
             */
            addSlide(new SimpleSlide.Builder()
                    .background(R.color.cpb_red_dark)
                    .title("My City My Story")
                    .description("\uF0FC\tCreate a new event in your city \n" +
                            "\uF0FC\tFlow all the events in your city by using our application  \n" +
                            "\uF0FC\tReceive a notification if there is a new event created in your city \n" +
                            "\uF0FC\tRegistration with Facebook\n" +
                            "\uF0FC\tParticipate on an event \n" +
                            "\uF0FC\tFlow all the events in which you have already participated on calendar.\n" +
                            "\uF0FC\tReceive notification before an hour of the start time of an event that you have already participated on.\n" +
                            "\uF0FC\tUpdate your city and country on your profile. \n" +
                            "\uF0FC\tReset your password by receiving an email \n" +
                            "\uF0FC\tfilter events by date and categories \n" +
                            "\uF0FC\tSave interesting categories to check out later\n" +
                            "\uF0FC\teasily share events with your friends on Facebook \n" +
                            "\uF0FC\tGet location of an event and get the direction between your current location and event’s location \n" +
                            "\uF0FC\tUpdate an event that you have created \n" +
                            "\uF0FC\tRating an event \n")
                    .backgroundDark(R.color.white)
                    .image(R.mipmap.ic_launcher)
                    .canGoBackward(true)
                    .canGoForward(true)
                    .scrollable(true)  //  Scrollable ???????????????????????????????
                    .build());
        }
        final Slide loginSlide;
        loginSlide = new FragmentSlide.Builder()
                .background(R.color.cpb_red_dark)
                .backgroundDark(R.color.white)
                .canGoBackward(true)
                .canGoForward(false)
                .fragment(new GetStarttedFragment())
                .build();
        addSlide(loginSlide);
/* Enable/disable fullscreen */
        setFullscreen(true);
    /* Enable/disable skip button */
        setSkipEnabled(true);
    /* Enable/disable finish button */
        setFinishEnabled(true);
    /* Add a navigation policy to define when users can go forward/backward */
        setNavigationPolicy(new NavigationPolicy() {
            @Override
            public boolean canGoForward(int position) {
                return true;
            }
            @Override
            public boolean canGoBackward(int position) {
                return false;
            }
        });
    /* Add a listener to detect when users try to go to a page they can't go to */
        addOnNavigationBlockedListener(new OnNavigationBlockedListener() {
            @Override
            public void onNavigationBlocked(int position, int direction) {
            }
        });
    /* Add your own page change listeners */
        addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }
}