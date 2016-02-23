package com.philips.multiproduct.base;

import android.os.Bundle;

import com.philips.multiproduct.ProductModelSelectionHelper;
import com.philips.multiproduct.R;
import com.philips.multiproduct.listfragment.ProductSelectionListingFragment;
import com.philips.multiproduct.listfragment.ProductSelectionListingTabletFragment;
import com.philips.multiproduct.utils.Constants;
import com.philips.multiproduct.utils.ProductSelectionLogger;
import com.philips.multiproduct.welcomefragment.WelcomeScreenFragmentSelection;


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
