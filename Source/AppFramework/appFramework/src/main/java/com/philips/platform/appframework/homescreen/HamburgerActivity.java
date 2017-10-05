/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */
package com.philips.platform.appframework.homescreen;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.philips.cdp.di.iap.integration.IAPListener;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.cdp.uikit.hamburger.HamburgerAdapter;
import com.philips.cdp.uikit.hamburger.HamburgerItem;
import com.philips.cdp.uikit.utils.HamburgerUtil;
import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseActivity;
import com.philips.platform.baseapp.base.FragmentView;
import com.philips.platform.baseapp.screens.settingscreen.IndexSelectionListener;
import com.philips.platform.baseapp.screens.utility.Constants;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.baseapp.screens.utility.SharedPreferenceUtility;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.thememanager.UIDHelper;

import java.util.ArrayList;
/**
 * This is the Main activity which host the main hamburger menu
 * This activity is the container of all the other fragment for the app
 * ActionbarListener is implemented by this activty and all the logic related to handleBack handling and actionar is contained in this activity
 */
public class HamburgerActivity extends AbstractAppFrameworkBaseActivity implements IAPListener,IndexSelectionListener, FragmentManager.OnBackStackChangedListener, FragmentView {
    private static String TAG = HamburgerActivity.class.getSimpleName();
    private HamburgerUtil hamburgerUtil;
    private String[] hamburgerMenuTitles;
    private ArrayList<HamburgerItem> hamburgerItems;
    private DrawerLayout philipsDrawerLayout;
    private ListView drawerListView;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ImageView footerView;
    private HamburgerAdapter adapter;
    private int selectedIndex=0;
    //shoppingCartLayout;
    //    private UserRegistrationState userRegistrationState;
    private SharedPreferenceUtility sharedPreferenceUtility;
    private boolean isBackButtonVisible = false;
    Handler handler = new Handler();

   /* private ImageView cartIcon;
    private TextView cartCount;
    private boolean isCartVisible = true;*/

