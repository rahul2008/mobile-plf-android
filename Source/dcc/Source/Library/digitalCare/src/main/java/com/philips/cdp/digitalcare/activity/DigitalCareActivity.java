/**
 * DigitalCareActivity is the main container class for Digital Care fragments.
 * Also responsible for fetching Product images, Facebook authentication & also
 * Twitter authentication.
 *
 * @author : Ritesh.jha@philips.com
 * @since : 5 Dec 2014
 * <p/>
 * Copyright (c) 2016 Philips. All rights reserved.
 */

package com.philips.cdp.digitalcare.activity;

import android.content.Context;
import android.os.Bundle;

import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.homefragment.SupportHomeFragment;
import com.philips.cdp.digitalcare.util.DigitalCareConstants;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.shamanland.fonticon.FontIconTypefaceHolder;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

/**
 * The Acitivity Class used while the component used as Activity Invoking.
 */
public class DigitalCareActivity extends DigitalCareBaseActivity {
    private static final String TAG = DigitalCareActivity.class.getSimpleName();
    private static int mEnterAnimation = -1;
    private static int mExitAnimation = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getDlSThemeLauncher();
        UIDHelper.injectCalligraphyFonts();
        if(savedInstanceState!=null){
            // if app killed by vm.
            savedInstanceState =null;
            finish();
            super.onCreate(savedInstanceState);
            return;
        }
        super.onCreate(savedInstanceState);

        setContentView(R.layout.consumercare_activity_digi_care);
        initActionBar();
        animateThisScreen();
        showFragment(new SupportHomeFragment());
    }

    public void animateThisScreen() {
        Bundle bundleExtras = getIntent().getExtras();

        String startAnim = null;
        String endAnim = null;

        int startAnimation = getAnInt(bundleExtras, DigitalCareConstants.START_ANIMATION_ID);
        int endAnimation = getAnInt(bundleExtras, DigitalCareConstants.STOP_ANIMATION_ID);
        int orientation = getAnInt(bundleExtras, DigitalCareConstants.SCREEN_ORIENTATION);

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

    private void getDlSThemeLauncher() {
        ThemeConfiguration config = DigitalCareConfigManager.getInstance().getThemeConfiguration();
        setTheme(DigitalCareConfigManager.getInstance().getDlsTheme());
        UIDHelper.init(config);
        FontIconTypefaceHolder.init(getAssets(), "fonts/iconfont.ttf");
    }

    private int getAnInt(Bundle bundleExtras, String startAnimationId) {
        return bundleExtras.getInt(startAnimationId);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
}
