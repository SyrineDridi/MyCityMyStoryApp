package com.esprit.mycitymystory.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.esprit.mycitymystory.AboutAppActivity;
import com.esprit.mycitymystory.Adapter.MenuListAdapter;
import com.esprit.mycitymystory.Entities.User;
import com.esprit.mycitymystory.MainAccueil;
import com.esprit.mycitymystory.R;
import com.esprit.mycitymystory.Utils.Strings;
import com.esprit.mycitymystory.model.MenuItemModel;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Fragment for displaying and controlling menu drawer
 *
 * @author luchfilip
 */
public class MenuDrawerFragment extends Fragment {

    ListView listView;
    MenuListAdapter menuListAdapter;
    ArrayList<MenuItemModel> menuItemModels = new ArrayList<>();
    TextView title;
    CircleImageView profile_image;


    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    FirebaseStorage storage;
    StorageReference storageRef;
    StorageReference imagesRef;
    private String FirstName, id;
    private String LastName;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstance) {
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.menudrawer_layout, viewGroup, false);
        listView = (ListView) view.findViewById(R.id.listview);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();


        setMenuData();

        View headerView = inflater.inflate(R.layout.menu_list_header, null, false);
        listView.addHeaderView(headerView);

        menuListAdapter = new MenuListAdapter(menuItemModels, getActivity());
        listView.setAdapter(menuListAdapter);
        setMenuClickListener();


        title = (TextView) headerView.findViewById(R.id.title);
        profile_image = (CircleImageView) headerView.findViewById(R.id.profile_image);


        id = mFirebaseUser.getUid();
        final DatabaseReference users = mDatabase.child("users").child(id);
        storageRef = storage.getReference("images_users");
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                //   if (! user.equals(null)) {
                if (!(user.getUrlImageUser().equals(null))) {
                    Picasso.with(getActivity()).load(user.getUrlImageUser())
                            .into(profile_image);
                }
                FirstName = user.getFirstname();
                LastName = user.getLastname();
                if (FirstName.equals("NONE")) {
                    FirstName = "";
                }
                if (LastName.equals("NONE")) {
                    LastName = "";
                }
                title.setText(FirstName + " " + LastName);
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.d("User", firebaseError.getMessage());
            }
        });


        return view;
    }

    public void setMenuData() {
        menuItemModels.clear();
        MenuItemModel home = new MenuItemModel();
        home.setIcon(R.mipmap.ic_home_red_48dp);
        home.setTitle("home");
        menuItemModels.add(home);


        MenuItemModel invite = new MenuItemModel();
        invite.setIcon(R.mipmap.ic_events_red_48dp);
        invite.setTitle("my events");
        menuItemModels.add(invite);

        MenuItemModel reviews = new MenuItemModel();
        reviews.setIcon(R.mipmap.ic_calendar_red_48);
        reviews.setTitle("my calander");
        menuItemModels.add(reviews);

        MenuItemModel logout = new MenuItemModel();
        logout.setIcon(R.mipmap.ic_exit_red_48dp);
        logout.setTitle("Log out");
        menuItemModels.add(logout);

    }

    public void setMenuClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = "";

//                    check if not header
                if (position > 0) {
                    title = ((MenuItemModel) menuListAdapter.getItem(position - 1)).getTitle();
                }

                switch (title) {
                    case "home":
                        MainFragment fragment = new MainFragment();
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fm.beginTransaction();
                        fragmentTransaction.replace(R.id.mdContent, fragment, Strings.MAIN_FRAGMENT_TAG);
                        fragmentTransaction.addToBackStack(Strings.MAIN_FRAGMENT_TAG);
                        fragmentTransaction.commit();
                        MainAccueil.mDrawer.closeMenu();
                        break;

                    case "my calander":
                        FragmentCalenderUser fragment2 = new FragmentCalenderUser();
                        FragmentManager fm2 = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction2 = fm2.beginTransaction();
                        fragmentTransaction2.replace(R.id.mdContent, fragment2, Strings.MAIN_FRAGMENT_TAG);
                        fragmentTransaction2.addToBackStack(Strings.MAIN_FRAGMENT_TAG);
                        fragmentTransaction2.commit();
                        MainAccueil.mDrawer.closeMenu();
                        break;
                    case "my events":
                        Fragment_MyEvents fragment3 = new Fragment_MyEvents();
                        FragmentManager fm3 = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction3 = fm3.beginTransaction();
                        fragmentTransaction3.replace(R.id.mdContent, fragment3, Strings.MAIN_FRAGMENT_TAG);
                        fragmentTransaction3.addToBackStack(Strings.MAIN_FRAGMENT_TAG);
                        fragmentTransaction3.commit();
                        MainAccueil.mDrawer.closeMenu();
                        break;

                    case "Log out":
                        FirebaseAuth.getInstance().signOut();
                        if (LoginManager.getInstance() != null) {
                            LoginManager.getInstance().logOut();
                        }
                        Intent intent = new Intent(getActivity(), AboutAppActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        MainAccueil.mDrawer.closeMenu();
                        break;

                }
            }
        });
    }
}
