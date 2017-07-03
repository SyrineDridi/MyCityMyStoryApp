package com.esprit.mycitymystory.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.esprit.mycitymystory.Entities.User;
import com.esprit.mycitymystory.MainAccueil;
import com.esprit.mycitymystory.R;
import com.esprit.mycitymystory.Register;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;


public class GetStarttedFragment extends Fragment {

    Button btnLogin, btnRegister;
    private FirebaseAuth mFirebaseAuth;
    private CallbackManager callbackManager;
    public DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private static final String TAG = "AndroidBash";
    public User user;

    NetworkInfo activeNetworkInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        AppEventsLogger.activateApp(getActivity());
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        AppEventsLogger.activateApp(getActivity());
        View view = inflater.inflate(R.layout.fragment_get_startted, container, false);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btnLogin = (Button) view.findViewById(R.id.btnLogin);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo == null) {


                    final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle("Problem of connection");
                    alert.setMessage("there is no connection try again");
                    alert.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alert.show();

                    Toast.makeText(getActivity(), "there is no connection try again", Toast.LENGTH_LONG).show();
                } else {
                    ShowDialogLogin();
                }
            }
        });
        btnRegister = (Button) view.findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo == null) {
                    Toast.makeText(getActivity(), "there is no connection try again", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    Intent intent = new Intent(getActivity(), Register.class);
                    startActivity(intent);
                }


            }
        });

        //FaceBook
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.button_facebook_login);
        loginButton.setReadPermissions("email", "public_profile"
        );
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("tag", "facebook:onSuccess:" + loginResult);
                signInWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("tag", "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("tag", "facebook:onError", error);
            }
        });
        return view;
    }

    //FaceBook
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void signInWithFacebook(AccessToken token) {
        Log.d("log", "signInWithFacebook:" + token);

        //   showProgressDialog();


        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity()
                        , new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "signInWithCredential", task.getException());
                                    Toast.makeText(getActivity(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    String uid = task.getResult().getUser().getUid();
                                    String name = task.getResult().getUser().getDisplayName();

                                    System.out.println("user info " + task.getResult().getUser().getProviderData());
                                    String image = task.getResult().getUser().getPhotoUrl().toString();
                                    //Create a new User and Save it in Firebase database

                                    user = new User();
                                    user.setFirstname(name);
                                    user.setUrlImageUser(image);
                                    user.setLastname("NONE");
                                    user.setCity("NONE");
                                    user.setCountry("NONE");
                                    user.setState("NONE");
                                    user.setSexe("NONE");
                                    user.setPhone("NONE");
/*******************************************/
                                    String token = FirebaseInstanceId.getInstance().getToken();
                                    mDatabase.child("Tokens").child(mFirebaseAuth.getCurrentUser().getUid()).child("Token").setValue(token);
                                    /*******************************************/
                                    mDatabase.child("users").child(uid).setValue(user).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                System.out.println("succes " + user);
                                                Intent intent = new Intent(getActivity(), MainAccueil.class);
                                                startActivity(intent);
                                                getActivity().finish();
                                            } else {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                builder.setMessage(task.getException().getMessage())
                                                        .setTitle("erreur")
                                                        .setPositiveButton(android.R.string.ok, null);
                                                AlertDialog dialog = builder.create();
                                                dialog.show();
                                            }
                                        }
                                    });

                                }

                                //     hideProgressDialog();
                            }
                        });
    }


    /*public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }*/
    public void ShowDialogLogin() {
        // Create Object of Dialog class
        final Dialog login = new Dialog(getActivity());
        // Set GUI of login screen
        login.setContentView(R.layout.login_dialog);
        login.setTitle("Login");

// Init button of login GUI
        Button btnLogin = (Button) login.findViewById(R.id.btnLogin);
        Button btnCancel = (Button) login.findViewById(R.id.btnCancel);
        final EditText txtUsername = (EditText) login.findViewById(R.id.txtUsername);
        final EditText txtPassword = (EditText) login.findViewById(R.id.txtPassword);
// Attached listener for login GUI button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtUsername.getText().toString().trim().length() > 0 && txtPassword.getText().toString().trim().length() > 0) {
                    // Validate Your login credential here than display message

                    mFirebaseAuth.signInWithEmailAndPassword(txtUsername.getText().toString(), txtPassword.getText().toString())
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        /*******************************************/
                                        String token = FirebaseInstanceId.getInstance().getToken();
                                        Log.d(TAG, "Token: " + token);
                                        mDatabase.child("Tokens").child(mFirebaseAuth.getCurrentUser().getUid()).child("Token").setValue(token);
                                        /*******************************************/
                                        login.dismiss();
                                        Intent intent = new Intent(getActivity(), MainAccueil.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setMessage(task.getException().getMessage())
                                                .setTitle("erreur")
                                                .setPositiveButton(android.R.string.ok, null);
                                        AlertDialog dialog = builder.create();
                                        dialog.show();
                                    }
                                }
                            });
                    // Redirect to dashboard / home screen.
                } else {
                    Toast.makeText(getActivity(), "Please enter Username and Password", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login.dismiss();
            }
        });
        // Make dialog box visible.
        login.show();
    }

}