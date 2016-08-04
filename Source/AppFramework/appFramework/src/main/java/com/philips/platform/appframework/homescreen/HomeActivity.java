/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.homescreen;


import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.philips.cdp.productselection.listeners.ActionbarUpdateListener;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.cdp.uikit.hamburger.HamburgerAdapter;
import com.philips.cdp.uikit.hamburger.HamburgerItem;
import com.philips.cdp.uikit.utils.HamburgerUtil;
import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.consumercare.ConsumerCareLauncher;
import com.philips.platform.appframework.debugtest.DebugTestFragment;
import com.philips.platform.appframework.inapppurchase.InAppPurchasesFragment;
import com.philips.platform.appframework.settingscreen.SettingsFragment;
import com.philips.platform.appframework.utility.Logger;
import com.philips.platform.appinfra.logging.LoggingInterface;

import java.util.ArrayList;


public class HomeActivity extends AppFrameworkBaseActivity {
    private static String TAG = HomeActivity.class.getSimpleName();
    protected TextView actionBarTitle;
    private String[] hamburgerMenuTitles;
    // private TypedArray hamburgerMenuIcons;
    private ArrayList<HamburgerItem> hamburgerItems;
    private DrawerLayout philipsDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView drawerListView;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ImageView footerView;
    private HamburgerAdapter adapter;
    private TextView actionBarCount;
    private HamburgerUtil hamburgerUtil;
    private ImageView hamburgerIcon;
    private LinearLayout hamburgerClick = null;
    private ConsumerCareLauncher mConsumerCareFragment = null;
    private ActionbarUpdateListener actionBarClickListener = new ActionbarUpdateListener() {

        @Override
        public void updateActionbar(String titleActionbar, Boolean hamburgerIconAvailable) {
            if (hamburgerIconAvailable) {
                hamburgerIcon.setImageDrawable(VectorDrawable.create(HomeActivity.this, R.drawable.uikit_hamburger_icon));
                hamburgerClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        philipsDrawerLayout.openDrawer(navigationView);
                    }
                });
            } else {
//                hamburgerIcon.setImageDrawable(R.drawable.consumercare_actionbar_back_arrow_white);
                hamburgerIcon.setImageDrawable(ContextCompat.getDrawable(HomeActivity.this,
                        R.drawable.consumercare_actionbar_back_arrow_white));
                hamburgerClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        backstackFragment();
                    }
                });
            }
            setTitle(titleActionbar);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Philips_DarkBlue_Gradient_NoActionBar);
        /*
         * Setting Philips UI KIT standard BLUE theme.
         */
        super.onCreate(savedInstanceState);
        AppFrameworkApplication.loggingInterface.log(LoggingInterface.LogLevel.INFO, TAG, " HomeScreen Activity Created ");

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
        actionBarTitle.setGravity(Gravity.CENTER_HORIZONTAL);

        actionBarCount = (TextView) findViewById(R.id.hamburger_count);
        actionBarCount.setVisibility(View.GONE);
        hamburgerIcon = (ImageView) findViewById(R.id.hamburger_icon);
        hamburgerIcon.setImageDrawable(VectorDrawable.create(this, R.drawable.uikit_hamburger_icon));
        hamburgerClick = (LinearLayout) findViewById(R.id.hamburger_click);

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
        philipsDrawerLayout.addDrawerListener(drawerToggle);
    }

    private void setHamburgerAdaptor() {
        addDrawerItems();
    }

    private void loadSlideMenuItems() {
        hamburgerMenuTitles = getResources().getStringArray(R.array.hamburger_drawer_items);
        hamburgerItems = new ArrayList<>();
    }

    private void addDrawerItems() {
        for (int i = 0; i < hamburgerMenuTitles.length; i++) {
            hamburgerItems.add(new HamburgerItem(hamburgerMenuTitles[i], null));

        }
    }

    private void displayView(int position) {

        if (hamburgerMenuTitles[position].equalsIgnoreCase("Settings")) {
            showSettingsFragment();
        } else if (hamburgerMenuTitles[position].equalsIgnoreCase("Support")) {
            showSupportFragment();
        } else if (hamburgerMenuTitles[position].equalsIgnoreCase("Shop")) {
            showShoppingFragment();
        } else if (hamburgerMenuTitles[position].equalsIgnoreCase("Debug and Testing")) {
            showDebugTestFragment();
        } else {
            if(mHomeFragment == null) {
                mHomeFragment = new HomeFragment();
            }
            showFragment(mHomeFragment, mHomeFragment.getClass().getSimpleName());
        }
        philipsDrawerLayout.closeDrawer(navigationView);
    }

    private HomeFragment mHomeFragment = null;
    private InAppPurchasesFragment shoppingFragment = null;
    private DebugTestFragment debugTestFragment = null;
    private SettingsFragment settingsFragment = null;

    private void showShoppingFragment() {
        if(shoppingFragment == null) {
            shoppingFragment = new InAppPurchasesFragment();
        }
        showFragment(shoppingFragment, shoppingFragment.getClass().getSimpleName());
    }


    private void showSettingsFragment() {
        if(settingsFragment == null) {
            settingsFragment = new SettingsFragment();
        }

        showFragment(settingsFragment, settingsFragment.getClass().getSimpleName());
    }

    private void showSupportFragment() {
        if(!findFragmentByTag("digitalcare")) {
            if (mConsumerCareFragment == null) {
                mConsumerCareFragment = new ConsumerCareLauncher();
            }
            mConsumerCareFragment.initCC(this, actionBarClickListener);
        }
    }

    private void showDebugTestFragment() {
        DebugTestFragment debugTestFragment = new DebugTestFragment();

        showFragment(debugTestFragment, debugTestFragment.getClass().getSimpleName());
    }

    @Override
    public void onBackPressed() {
        if (philipsDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            philipsDrawerLayout.closeDrawer(Gravity.LEFT);
        }
        else {
            inAppPurchaseBackPress();
        }

        super.onBackPressed();
    }

    private void inAppPurchaseBackPress() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.vertical_Container);
        if(currentFragment != null && (currentFragment instanceof  InAppPurchasesFragment)){
            if(((InAppPurchasesFragment) currentFragment).onBackPressed()) {
                return;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseConsumerCare();
    }

    private void releaseConsumerCare() {
        if (mConsumerCareFragment != null) {
            mConsumerCareFragment.releaseConsumerCare();
        }
    }
}
