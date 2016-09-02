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
import android.widget.Toast;

import com.philips.cdp.di.iap.Fragments.BaseAnimationSupportFragment;
import com.philips.cdp.di.iap.integration.IAPInterface;
import com.philips.cdp.di.iap.session.IAPListener;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.registration.ui.traditional.RegistrationFragment;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.cdp.uikit.hamburger.HamburgerAdapter;
import com.philips.cdp.uikit.hamburger.HamburgerItem;
import com.philips.cdp.uikit.utils.HamburgerUtil;
import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.utility.SharedPreferenceUtility;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.modularui.statecontroller.UIFlowManager;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.stateimpl.UserRegistrationState;
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
    private static HamburgerUtil hamburgerUtil;
    private ImageView hamburgerIcon;
    private LinearLayout hamburgerClick = null;
    private static int mCartItemCount = 0;
    private final int CART_POSITION_IN_MENU = 2;
    private UserRegistrationState userRegistrationState;
    private SharedPreferenceUtility sharedPreferenceUtility;
    private static final String HOME_FRAGMENT_PRESSED = "Home_Fragment_Pressed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Philips_DarkBlue_Gradient_NoActionBar);
        /*
         * Setting Philips UI KIT standard BLUE theme.
         */
        super.onCreate(savedInstanceState);
        presenter = new HomeActivityPresenter();
        AppFrameworkApplication.loggingInterface.log(LoggingInterface.LogLevel.INFO, TAG, " HomeScreen Activity Created ");
        sharedPreferenceUtility = new SharedPreferenceUtility(this);
        setContentView(R.layout.uikit_hamburger_menu);
        initViews();
        initActionBar(getSupportActionBar());
        configureDrawer();
        renderHamburgerMenu();
    }

    public void cartCountUpdate(int  count) {
        mCartItemCount = count;
        hamburgerItems.get(2).setCount(count);
        adapter.notifyDataSetChanged();
    }

    private void renderHamburgerMenu() {
        hamburgerUtil = null;
        drawerListView = null;
        loadSlideMenuItems();
        setHamburgerAdaptor();
        drawerListView = (ListView) findViewById(R.id.hamburger_list);
        hamburgerUtil = new HamburgerUtil(this, drawerListView);
        hamburgerUtil.updateSmartFooter(footerView, hamburgerItems.size());
        setDrawerAdaptor();
        showNavigationDrawerItem(0);
        sharedPreferenceUtility.writePreferenceInt(HOME_FRAGMENT_PRESSED,0);
        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                if (!hamburgerMenuTitles[position].equalsIgnoreCase("Title")) {
                    adapter.setSelectedIndex(position);
                    adapter.notifyDataSetChanged();
                    sharedPreferenceUtility.writePreferenceInt(HOME_FRAGMENT_PRESSED,position);
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
        hamburgerIcon.setTag("HamburgerIcon");
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
        for (int i = 0; i < hamburgerMenuTitles.length; i++) {
            if (i == 2 && mCartItemCount > 0) {
                hamburgerItems.add(new HamburgerItem(hamburgerMenuTitles[i], null, mCartItemCount));
            } else {
                hamburgerItems.add(new HamburgerItem(hamburgerMenuTitles[i],null));
            }
        }
    }

    private void loadSlideMenuItems() {
        hamburgerMenuTitles = getResources().getStringArray(R.array.hamburger_drawer_items);
        hamburgerItems = new ArrayList<>();
    }


    @Override
    public void onBackPressed() {
        if(null != hamburgerIcon) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment currentFrag = fragmentManager.findFragmentById(R.id.frame_container);
            boolean backState = false;
            if (hamburgerIcon.getTag().equals("HamburgerIcon")) {
                if (philipsDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    philipsDrawerLayout.closeDrawer(Gravity.LEFT);
                    return;
                }
                if (currentFrag instanceof HomeFragment) {
                    finishAffinity();
                } else if (fragmentManager.getBackStackEntryCount() == 1) {
                    showNavigationDrawerItem(0);
                } else if (currentFrag != null && currentFrag instanceof BackEventListener && currentFrag instanceof RegistrationFragment) {
                    backState = ((BackEventListener) currentFrag).handleBackEvent();
                    if (!backState) {
                        fragmentManager.popBackStack();
                    }
                } else if (currentFrag != null && currentFrag instanceof BackEventListener && currentFrag instanceof BaseAnimationSupportFragment) {
                    backState = ((BackEventListener) currentFrag).handleBackEvent();
                    if (!backState) {
                        popBackTillHomeFragment();
                    }
                } else if (currentFrag != null && currentFrag instanceof BackEventListener && currentFrag.getTag().equalsIgnoreCase("digitalcare")) {
                    backState = ((BackEventListener) currentFrag).handleBackEvent();
                    if (!backState) {
                        popBackTillHomeFragment();
                    }
                } else {
                    AppFrameworkApplication applicationContext = (AppFrameworkApplication) HomeActivity.this.getApplicationContext();
                    UIFlowManager flowManager = applicationContext.getFlowManager();
                    UIState currentState = flowManager.getCurrentState();
                    currentState.back(this);
                }
            } else if (hamburgerIcon.getTag().equals("BackButton")) {
                if (currentFrag != null && currentFrag instanceof BackEventListener){
                    backState = ((BackEventListener) currentFrag).handleBackEvent();
                    if (!backState) {
                       super.onBackPressed();
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferenceUtility.writePreferenceInt(HOME_FRAGMENT_PRESSED,UIState.UI_HOME_FRAGMENT_STATE);
    }


    private void addIapCartCount() {
        IAPInterface iapInterface = ((AppFrameworkApplication)getApplicationContext()).getIapInterface();
        iapInterface.getProductCartCount(new IAPListener() {
            @Override
            public void onGetCartCount(int i) {
                cartCountUpdate(i);
            }

            @Override
            public void onGetCompleteProductList(ArrayList<String> arrayList) {

            }

            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(int i) {
                showIAPToast(i);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        showNavigationDrawerItem(sharedPreferenceUtility.getPreferenceInt(HOME_FRAGMENT_PRESSED));
        userRegistrationState = new UserRegistrationState(UIState.UI_USER_REGISTRATION_STATE);
        if(userRegistrationState.getUserObject(this).isUserSignIn()){
            addIapCartCount();
        }

    }

    private void showIAPToast(int errorCode) {
        String errorText = getResources().getString(R.string.iap_unknown_error);
        if (IAPConstant.IAP_ERROR_NO_CONNECTION == errorCode) {
        } else if (IAPConstant.IAP_ERROR_CONNECTION_TIME_OUT == errorCode) {
        } else if (IAPConstant.IAP_ERROR_AUTHENTICATION_FAILURE == errorCode) {
        } else if (IAPConstant.IAP_ERROR_INSUFFICIENT_STOCK_ERROR == errorCode) {
        }

        if (null != this) {
            Toast toast = Toast.makeText(this, errorText, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    @Override
    public void updateActionBar(@StringRes int i, boolean b) {

        setTitle(getResources().getString(i));
        if (b) {
            hamburgerIcon.setImageDrawable(VectorDrawable.create(this, R.drawable.left_arrow));
            hamburgerIcon.setTag("BackButton");
            hamburgerClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   onBackPressed();
                }
            });
        } else {
            hamburgerIcon.setImageDrawable(VectorDrawable.create(HomeActivity.this, R.drawable.uikit_hamburger_icon));
            hamburgerIcon.setTag("HamburgerIcon");
            hamburgerClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    philipsDrawerLayout.openDrawer(navigationView);
                }
            });

        }
    }

    @Override
    public void updateActionBar(String s, boolean b) {

        setTitle(s);
        if (b) {
            hamburgerIcon.setImageDrawable(VectorDrawable.create(this, R.drawable.left_arrow));
            hamburgerIcon.setTag("BackButton");
            hamburgerClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } else {
            hamburgerIcon.setImageDrawable(VectorDrawable.create(HomeActivity.this, R.drawable.uikit_hamburger_icon));
            hamburgerIcon.setTag("HamburgerIcon");
            hamburgerClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    philipsDrawerLayout.openDrawer(navigationView);
                }
            });

        }
    }
}
