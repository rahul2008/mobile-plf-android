package com.philips.cdp.ui.catalog.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.hamburgerfragments.HamburgerFragment;
import com.philips.cdp.uikit.hamburger.ExpandableListAdapter;
import com.philips.cdp.uikit.hamburger.PhilipsExpandableHamburgerMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class HamburgerMenuExpandableDemo extends PhilipsExpandableHamburgerMenu {

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hamburger_demo);
        loadSlideMenuItems();
        prepareListData();
        final ExpandableListAdapter listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        drawerListView.setAdapter(listAdapter);
        if (savedInstanceState == null) {
            displayView(0);
        }

        setOnItemClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(final ExpandableListView parent, final View v, final int groupPosition, final int childPosition, final long id) {
                return false;
            }
        });
        setTitle(getResources().getString(R.string.app_name));
    }

    private void loadSlideMenuItems() {
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reload:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void displayView(int position) {
        final HamburgerFragment fragment = new HamburgerFragment();
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            Bundle bundle = getBundle(navMenuTitles[position], navMenuIcons.getResourceId(position, -1));
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(getFragmentContainerID(), fragment).commit();
            setTitle(navMenuTitles[position]);
            closeDrawer();
        } else {
            Log.e(getClass()+"", "Error in creating fragment");
        }
    }

    @NonNull
    private Bundle getBundle(final String navMenuTitle, final int resourceId) {
        Bundle bundle = new Bundle();
        bundle.putString("data", navMenuTitle);
        bundle.putInt("resId", resourceId);
        return bundle;
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding child data
        listDataHeader.add("Top 250");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");

        // Adding child data
        List<String> top250 = new ArrayList<>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        List<String> nowShowing = new ArrayList<>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }
}