    /**
     * For instantiating the view and actionabar and hamburger menu initialization
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        RALog.d(TAG, " OnCreate ");
//        setTheme(R.style.Theme_Philips_DarkBlue_Gradient_NoActionBar);
        /*
         * Setting Philips UI KIT standard BLUE theme.
         */
        super.onCreate(savedInstanceState);
        presenter = new HamburgerActivityPresenter(this);
        sharedPreferenceUtility = new SharedPreferenceUtility(this);
        setContentView(R.layout.rap_activity_hamburger);
        initializeActivityContents();
    }

    protected void initializeActivityContents() {
        initViews();
        philipsDrawerLayout.addDrawerListener(configureDrawer());

        renderHamburgerMenu();
        getSupportFragmentManager().addOnBackStackChangedListener(this);
    }

    /**
     * For updating the hamburger drawer
     */
    private void renderHamburgerMenu() {
        RALog.d(TAG, " render HamburgerMenu ");
        hamburgerUtil = null;
        drawerListView = null;
        loadSlideMenuItems();
        setHamburgerAdapter();
        drawerListView = (ListView) findViewById(R.id.hamburger_list);
        hamburgerUtil = new HamburgerUtil(this, drawerListView);
        hamburgerUtil.updateSmartFooter(footerView, hamburgerItems.size());
        setDrawerAdapter();
        showNavigationDrawerItem(0);
        sharedPreferenceUtility.writePreferenceInt(Constants.HOME_FRAGMENT_PRESSED,0);
        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                if (!hamburgerMenuTitles[position].equalsIgnoreCase("Title")) {
                    if(position==selectedIndex){
                        philipsDrawerLayout.closeDrawer(navigationView);
                        return;
                    }
                    selectedIndex=position;
                    adapter.setSelectedIndex(position);
                    adapter.notifyDataSetChanged();
                    sharedPreferenceUtility.writePreferenceInt(Constants.HOME_FRAGMENT_PRESSED,position);
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
    }

    public HamburgerAdapter getHamburgerAdapter()
    {
        return this.adapter;
    }

    private void initViews() {
        RALog.d(TAG, " initViews");
        toolbar = (Toolbar) findViewById(R.id.uid_toolbar);
        philipsDrawerLayout = (DrawerLayout) findViewById(R.id.philips_drawer_layout);
        drawerListView = (ListView) findViewById(R.id.hamburger_list);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        footerView = (ImageView) findViewById(R.id.philips_logo);
        int resID = com.philips.cdp.uikit.R.drawable.uikit_philips_logo;
        footerView.setImageDrawable(VectorDrawable.create(this, resID));
        UIDHelper.setupToolbar(this);
        toolbar.setNavigationIcon(VectorDrawableCompat.create(getResources(), R.drawable.ic_hamburger_icon, getTheme()));
    }

    private void setDrawerAdapter() {
        RALog.d(TAG, "  Set Drawer Adapter  ");
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
        UIDHelper.setTitle(this, title);
    }

    protected ActionBarDrawerToggle configureDrawer() {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, philipsDrawerLayout,
                R.string.af_app_name, R.string.af_app_name) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        return drawerToggle;
    }

    private void setHamburgerAdapter() {
        for (int i = 0; i < hamburgerMenuTitles.length; i++) {
                hamburgerItems.add(new HamburgerItem(hamburgerMenuTitles[i],null));
        }
    }

    private void loadSlideMenuItems() {
        hamburgerMenuTitles = getResources().getStringArray(R.array.hamburger_drawer_items);
        hamburgerItems = new ArrayList<>();
    }


    @Override
    public void onBackPressed() {
        RALog.d(TAG, " on Back Pressed  ");
        if(philipsDrawerLayout.isDrawerOpen(navigationView))
        {
            philipsDrawerLayout.closeDrawer(navigationView);
        }
        else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment currentFrag = fragmentManager.findFragmentById(R.id.frame_container);
            boolean backState = false;
            if (fragmentManager.getBackStackEntryCount() == 1) {
                finishAffinity();
            } else if (currentFrag instanceof BackEventListener) {
                backState = ((BackEventListener) currentFrag).handleBackEvent();
                if (!backState) {
//                    ((AppFrameworkApplication)getApplicationContext()).getTargetFlowManager().getBackState();
                  //  adapter.setSelectedIndex(0);
                    if (fragmentManager.getBackStackEntryCount() == 2) {
                        updateSelectionIndex(0);
                    }
                    super.onBackPressed();
                }
            } else {
//                ((AppFrameworkApplication)getApplicationContext()).getTargetFlowManager().getBackState();
                if (fragmentManager.getBackStackEntryCount() == 2) {
                    updateSelectionIndex(0);
                }

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
        RALog.d(TAG, " onDestroy ");
        super.onDestroy();
//        userRegistrationState.unregisterUserRegistrationListener();
    }


   /* private void addIapCartCount() {
        try {

            IAPInterface iapInterface = ((AppFrameworkApplication)getApplicationContext()).getIap().getIapInterface();
            iapInterface.getProductCartCount(this);
        }catch (RuntimeException e){
        }
    }*/
    @Override
    protected void onResume() {
        super.onResume();
//        userRegistrationState = new UserRegistrationSettingsState();
//        if(userRegistrationState.getUserObject(this).isUserSignIn()){
//           // addIapCartCount();
//        }

    }

    /**
     * For Updating the actionbar title as coming from other components
     * @param i String res ID
     * @param b Whether handleBack is handled by them or not
     */
    @Override
    public void updateActionBar(@StringRes int i, boolean b) {
        UIDHelper.setTitle(this, i);
        updateActionBarIcon(b);
    }

    /**
     * For Updating the actionbar title as coming from other components
     * @param s String to be updated on actionbar title
     * @param b Whether handleBack is handled by them or not
     */
    @Override
    public void updateActionBar(String s, boolean b) {
        UIDHelper.setTitle(this, s);
        updateActionBarIcon(b);

    }

    public String getActionbarTag() {
        return (String) toolbar.getNavigationContentDescription();
    }

    /**
     * Method for showing the hamburger Icon or Back key on home fragments
     */
    public void updateActionBarIcon(boolean isBackButtonVisible) {
        RALog.d(TAG, " updateActionBarIcon : " + isBackButtonVisible);
        int navigationDrawableId = isBackButtonVisible ? R.drawable.left_arrow : R.drawable.ic_hamburger_icon;
        toolbar.setNavigationIcon(VectorDrawableCompat.create(getResources(), navigationDrawableId, getTheme()));
        toolbar.setNavigationContentDescription(String.valueOf(navigationDrawableId));
        this.isBackButtonVisible = isBackButtonVisible;
    }

    /*public void cartIconVisibility(boolean shouldShow) {
        if(shouldShow){
            cartIcon.setVisibility(View.VISIBLE);
            int cartItemsCount = getCartItemCount();
                if (cartItemsCount > 0) {
                        cartCount.setVisibility(View.VISIBLE);
                        cartCount.setText(String.valueOf(cartItemsCount));
                }else {
                    cartCount.setVisibility(View.GONE);
                }
        } else {
                cartIcon.setVisibility(View.GONE);
                cartCount.setVisibility(View.GONE);
        }
    }*/
    @Override
    public void onGetCartCount(int cartCount) {
        /*setCartItemCount(cartCount);
        if(cartCount > 0 && cartIcon.getVisibility() == View.VISIBLE) {
            cartIconVisibility(true);
        }*/
    }

    @Override
    public void onUpdateCartCount() {
        /*if(userRegistrationState.getUserObject(this).isUserSignIn()){
            addIapCartCount();
        }*/
    }

    @Override
    public void updateCartIconVisibility(boolean shouldShow) {
      //  isCartVisible = shouldShow;
    }

    @Override
    public void onGetCompleteProductList(ArrayList<String> arrayList) {

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onSuccess(boolean isCartVisible) {

    }

    @Override
    public void onFailure(int i) {
     //   showToast(i);
    }

    /*private void showToast(int errorCode) {
        String errorText = getResources().getString(R.string.af_iap_server_error);
        if (IAPConstant.IAP_ERROR_NO_CONNECTION == errorCode) {
            errorText = getResources().getString(R.string.af_iap_no_connection);
        } else if (IAPConstant.IAP_ERROR_CONNECTION_TIME_OUT == errorCode) {
            errorText = getResources().getString(R.string.af_iap_connection_time_out);
        } else if (IAPConstant.IAP_ERROR_AUTHENTICATION_FAILURE == errorCode) {
            errorText = getResources().getString(R.string.af_iap_authentication_failure);
        } else if (IAPConstant.IAP_ERROR_INSUFFICIENT_STOCK_ERROR == errorCode) {
            errorText = getResources().getString(R.string.af_iap_prod_out_of_stock);
        }
        Toast toast = Toast.makeText(this, errorText, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }*/
    @Override
    public void onBackStackChanged() {
        /*if(getSupportFragmentManager().getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1);
            String str = backEntry.getName();
            if(null != str){
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(str);
                if(fragment instanceof InAppBaseFragment){
                    cartIconVisibility(isCartVisible);
                }
                else {
                    cartIconVisibility(true);
                }
            }
        }*/
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
    public FragmentActivity getFragmentActivity() {
        return this;
    }


    @Override
    public void updateSelectionIndex(final int position) {
        RALog.d(TAG, " setting selection index to 0  hamburger menu ");

        if(handler!=null)
            handler.post(new Runnable() {
            @Override
            public void run() {
                adapter.setSelectedIndex(position);
                selectedIndex=position;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (isBackButtonVisible) {
                    onBackPressed();
                } else {
                    philipsDrawerLayout.openDrawer(navigationView);
                }
                break;
        }
        return true;
    }
}
