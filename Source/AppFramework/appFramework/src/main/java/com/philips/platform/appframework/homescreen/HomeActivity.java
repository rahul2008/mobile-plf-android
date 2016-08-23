/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.homescreen;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import com.philips.cdp.registration.ui.utils.RegistrationLaunchHelper;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.cdp.uikit.hamburger.HamburgerAdapter;
import com.philips.cdp.uikit.hamburger.HamburgerItem;
import com.philips.cdp.uikit.utils.HamburgerUtil;
import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.R;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.modularui.statecontroller.UIFlowManager;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppFrameworkBaseActivity implements ActionBarListener {
    private static String TAG = HomeActivity.class.getSimpleName();
    private String[] hamburgerMenuTitles;
    private ArrayList<HamburgerItem> hamburgerItems;
    private DrawerLayout philipsDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView drawerListView;
    private NavigationView navigationView;
    private Toolbar toolbar;
    protected TextView actionBarTitle;
    private ImageView footerView;
    private HamburgerAdapter adapter;
    private TextView actionBarCount;
    private HamburgerUtil hamburgerUtil;
    private ImageView hamburgerIcon;
    private LinearLayout hamburgerClick = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Philips_DarkBlue_Gradient_NoActionBar);
        /*
         * Setting Philips UI KIT standard BLUE theme.
         */
        super.onCreate(savedInstanceState);
        presenter = new HomeActivityPresenter();
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
        showNavigationDrawerItem(0);
        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                if (!hamburgerMenuTitles[position].equalsIgnoreCase("Title")) {
                    adapter.setSelectedIndex(position);
                    showNavigationDrawerItem(position);
                }
            }
        });
    }

    private void showNavigationDrawerItem(int position) {

        philipsDrawerLayout.closeDrawer(navigationView);
        presenter.onClick(position, HomeActivity.this);
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

    @Override
    public void onBackPressed() {
        if (philipsDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            philipsDrawerLayout.closeDrawer(Gravity.LEFT);
            return;
        } else if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finishAffinity();
        } else if (findIfHomeFragmentSentBack()) {
            finishAffinity();
        } /*else if (findFragmentByTag(InAppPurchasesFragment.TAG)) {
            if (!inAppPurchaseBackPress()) {
                super.popBackTillHomeFragment();
            }
        } */else if (findFragmentByTag("Registration_fragment_tag")) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = fragmentManager
                    .findFragmentById(R.id.fl_reg_fragment_container);
            if (fragment != null && fragment instanceof BackEventListener) {
                boolean isConsumed = ((BackEventListener) fragment).handleBackEvent();
                if (isConsumed)
                    super.popBack();
            }
        } else {
            AppFrameworkApplication applicationContext = (AppFrameworkApplication) HomeActivity.this.getApplicationContext();
            UIFlowManager flowManager = applicationContext.getFlowManager();
            UIState currentState = flowManager.getCurrentState();
            currentState.back(this);
        }
    }

    private boolean findIfHomeFragmentSentBack() {
        FragmentManager.BackStackEntry backStackEntryAt = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1);
        String name = backStackEntryAt.getName();
        if(name.equalsIgnoreCase(HomeFragment.TAG))
            return true;
        return false;
    }

    private boolean inAppPurchaseBackPress() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_container);
        /*if(currentFragment != null && (currentFragment instanceof InAppPurchasesFragment)){
            boolean isBackPressed = ((InAppPurchasesFragment) currentFragment).onBackPressed();
            return isBackPressed;
        }*/
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /*@Override
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
            hamburgerIcon.setImageDrawable(VectorDrawable.create(this, R.drawable.left_arrow));
            hamburgerClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    backstackFragment();
                }
            });
        }
    }
*/
    public void updateTitle(){
        hamburgerIcon.setImageDrawable(VectorDrawable.create(this, R.drawable.left_arrow));
        hamburgerClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        actionBarTitle.setText(R.string.af_app_name);
    }

    public void updateTitleWithBack(){
        hamburgerIcon.setImageDrawable(VectorDrawable.create(this, R.drawable.left_arrow));
        hamburgerClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        actionBarTitle.setText(R.string.af_app_name);
    }

    public void updateTitleWithoutBack(){
        hamburgerIcon.setImageDrawable(VectorDrawable.create(HomeActivity.this, R.drawable.uikit_hamburger_icon));
        hamburgerClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                philipsDrawerLayout.openDrawer(navigationView);
            }
        });
        actionBarTitle.setText(R.string.af_app_name);
    }

    @Override
    public void updateActionBar(@StringRes int i, boolean hamburgerIconAvailable) {
        if (hamburgerIconAvailable) {
            hamburgerIcon.setImageDrawable(VectorDrawable.create(HomeActivity.this, R.drawable.uikit_hamburger_icon));
            hamburgerClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    philipsDrawerLayout.openDrawer(navigationView);
                }
            });
        } else {
            hamburgerIcon.setImageDrawable(VectorDrawable.create(this, R.drawable.left_arrow));
            hamburgerClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    backstackFragment();
                }
            });
        }
    }

    @Override
    public void updateActionBar(String s, boolean hamburgerIconAvailable) {
        if (hamburgerIconAvailable) {
            hamburgerIcon.setImageDrawable(VectorDrawable.create(HomeActivity.this, R.drawable.uikit_hamburger_icon));
            hamburgerClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    philipsDrawerLayout.openDrawer(navigationView);
                }
            });
        } else {
            hamburgerIcon.setImageDrawable(VectorDrawable.create(this, R.drawable.left_arrow));
            hamburgerClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    backstackFragment();
                }
            });
        }

    }
  /*  @Override
    public void updateActionbar(String s) {

    }*/

}
