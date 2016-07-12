package com.philips.cdp.productselection.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;

import com.philips.cdp.productselection.R;
import com.philips.cdp.productselection.fragments.listfragment.ProductSelectionListingFragment;
import com.philips.cdp.productselection.fragments.welcomefragment.WelcomeScreenFragmentSelection;
import com.philips.cdp.productselection.utils.Constants;


public class ProductSelectionActivity extends ProductSelectionBaseActivity {
    private static final String TAG = ProductSelectionActivity.class.getSimpleName();
    private static final String USER_SELECTED_PRODUCT_CTN = "mCtnFromPreference";
    private static final String USER_PREFERENCE = "user_product";
    private static int mEnterAnimation = -1;
    // private static boolean isFirstTimeWelcomeScreenlaunch = true;
    private static int mExitAnimation = -1;
    private SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int alwaysFinishActivity = 0;

        if (savedInstanceState != null)
            alwaysFinishActivity = savedInstanceState.getInt("ALWAYS_FINISH_ACTIVITIES");

        setContentView(R.layout.activity_productselection_layout);

        if (alwaysFinishActivity == 0) {
            animateThisScreen();

            Configuration configuration = getResources().getConfiguration();
            if (getCtnFromPreference()) {
                showFragment(new WelcomeScreenFragmentSelection());
                // isFirstTimeWelcomeScreenlaunch = false;
            } else {
                showFragment(new ProductSelectionListingFragment());
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        int alwaysFinishActivity = Settings.System.getInt(getContentResolver(), Settings.System.ALWAYS_FINISH_ACTIVITIES, 0);
        bundle.putInt("ALWAYS_FINISH_ACTIVITIES", alwaysFinishActivity);
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
