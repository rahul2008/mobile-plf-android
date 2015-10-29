package com.philips.cdp.ui.catalog.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.hamburgerfragments.CommunityFragment;
import com.philips.cdp.ui.catalog.hamburgerfragments.FindPeopleFragment;
import com.philips.cdp.ui.catalog.hamburgerfragments.HomeFragment;
import com.philips.cdp.ui.catalog.hamburgerfragments.PagesFragment;
import com.philips.cdp.ui.catalog.hamburgerfragments.PhotosFragment;
import com.philips.cdp.ui.catalog.hamburgerfragments.WhatsHotFragment;
import com.philips.cdp.uikit.hamburger.HamburgerAdapter;
import com.philips.cdp.uikit.hamburger.HamburgerItem;
import com.philips.cdp.uikit.hamburger.PhilipsHamburgerMenu;

import java.util.ArrayList;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class HamburgerMenuDemo extends PhilipsHamburgerMenu {

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private HamburgerAdapter adapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hamburger_demo);
        loadSlideMenuItems();
        addDrawerItems();
        setDrawerAdaptor();

        if (savedInstanceState == null) {
            displayView(0);
        }

        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                displayView(position);
            }
        });

    }

    private void loadSlideMenuItems() {
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);

        hamburgerItems = new ArrayList<>();
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

    private void addDrawerItems() {
        hamburgerItems.add(new HamburgerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
        hamburgerItems.add(new HamburgerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));
        hamburgerItems.add(new HamburgerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, -1)));
        hamburgerItems.add(new HamburgerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));
        hamburgerItems.add(new HamburgerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
        hamburgerItems.add(new HamburgerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
        hamburgerItems.add(new HamburgerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
        hamburgerItems.add(new HamburgerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
        hamburgerItems.add(new HamburgerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
        hamburgerItems.add(new HamburgerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
        hamburgerItems.add(new HamburgerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
        hamburgerItems.add(new HamburgerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
        hamburgerItems.add(new HamburgerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
        hamburgerItems.add(new HamburgerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
        hamburgerItems.add(new HamburgerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
        hamburgerItems.add(new HamburgerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
        hamburgerItems.add(new HamburgerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
        hamburgerItems.add(new HamburgerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
        hamburgerItems.add(new HamburgerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
        hamburgerItems.add(new HamburgerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
        hamburgerItems.add(new HamburgerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
        hamburgerItems.add(new HamburgerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
        hamburgerItems.add(new HamburgerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
        hamburgerItems.add(new HamburgerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
        hamburgerItems.add(new HamburgerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
        hamburgerItems.add(new HamburgerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
        hamburgerItems.add(new HamburgerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));
        navMenuIcons.recycle();
    }

    private void setDrawerAdaptor() {
        adapter = new HamburgerAdapter(this,
                hamburgerItems);
        drawerListView.setAdapter(adapter);
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
            setTitle(navMenuTitles[position]);
            closeDrawer();
        } else {
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

}
