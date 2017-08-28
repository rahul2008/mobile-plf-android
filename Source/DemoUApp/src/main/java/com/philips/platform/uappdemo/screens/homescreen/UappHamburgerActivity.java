/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.uappdemo.screens.homescreen;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.cdp.uikit.hamburger.HamburgerAdapter;
import com.philips.cdp.uikit.hamburger.HamburgerItem;
import com.philips.cdp.uikit.utils.HamburgerUtil;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.flowmanager.utility.UappConstants;
import com.philips.platform.flowmanager.utility.UappSharedPreference;
import com.philips.platform.uappdemo.screens.base.UappBaseActivity;
import com.philips.platform.uappdemo.screens.base.UappFragmentView;
import com.philips.platform.uappdemo.UappDemoUiHelper;
import com.philips.platform.uappdemolibrary.R;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the Main activity which host the main hamburger menu
 * This activity is the container of all the other fragment for the app
 * ActionbarListener is implemented by this activty and all the logic related to handleBack handling and actionar is contained in this activity
 */
public class UappHamburgerActivity extends UappBaseActivity implements FragmentManager.OnBackStackChangedListener, UappFragmentView {
    private static String TAG = UappHamburgerActivity.class.getSimpleName();
    protected TextView actionBarTitle;
    private HamburgerUtil hamburgerUtil;
    private String[] hamburgerMenuTitles;
    private ArrayList<HamburgerItem> hamburgerItems;
    private DrawerLayout philipsDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private ListView drawerListView;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ImageView footerView;
    private HamburgerAdapter adapter;
    private ImageView hamburgerIcon;
    private FrameLayout hamburgerClick = null;//shoppingCartLayout;
    private UappSharedPreference uappSharedPreference;

