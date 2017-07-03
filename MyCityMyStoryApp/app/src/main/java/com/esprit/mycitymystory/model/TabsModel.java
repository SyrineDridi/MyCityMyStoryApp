package com.esprit.mycitymystory.model;

import android.support.v4.app.Fragment;

import java.util.ArrayList;

/**
 * Model used in MainFragment for displaying tabs
 *
 * @author luchfilip
 */
public class TabsModel {
    ArrayList<CharSequence> titles;
    ArrayList<Integer> icons;
    ArrayList<Fragment> fragments;

    public ArrayList<Integer> getIcons() {
        return icons;
    }

    public void setIcons(ArrayList<Integer> icons) {
        this.icons = icons;
    }

    public ArrayList<CharSequence> getTitles() {
        return titles;
    }

    public void setTitles(ArrayList<CharSequence> titles) {
        this.titles = titles;
    }

    public ArrayList<Fragment> getFragments() {
        return fragments;
    }

    public void setFragments(ArrayList<Fragment> fragments) {
        this.fragments = fragments;
    }
}
