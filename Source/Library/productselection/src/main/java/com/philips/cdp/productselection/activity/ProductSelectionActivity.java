package com.philips.cdp.productselection.activity;

import android.os.Bundle;

import com.philips.cdp.productselection.ProductModelSelectionHelper;
import com.philips.cdp.productselection.fragments.listfragment.ProductSelectionListingFragment;
import com.philips.cdp.productselection.fragments.listfragment.ProductSelectionListingTabletFragment;
import com.philips.cdp.productselection.utils.ProductSelectionLogger;
import com.philips.cdp.productselection.fragments.welcomefragment.WelcomeScreenFragmentSelection;
import com.philips.cdp.productselection.R;
import com.philips.cdp.productselection.utils.Constants;


public class ProductSelectionActivity extends ProductSelectionBaseActivity {
    private static final String TAG = ProductSelectionActivity.class.getSimpleName();
    private static int mEnterAnimation = -1;
    private static int mExitAnimation = -1;
    private static boolean isFirstTimeWelcomeScreenlaunch = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productselection_layout);

        animateThisScreen();
        int ctnSize = ProductModelSelectionHelper.getInstance().getProductCtnList().length;
        ProductSelectionLogger.d(TAG, "Size of the Ctn is : " + ctnSize);
        if (isFirstTimeWelcomeScreenlaunch) {
            showFragment(new WelcomeScreenFragmentSelection());
            isFirstTimeWelcomeScreenlaunch = false;
        } else
            if(isTablet()) {
                showFragment(new ProductSelectionListingTabletFragment());
            }
        else{
                showFragment(new ProductSelectionListingFragment());
            }
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