    /**
     * For instantiating the view and actionabar and hamburger menu initialization
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initDLS();
        /*
         * Setting Philips UI KIT standard BLUE theme.
         */
        super.onCreate(savedInstanceState);
        presenter = new HamburgerActivityPresenter(this);
        uappSharedPreference = new UappSharedPreference(this);
        setContentView(R.layout.uikit_hamburger_menu);
        initViews();
        setActionBar(getSupportActionBar());
        configureDrawer();
        renderHamburgerMenu();
        getSupportFragmentManager().addOnBackStackChangedListener(this);
    }

    public void initDLS() {
        UIDHelper.init(new ThemeConfiguration(this, ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT, AccentRange.ORANGE));
        getTheme().applyStyle(R.style.Theme_Philips_DarkBlue_NoActionBar, true);
    }


    /**
     * For updating the hamburger drawer
     */
    private void renderHamburgerMenu() {
        hamburgerUtil = null;
        drawerListView = null;
        loadSlideMenuItems();
        setHamburgerAdapter();
        drawerListView = (ListView) findViewById(R.id.hamburger_list);
        hamburgerUtil = new HamburgerUtil(this, drawerListView);
        hamburgerUtil.updateSmartFooter(footerView, hamburgerItems.size());
        setDrawerAdapter();
        showNavigationDrawerItem(0);
        uappSharedPreference.writePreferenceInt(UappConstants.HOME_FRAGMENT_PRESSED,0);
        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                if (!hamburgerMenuTitles[position].equalsIgnoreCase("Title")) {
                    adapter.setSelectedIndex(position);
                    adapter.notifyDataSetChanged();
                    uappSharedPreference.writePreferenceInt(UappConstants.HOME_FRAGMENT_PRESSED,position);
                    showNavigationDrawerItem(position);

                }
            }
        });
    }

    /**
     * To show navigation Drawer
     * @param position : Pass the position of hamburger item to be shown
     */
    private void showNavigationDrawerItem(int position) {
        philipsDrawerLayout.closeDrawer(navigationView);
        presenter.onEvent(position);
        setTitle(hamburgerMenuTitles[position]);
    }

    public HamburgerAdapter getHamburgerAdapter()
    {
        return this.adapter;
    }


    /**
     * To set the actionbar
     * @param actionBar : Requires the actionbar obejct
     */
    private void setActionBar(ActionBar actionBar) {
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the textview in the ActionBar !
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER);
   View mCustomView = LayoutInflater.from(this).inflate(R.layout.af_action_bar_shopping_cart, null); // layout which contains your button.
        hamburgerIcon = (ImageView) mCustomView.findViewById(R.id.af_hamburger_imageview);
        hamburgerIcon.setImageDrawable(VectorDrawable.create(this, R.drawable.uikit_hamburger_icon));
        hamburgerClick = (FrameLayout) mCustomView.findViewById(R.id.af_hamburger_frame_layout);
        hamburgerClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                philipsDrawerLayout.openDrawer(navigationView);
            }
        });
        actionBarTitle = (TextView) mCustomView.findViewById(R.id.af_actionbar_title);
        setTitle(getResources().getString(R.string.app_name));
        actionBar.setCustomView(mCustomView, params);
        Toolbar parent = (Toolbar) mCustomView.getParent();
        parent.setContentInsetsAbsolute(0, 0);
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

    private void setDrawerAdapter() {
        adapter = null;
        TextView totalCountView = (TextView) findViewById(R.id.hamburger_count);
        adapter = new HamburgerAdapter(this,
                hamburgerItems, totalCountView, false);
        adapter.notifyDataSetChanged();
        drawerListView.setAdapter(adapter);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        actionBarTitle.setText(title);
        actionBarTitle.setSelected(true);
    }

    private void configureDrawer() {
        drawerToggle = new ActionBarDrawerToggle(this, philipsDrawerLayout, R.string.af_app_name, R.string.af_app_name) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        philipsDrawerLayout.addDrawerListener(drawerToggle);
    }

    private void setHamburgerAdapter() {
        for (int i = 0; i < hamburgerMenuTitles.length; i++) {
                hamburgerItems.add(new HamburgerItem(hamburgerMenuTitles[i],null));
        }
    }

    private void loadSlideMenuItems() {
        hamburgerMenuTitles = getResources().getStringArray(R.array.uapp_hamburger_drawer_items);
        hamburgerItems = new ArrayList<>();
    }


    @Override
    public void onBackPressed() {
        if(philipsDrawerLayout.isDrawerOpen(navigationView))
        {
            philipsDrawerLayout.closeDrawer(navigationView);
        }
        else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment currentFrag = fragmentManager.findFragmentById(R.id.frame_container);
            boolean backState = false;
            if (fragmentManager.getBackStackEntryCount() == 1) {
                finish();
            } else if (currentFrag instanceof BackEventListener) {
                backState = ((BackEventListener) currentFrag).handleBackEvent();
                if (!backState) {
                    adapter.setSelectedIndex(0);
                    setTitle(hamburgerMenuTitles[0]);
                    super.onBackPressed();
                }
            } else {
                adapter.setSelectedIndex(0);
                setTitle(hamburgerMenuTitles[0]);
                super.onBackPressed();
            }
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getFragmentActivity().getWindow().getDecorView().requestLayout();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    /**
     * For Updating the actionbar title as coming from other components
     * @param i String res ID
     * @param b Whether handleBack is handled by them or not
     */
    @Override
    public void updateActionBar(@StringRes int i, boolean b) {
        setTitle(getResources().getString(i));
        updateActionBarIcon(b);
    }

    /**
     * For Updating the actionbar title as coming from other components
     * @param s String to be updated on actionbar title
     * @param b Whether handleBack is handled by them or not
     */
    @Override
    public void updateActionBar(String s, boolean b) {
        setTitle(s);
        updateActionBarIcon(b);

    }

    /**
     * Method for showing the hamburger Icon or Back key on home fragments
     */
    public void updateActionBarIcon(boolean b)
    {
        if (b) {
            hamburgerIcon.setImageDrawable(VectorDrawable.create(this, R.drawable.left_arrow));
            hamburgerClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } else {
            hamburgerIcon.setImageDrawable(VectorDrawable.create(UappHamburgerActivity.this, R.drawable.uikit_hamburger_icon));
            hamburgerClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    philipsDrawerLayout.openDrawer(navigationView);
                }
            });

        }
    }

    @Override
    public void onBackStackChanged() {
    }

    @Override
    public ActionBarListener getActionBarListener() {
        return this;
    }

    @Override
    public int getContainerId() {
        return R.id.frame_container;
    }

    @Override
    public BaseFlowManager getTargetFlowManager() {
        return UappDemoUiHelper.getInstance().getFlowManager();
    }

    @Override
    public FragmentActivity getFragmentActivity() {
        return this;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null) {
                    fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
            }
        }
    }
}
