package com.philips.cdp.ui.catalog.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.hamburgerfragments.HamburgerFragment;
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
        setHamburgerAdaptor();
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

    private void setHamburgerAdaptor() {
        int feature = getIntent().getIntExtra("feature", -1);
        if (feature == 1)
            addDrawerItems();
        else if (feature == 2)
            addDrawerItemsWithIcons();
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
//        hamburgerItems.add(new HamburgerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), "50+"));
        for (int i = 0; i < navMenuTitles.length; i++) {
            hamburgerItems.add(new HamburgerItem(navMenuTitles[i], 0));
        }
    }

    private void addDrawerItemsWithIcons() {
        for (int i = 0; i < navMenuTitles.length; i++) {
            hamburgerItems.add(new HamburgerItem(navMenuTitles[i], navMenuIcons.getResourceId(i, -1)));
        }
    }

    private void setDrawerAdaptor() {
        adapter = new HamburgerAdapter(this,
                hamburgerItems);
        drawerListView.setAdapter(adapter);
    }

    private void displayView(int position) {
        final HamburgerFragment fragment = new HamburgerFragment();
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            Bundle bundle = getBundle(navMenuTitles[position],navMenuIcons.getResourceId(position,-1));
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
}
