package com.philips.cdp.ui.catalog.activity;

import android.app.FragmentManager;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.hamburgerfragments.HamburgerFragment;
import com.philips.cdp.uikit.costumviews.PhilipsDrawerLayout;
import com.philips.cdp.uikit.hamburger.HamburgerItem;
import com.philips.cdp.uikit.hamburger.PhilipsHamburgerAdapter;

import java.util.ArrayList;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class HamburgerMenuDemo extends CatalogActivity {

    private String[] hamburgerMenuTitles;
    private TypedArray hamburgerMenuIcons;
    private PhilipsHamburgerAdapter adapter;
    private ArrayList<HamburgerItem> hamburgerItems;
    private PhilipsDrawerLayout philipsDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hamburger_demo);
        philipsDrawerLayout = (PhilipsDrawerLayout) findViewById(R.id.drawer_layout_demo);
        configureDrawer();
        loadSlideMenuItems();
        setHamburgerAdaptor();
        setDrawerAdaptor();

        if (savedInstanceState == null) {
            displayView(0);
        }

        philipsDrawerLayout.getDrawerListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                displayView(position);
            }
        });

    }

    private void configureDrawer() {
        drawerToggle = new ActionBarDrawerToggle(this, philipsDrawerLayout.getDrawerLayout(), com.philips.cdp.uikit.R.string.app_name, com.philips.cdp.uikit.R.string.app_name) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        philipsDrawerLayout.getDrawerLayout().setDrawerListener(drawerToggle);
    }

    private void setHamburgerAdaptor() {
        int feature = getIntent().getIntExtra("feature", -1);
        if (feature == 1)
            addDrawerItems();
        else if (feature == 2)
            addDrawerItemsWithIcons();
    }

    private void loadSlideMenuItems() {
        hamburgerMenuTitles = getResources().getStringArray(R.array.hamburger_drawer_items);

        hamburgerMenuIcons = getResources()
                .obtainTypedArray(R.array.hamburger_drawer_icons);

        hamburgerItems = new ArrayList<>();
    }

    private void addDrawerItems() {
        for (int i = 0; i < hamburgerMenuTitles.length; i++) {
            hamburgerItems.add(new HamburgerItem(hamburgerMenuTitles[i], 0, String.valueOf(i + 1)));
        }
    }

    private void addDrawerItemsWithIcons() {
        for (int i = 0; i < hamburgerMenuTitles.length; i++) {
            hamburgerItems.add(new HamburgerItem(hamburgerMenuTitles[i], hamburgerMenuIcons.getResourceId(i, -1), String.valueOf(i + 1)));
        }
    }

    private void setDrawerAdaptor() {
        adapter = new PhilipsHamburgerAdapter(this,
                hamburgerItems);
        philipsDrawerLayout.setCounterListener(adapter);
        philipsDrawerLayout.getDrawerListView().setAdapter(adapter);
    }

    private void displayView(int position) {
        final HamburgerFragment fragment = new HamburgerFragment();
            FragmentManager fragmentManager = getFragmentManager();
            Bundle bundle = getBundle(hamburgerMenuTitles[position], hamburgerMenuIcons.getResourceId(position, -1));
            fragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.drawer_layout_demo, fragment).commit();
        philipsDrawerLayout.setTitle(hamburgerMenuTitles[position]);
        philipsDrawerLayout.closeDrawer();
    }

    @NonNull
    private Bundle getBundle(final String navMenuTitle, final int resourceId) {
        Bundle bundle = new Bundle();
        bundle.putString("data", navMenuTitle);
        bundle.putInt("resId", resourceId);
        return bundle;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_reload:
                Toast.makeText(this, "clicked reload", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
