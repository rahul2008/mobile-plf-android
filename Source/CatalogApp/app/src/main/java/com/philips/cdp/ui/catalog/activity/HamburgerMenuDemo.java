package com.philips.cdp.ui.catalog.activity;

import android.app.FragmentManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.hamburgerfragments.HamburgerFragment;
import com.philips.cdp.uikit.com.philips.cdp.uikit.utils.HamburgerUtil;
import com.philips.cdp.uikit.com.philips.cdp.uikit.utils.OnDataNotified;
import com.philips.cdp.uikit.costumviews.PhilipsBadgeView;
import com.philips.cdp.uikit.costumviews.VectorDrawableImageView;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.cdp.uikit.hamburger.HamburgerItem;
import com.philips.cdp.uikit.hamburger.PhilipsHamburgerAdapter;

import java.util.ArrayList;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class HamburgerMenuDemo extends CatalogActivity implements OnDataNotified {

    private String[] hamburgerMenuTitles;
    private TypedArray hamburgerMenuIcons;
    private ArrayList<HamburgerItem> hamburgerItems;
    private DrawerLayout philipsDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView drawerListView;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView actionBarTitle;
    private VectorDrawableImageView footerView;
    private PhilipsHamburgerAdapter adapter;
    private PhilipsBadgeView actionBarCount;
    private HamburgerUtil hamburgerUtil;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uikit_hamburger_menu_basic);
        initViews();
        initActionBar(getSupportActionBar());
        configureDrawer();
        loadSlideMenuItems();
        setHamburgerAdaptor();
        setDrawerAdaptor();
        if (savedInstanceState == null) {
            displayView(0);
        }

        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                displayView(position);
            }
        });

        hamburgerUtil = new HamburgerUtil(this, drawerListView);
        hamburgerUtil.updateSmartFooter(footerView);
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        philipsDrawerLayout = (DrawerLayout) findViewById(R.id.philips_drawer_layout);
        drawerListView = (ListView) findViewById(R.id.hamburger_list);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        footerView = (VectorDrawableImageView) findViewById(R.id.image);
        setSupportActionBar(toolbar);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        actionBarTitle.setText(title);
    }

    private void configureDrawer() {
        drawerToggle = new ActionBarDrawerToggle(this, philipsDrawerLayout, com.philips.cdp.uikit.R.string.app_name, com.philips.cdp.uikit.R.string.app_name) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        philipsDrawerLayout.setDrawerListener(drawerToggle);
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
        adapter.setOnDataNotified(this);
        drawerListView.setAdapter(adapter);
    }

    private void displayView(int position) {
        final HamburgerFragment fragment = new HamburgerFragment();
        FragmentManager fragmentManager = getFragmentManager();
        Bundle bundle = getBundle(hamburgerMenuTitles[position], hamburgerMenuIcons.getResourceId(position, -1));
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragment).commit();
        setTitle(hamburgerMenuTitles[position]);
        philipsDrawerLayout.closeDrawer(navigationView);
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

    private void initActionBar(ActionBar actionBar) {
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(com.philips.cdp.uikit.R.layout.uikit_action_bar_title);
        actionBar.setHomeAsUpIndicator(VectorDrawable.create(this, com.philips.cdp.uikit.R.drawable.uikit_hamburger_icon));
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBarTitle = (TextView) findViewById(R.id.hamburger_title);
        actionBarCount = (PhilipsBadgeView) findViewById(R.id.hamburger_count);
        actionBarCount.setText("0");
        Toolbar parent = (Toolbar) actionBar.getCustomView().getParent();
        parent.setContentInsetsAbsolute(0, 0);
    }

    @Override
    public void onDataSetChanged(String dataCount) {
        actionBarCount.setText(dataCount);
        hamburgerUtil.updateSmartFooter(footerView);
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            hamburgerUtil.updateSmartFooter(footerView);
        }
    }

}
