package com.philips.cdp.appframework.homescreen;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.philips.cdp.appframework.AppFrameworkBaseActivity;
import com.philips.cdp.appframework.R;
import com.philips.cdp.appframework.userregistrationscreen.UserRegistrationActivity;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.cdp.uikit.hamburger.HamburgerAdapter;
import com.philips.cdp.uikit.hamburger.HamburgerItem;
import com.philips.cdp.uikit.utils.HamburgerUtil;

import java.util.ArrayList;
/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

public class HomeActivity extends AppFrameworkBaseActivity {
    private String[] hamburgerMenuTitles;
    // private TypedArray hamburgerMenuIcons;
    private ArrayList<HamburgerItem> hamburgerItems;
    private DrawerLayout philipsDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView drawerListView;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView actionBarTitle;
    private ImageView footerView;
    private HamburgerAdapter adapter;
    private TextView actionBarCount;
    private HamburgerUtil hamburgerUtil;
    private ImageView hamburgerIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Philips_DarkBlue_Gradient_NoActionBar);
        /*
         * Setting Philips UI KIT standard BLUE theme.
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uikit_hamburger_menu);
        initViews();
        initActionBar(getSupportActionBar());
        configureDrawer();
        loadSlideMenuItems();
        setHamburgerAdaptor();
        hamburgerUtil = new HamburgerUtil(this, drawerListView);
        hamburgerUtil.updateSmartFooter(footerView, hamburgerItems.size());
        setDrawerAdaptor();
        displayView(0);

        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                if (!hamburgerMenuTitles[position].equalsIgnoreCase("Title")) {
                    adapter.setSelectedIndex(position);
                    displayView(position);
                }
            }
        });
    }

    private void initActionBar(ActionBar actionBar) {
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(com.philips.cdp.uikit.R.layout.uikit_action_bar_title);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBarTitle = (TextView) findViewById(R.id.hamburger_title);

        actionBarCount = (TextView) findViewById(R.id.hamburger_count);
        actionBarCount.setVisibility(View.GONE);
        hamburgerIcon = (ImageView) findViewById(R.id.hamburger_icon);
        hamburgerIcon.setImageDrawable(VectorDrawable.create(this, R.drawable.uikit_hamburger_icon));
        LinearLayout hamburgerClick = (LinearLayout) findViewById(R.id.hamburger_click);

        hamburgerClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                philipsDrawerLayout.openDrawer(navigationView);
            }
        });
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        philipsDrawerLayout = (DrawerLayout) findViewById(R.id.philips_drawer_layout);
        drawerListView = (ListView) findViewById(R.id.hamburger_list);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        footerView = (ImageView) findViewById(R.id.philips_logo);
        int resID = com.philips.cdp.uikit.R.drawable.uikit_philips_logo;
        footerView.setImageDrawable(VectorDrawable.create(this, resID));
        setSupportActionBar(toolbar);
    }

    private void setDrawerAdaptor() {
        adapter = new HamburgerAdapter(this,
                hamburgerItems);
        drawerListView.setAdapter(adapter);
        //  actionBarCount.setText(String.valueOf(adapter.getCounterValue()));
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        actionBarTitle.setText(title);
        actionBarTitle.setSelected(true);
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
        addDrawerItems();
    }

    private void loadSlideMenuItems() {
        hamburgerMenuTitles = getResources().getStringArray(R.array.hamburger_drawer_items);
        // hamburgerMenuIcons = getResources()
        //    .obtainTypedArray(R.array.hamburger_drawer_icons);
        hamburgerItems = new ArrayList<>();
    }

    private void addDrawerItems() {
        for (int i = 0; i < hamburgerMenuTitles.length; i++) {
            hamburgerItems.add(new HamburgerItem(hamburgerMenuTitles[i], null));

        }
    }

    private void displayView(int position) {

        if (hamburgerMenuTitles[position].equalsIgnoreCase("Log Out")) {
            startActivity(new Intent(HomeActivity.this, UserRegistrationActivity.class));
        } else {
            final HomeScreenFragment fragment = new HomeScreenFragment();
            FragmentManager fragmentManager = getFragmentManager();
            // Bundle bundle = getBundle(hamburgerMenuTitles[position], hamburgerMenuIcons.getResourceId(position, -1));
            //   fragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();
        }
        setTitle(hamburgerMenuTitles[position]);
        philipsDrawerLayout.closeDrawer(navigationView);
    }

    @Override
    public void onBackPressed() {
    }
}
