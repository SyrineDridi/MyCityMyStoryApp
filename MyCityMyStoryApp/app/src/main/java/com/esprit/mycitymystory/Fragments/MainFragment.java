package com.esprit.mycitymystory.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esprit.mycitymystory.Adapter.TabsViewPagerAdapter;
import com.esprit.mycitymystory.R;
import com.esprit.mycitymystory.Utils.SlidingTabLayout;
import com.esprit.mycitymystory.model.TabsModel;

import java.util.ArrayList;


/**
 * Fragment for displaying & Managing All tabs
 *
 * @author luchfilip
 */
public class MainFragment extends Fragment {

    TabsViewPagerAdapter tabsAdapter;
    ViewPager pager;
    SlidingTabLayout tabs;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstance) {
        View view = inflater.inflate(R.layout.main_fragment, viewGroup, false);
        pager = (ViewPager) view.findViewById(R.id.pager);
        tabs = (SlidingTabLayout) view.findViewById(R.id.tabs);

        initTabs();

        return view;
    }



    public void initTabs() {
        ArrayList<CharSequence> titles = new ArrayList<>();
        ArrayList<Fragment> fragments = new ArrayList<>();
        ArrayList<Integer> icons = new ArrayList<>();

        titles.add("Home");
        icons.add(R.mipmap.ic_home_red_48dp);
        fragments.add(new HomeFragment());

        titles.add("Map");
        icons.add(R.mipmap.ic_map_red_48dp);
        fragments.add(new FragmentEventNearMe());

        titles.add("Profile");
        icons.add(R.mipmap.ic_user_profile_red_48dp);
        fragments.add(new ProfileFragment());

        titles.add("About");
        icons.add(R.mipmap.ic_about_red_48dp);
        fragments.add(new Fragment_About_App());


        TabsModel tabsModel = new TabsModel();
        tabsModel.setTitles(titles);
        tabsModel.setFragments(fragments);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        tabsAdapter = new TabsViewPagerAdapter(fragmentManager, tabsModel, getActivity());
        // Assigning ViewPager View and setting the adapter
        pager.setAdapter(tabsAdapter);

        // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width
        tabs.setDistributeEvenly(true);

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getActivity().getResources().getColor(R.color.colorPrimaryDark);
            }
        });
        //setting custom tab layout
        tabs.setCustomTabIconView(R.layout.custom_tab_view, R.id.title, R.id.icon, icons);

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

    }



}
