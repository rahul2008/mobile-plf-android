/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.homescreen;

import android.graphics.drawable.Drawable;
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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.di.iap.integration.IAPInterface;
import com.philips.cdp.di.iap.session.IAPListener;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.cdp.uikit.hamburger.HamburgerAdapter;
import com.philips.cdp.uikit.hamburger.HamburgerItem;
import com.philips.cdp.uikit.utils.HamburgerUtil;
import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.utility.Constants;
import com.philips.platform.appframework.utility.SharedPreferenceUtility;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.stateimpl.IAPState;
import com.philips.platform.modularui.stateimpl.UserRegistrationState;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;

import java.util.ArrayList;
/**
 * This is the Main activity which host the main hamburger menu
 * This activity is the container of all the other fragment for the app
 * ActionbarListener is implemented by this activty and all the logic related to handleBack handling and actionar is contained in this activity
 */
public class HomeActivity extends AppFrameworkBaseActivity implements IAPListener,FragmentManager.OnBackStackChangedListener {
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
    private static HamburgerUtil hamburgerUtil;
    private ImageView hamburgerIcon;
    private FrameLayout hamburgerClick = null,shoppingCartLayout;
    private UserRegistrationState userRegistrationState;
    private SharedPreferenceUtility sharedPreferenceUtility;
    private ImageView cartIcon;
    private TextView cartCount;
    private int cartItemCount = 0;

    /**
     * For instantiating the view and actionabar and hamburger menu initialization
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Philips_DarkBlue_Gradient_NoActionBar);
        /*
         * Setting Philips UI KIT standard BLUE theme.
         */
        super.onCreate(savedInstanceState);
        presenter = new HomeActivityPresenter();
        sharedPreferenceUtility = new SharedPreferenceUtility(this);
        setContentView(R.layout.uikit_hamburger_menu);
        initViews();
        setActionBar(getSupportActionBar());
        configureDrawer();
        renderHamburgerMenu();
        getSupportFragmentManager().addOnBackStackChangedListener(this);
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
        sharedPreferenceUtility.writePreferenceInt(Constants.HOME_FRAGMENT_PRESSED,0);
        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                if (!hamburgerMenuTitles[position].equalsIgnoreCase("Title")) {
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
        presenter.onClick(position, HomeActivity.this);
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
        setTitle(getResources().getString(com.philips.cdp.di.iap.R.string.app_name));
        cartIcon = (ImageView) mCustomView.findViewById(R.id.af_shoppng_cart_icon);
        shoppingCartLayout = (FrameLayout) mCustomView.findViewById(R.id.af_cart_layout);
        Drawable mCartIconDrawable = VectorDrawable.create(this, R.drawable.uikit_cart);
        cartIcon.setBackground(mCartIconDrawable);
        shoppingCartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                philipsDrawerLayout.closeDrawer(navigationView);
                presenter.onClick(Constants.UI_SHOPPING_CART_BUTTON_CLICK, HomeActivity.this);
            }
        });
        cartCount = (TextView) mCustomView.findViewById(R.id.af_cart_count_view);
        cartCount.setVisibility(View.GONE);
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

    public int getCartItemCount() {
        return cartItemCount;
    }

    public void setCartItemCount(int cartItemCount) {
        this.cartItemCount = cartItemCount;
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
        hamburgerMenuTitles = getResources().getStringArray(R.array.hamburger_drawer_items);
        hamburgerItems = new ArrayList<>();
    }


    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFrag = fragmentManager.findFragmentById(R.id.frame_container);
        boolean backState = false;
        if(fragmentManager.getBackStackEntryCount() == 1){
            finishAffinity();
        } else if(currentFrag instanceof BackEventListener){
            backState = ((BackEventListener) currentFrag).handleBackEvent();
            if (!backState) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void addIapCartCount() {
        try {

            IAPInterface iapInterface = ((AppFrameworkApplication)getApplicationContext()).getIap().getIapInterface();
            iapInterface.getProductCartCount(this);
        }catch (RuntimeException e){
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        userRegistrationState = new UserRegistrationState(UIState.UI_USER_REGISTRATION_STATE);
        if(userRegistrationState.getUserObject(this).isUserSignIn()){
            addIapCartCount();
        }

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
            hamburgerIcon.setImageDrawable(VectorDrawable.create(HomeActivity.this, R.drawable.uikit_hamburger_icon));
            hamburgerClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    philipsDrawerLayout.openDrawer(navigationView);
                }
            });

        }
    }

    public void cartIconVisibility(boolean shouldShow) {
        if(shouldShow){
            cartIcon.setVisibility(View.VISIBLE);
                if (cartItemCount > 0) {
                        cartCount.setVisibility(View.VISIBLE);
                        cartCount.setText(String.valueOf(cartItemCount));
                }else {
                    cartCount.setVisibility(View.GONE);
                }
        } else {
                cartIcon.setVisibility(View.GONE);
                cartCount.setVisibility(View.GONE);
        }
    }
    @Override
    public void onGetCartCount(int count) {
        cartItemCount = count;
        if(cartItemCount > 0 && cartIcon.getVisibility() == View.VISIBLE) {
            cartIconVisibility(true);
        }
    }

    @Override
    public void onUpdateCartCount() {
        if(userRegistrationState.getUserObject(this).isUserSignIn()){
            addIapCartCount();
        }
    }

    @Override
    public void updateCartIconVisibility(boolean shouldShow) {
        cartIconVisibility(shouldShow);
    }

    @Override
    public void onGetCompleteProductList(ArrayList<String> arrayList) {

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure(int i) {
        showToast(i);
    }

    private void showToast(int errorCode) {
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
    }
    @Override
    public void onBackStackChanged() {
        if(getSupportFragmentManager().getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1);
            String str = backEntry.getName();
            if (null != str) {
                if(str.contains(Constants.IAP_PHILIPS_SHOP_FRAGMENT_TAG) || str.contains(Constants.IAP_PURCHASE_HISTORY_FRAGMENT_TAG) || str.contains(Constants.IAP_SHOPPING_CART_FRAGMENT_TAG)){
                    return;
                } else {
                    cartIconVisibility(true);
                }
            }
        }
    }
}
