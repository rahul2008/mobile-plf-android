package com.philips.cdp.productselection.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.philips.cdp.productselection.R;
import com.philips.cdp.productselection.fragments.listfragment.ProductSelectionListingFragment;
import com.philips.cdp.productselection.fragments.welcomefragment.WelcomeScreenFragmentSelection;
import com.philips.cdp.productselection.utils.Constants;
import com.philips.platform.uid.thememanager.UIDHelper;


public class ProductSelectionActivity extends ProductSelectionBaseActivity {
    private static final String TAG = ProductSelectionActivity.class.getSimpleName();
    private static final String USER_SELECTED_PRODUCT_CTN = "mCtnFromPreference";
    private static final String USER_PREFERENCE = "user_product";
    private static int mEnterAnimation = -1;
    // private static boolean isFirstTimeWelcomeScreenlaunch = true;
    private static int mExitAnimation = -1;
    private SharedPreferences prefs = null;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // if app killed by vm.
            savedInstanceState = null;
            finish();
            super.onCreate(savedInstanceState);
            return;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productselection_layout);
        animateThisScreen();
        if (getCtnFromPreference()) {
            showFragment(new WelcomeScreenFragmentSelection());
            // isFirstTimeWelcomeScreenlaunch = false;
        } else {
            showFragment(new ProductSelectionListingFragment());
        }
        UIDHelper.setupToolbar(this);
        getTheme().applyStyle(com.philips.cdp.uikit.R.style.Theme_Philips_BrightOrange_Gradient, true);
        toolbar = (Toolbar) findViewById(R.id.uid_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_icon);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    protected boolean getCtnFromPreference() {
        String ctn = null;
        prefs = getSharedPreferences(
                USER_PREFERENCE, Context.MODE_PRIVATE);
        ctn = prefs.getString(USER_SELECTED_PRODUCT_CTN, "");
        return !(ctn != null && ctn != "");
    }

    private void animateThisScreen() {
        Bundle bundleExtras = getIntent().getExtras();

        String startAnim = null;
        String endAnim = null;

        int startAnimation = bundleExtras.getInt(Constants.START_ANIMATION_ID);
        int endAnimation = bundleExtras.getInt(Constants.STOP_ANIMATION_ID);
        int orientation = bundleExtras.getInt(Constants.SCREEN_ORIENTATION);

        if (startAnimation == 0 && endAnimation == 0) {
            return;
        }

        startAnim = getResources().getResourceName(startAnimation);
        endAnim = getResources().getResourceName(endAnimation);

        String packageName = getPackageName();
        mEnterAnimation = getApplicationContext().getResources().getIdentifier(startAnim,
                "anim", packageName);
        mExitAnimation = getApplicationContext().getResources().getIdentifier(endAnim, "anim",
                packageName);
        setRequestedOrientation(orientation);
        overridePendingTransition(mEnterAnimation, mExitAnimation);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
