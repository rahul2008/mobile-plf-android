/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.tabbedscreen;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.widget.Toast;

import com.philips.cdp.di.iap.integration.IAPListener;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.uikit.utils.TabUtils;
import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseActivity;
import com.philips.platform.baseapp.base.FragmentView;
import com.philips.platform.baseapp.screens.settingscreen.IndexSelectionListener;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;

import java.util.ArrayList;

/**
 * This is the Main activity which host the main hamburger menu
 * This activity is the container of all the other fragment for the app
 * ActionbarListener is implemented by this activty and all the logic related to handleBack handling and actionar is contained in this activity
 */
public class TabbedActivity extends AbstractAppFrameworkBaseActivity implements FragmentManager.OnBackStackChangedListener, IndexSelectionListener,FragmentView, IAPListener {
    private static String TAG = TabbedActivity.class.getSimpleName();
    private TabLayout topLayout;

    /**
     * For instantiating the view and actionabar and hamburger menu initialization
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        RALog.d(TAG, " OnCreate ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.af_home_tab_top);
        presenter = new TabbedActivityPresenter(this);

        TabUtils.disableActionbarShadow(this);
        setTopBar();
        setViewPager();

        getSupportFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("top", topLayout.getSelectedTabPosition());
    }

    private void setViewPager() {
        presenter.onEvent(0);
        topLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(final TabLayout.Tab tab) {
                presenter.onEvent(tab.getPosition());
            }

            @Override
            public void onTabUnselected(final TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(final TabLayout.Tab tab) {

            }
        });
    }

    private void setTopBar() {
        topLayout = (TabLayout) findViewById(R.id.tab_bar);
        TabUtils utils = new TabUtils(this, topLayout, true);

        TabLayout.Tab tab = utils.newTab(R.string.uikit_splash_title, android.R.drawable.btn_star, 0);
        utils.setTitle(tab, R.string.RA_HomeTab_Menu_Title);
        topLayout.addTab(tab);

        tab = utils.newTab(R.string.uikit_splash_title, android.R.drawable.btn_radio, 0);
        utils.setTitle(tab, R.string.RA_Settings_Menu_Title);
        topLayout.addTab(tab);

        tab = utils.newTab(R.string.uikit_splash_title, android.R.drawable.btn_star_big_on, 0);
        utils.setTitle(tab, R.string.RA_Philips_Shop_Menu_Title);
        topLayout.addTab(tab);

        tab = utils.newTab(R.string.uikit_splash_title, android.R.drawable.btn_star_big_off, 0);
        utils.setTitle(tab, R.string.RA_Support_Menu_Title);
        topLayout.addTab(tab);

        tab = utils.newTab(R.string.uikit_splash_title, android.R.drawable.btn_star_big_off, 0);
        utils.setTitle(tab, "About");
        topLayout.addTab(tab);

        tab = utils.newTab(R.string.uikit_splash_title, android.R.drawable.btn_star_big_off, 0);
        utils.setTitle(tab, "Data Sync");
        topLayout.addTab(tab);

        tab = utils.newTab(R.string.uikit_splash_title, android.R.drawable.btn_star_big_off, 0);
        utils.setTitle(tab, "Connectivity");
        topLayout.addTab(tab);

        tab = utils.newTab(R.string.uikit_splash_title, android.R.drawable.btn_star_big_off, 0);
        utils.setTitle(tab, R.string.RA_Coco_Version);
        topLayout.addTab(tab);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
    }

    @Override
    public void onBackPressed() {
        RALog.d(TAG," Back Pressed ");
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFrag = fragmentManager.findFragmentById(R.id.pagerTabbedHome);
        boolean backState = false;
        if (fragmentManager.getBackStackEntryCount() == 1) {
            finishAffinity();
        } else if (currentFrag instanceof BackEventListener) {
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

    @Override
    protected void onResume() {
        super.onResume();
        TabUtils.adjustTabs(topLayout, this);
    }

    /**
     * For Updating the actionbar title as coming from other components
     *
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
     *
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
    public void updateActionBarIcon(boolean b) {
    }

    private void showToast(int errorCode) {
        String errorText = getResources().getString(R.string.RA_Iap_Server_Error);
        if (IAPConstant.IAP_ERROR_NO_CONNECTION == errorCode) {
            errorText = getResources().getString(R.string.RA_Iap_No_Connection);
        } else if (IAPConstant.IAP_ERROR_CONNECTION_TIME_OUT == errorCode) {
            errorText = getResources().getString(R.string.RA_Iap_Connection_Time_Out);
        } else if (IAPConstant.IAP_ERROR_AUTHENTICATION_FAILURE == errorCode) {
            errorText = getResources().getString(R.string.RA_Iap_Authentication_Failure);
        } else if (IAPConstant.IAP_ERROR_INSUFFICIENT_STOCK_ERROR == errorCode) {
            errorText = getResources().getString(R.string.RA_Iap_Product_Out_Of_Stock);
        }
        Toast toast = Toast.makeText(this, errorText, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public void onBackStackChanged() {
        /*if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1);
            String str = backEntry.getName();
            if (null != str) {
                if (str.contains(Constants.IAP_PHILIPS_SHOP_FRAGMENT_TAG) || str.contains(Constants.IAP_PURCHASE_HISTORY_FRAGMENT_TAG) || str.contains(Constants.IAP_SHOPPING_CART_FRAGMENT_TAG)) {
                    return;
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
        return R.id.pagerTabbedHome;
    }

    @Override
    public FragmentActivity getFragmentActivity() {
        return this;
    }

    @Override
    public void onGetCartCount(final int i) {

    }

    @Override
    public void onUpdateCartCount() {

    }

    @Override
    public void updateCartIconVisibility(final boolean b) {

    }

    @Override
    public void onGetCompleteProductList(final ArrayList<String> arrayList) {

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure(final int i) {

    }
    @Override
    public void updateSelectionIndex(int position) {
    }
}
