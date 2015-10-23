package com.philips.cdp.ui.catalog.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.hamburgerfragments.CommunityFragment;
import com.philips.cdp.ui.catalog.hamburgerfragments.FindPeopleFragment;
import com.philips.cdp.ui.catalog.hamburgerfragments.HomeFragment;
import com.philips.cdp.ui.catalog.hamburgerfragments.PagesFragment;
import com.philips.cdp.ui.catalog.hamburgerfragments.PhotosFragment;
import com.philips.cdp.ui.catalog.hamburgerfragments.WhatsHotFragment;
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


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hamburger_demo);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.back:
                Toast.makeText(this, "pressed back", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new FindPeopleFragment();
                break;
            case 2:
                fragment = new PhotosFragment();
                break;
            case 3:
                fragment = new CommunityFragment();
                break;
            case 4:
                fragment = new PagesFragment();
                break;
            case 5:
                fragment = new WhatsHotFragment();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(getFragmentContainerID(), fragment).commit();
            drawerListView.setItemChecked(position, true);
            drawerListView.setSelection(position);
            closeDrawer();
        } else {
            // error in creating fragment
            Log.e(getClass() + "", "Error in creating fragment");
        }
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
