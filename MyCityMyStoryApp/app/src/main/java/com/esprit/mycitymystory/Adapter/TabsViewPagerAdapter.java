package com.esprit.mycitymystory.Adapter;



import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.esprit.mycitymystory.model.TabsModel;



public class TabsViewPagerAdapter extends FragmentStatePagerAdapter {

    TabsModel tabsModel;
    Context context;

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public TabsViewPagerAdapter(FragmentManager fm, TabsModel tabsModel, Context context) {
        super(fm);
        this.tabsModel = tabsModel;
        this.context = context;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        return tabsModel.getFragments().get(position);
    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return tabsModel.getTitles().get(position);
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return tabsModel.getTitles().size();
    }
}
