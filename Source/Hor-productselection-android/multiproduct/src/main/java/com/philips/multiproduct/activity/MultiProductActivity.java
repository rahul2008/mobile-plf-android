package com.philips.multiproduct.activity;

import android.os.Bundle;

import com.philips.multiproduct.R;
import com.philips.multiproduct.utils.Constants;


public class MultiProductActivity extends MultiProductBaseActivity {
    private static final String TAG = MultiProductActivity.class.getSimpleName();
    private static int mEnterAnimation = -1;
    private static int mExitAnimation = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digi_care);

        animateThisScreen();
        showFragment(new SupportHomeFragment());
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

}
