/**
 * ProdRegBaseActivity is the parent abstract class for Product Registration
 * Activity.
 *
 * @author: ritesh.jha@philips.com
 * @since: Dec 5, 2014
 * Copyright (c) 2016 Philips. All rights reserved.
 */
package com.philips.cdp.prodreg.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.launcher.PRInterface;
import com.philips.cdp.prodreg.launcher.PRLaunchInput;
import com.philips.cdp.prodreg.launcher.PRUiHelper;
import com.philips.cdp.prodreg.logging.ProdRegLogger;
import com.philips.cdp.prodreg.register.Product;
import com.philips.cdp.product_registration_lib.R;
import com.philips.cdp.registration.apptagging.AppTagging;
import com.philips.cdp.uikit.UiKitActivity;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;

import java.util.ArrayList;

public class ProdRegBaseActivity extends UiKitActivity {
    private static final String TAG = ProdRegBaseActivity.class.getSimpleName();
    private TextView mTitleTextView;
    private Handler mSiteCatListHandler = new Handler();
    private int DEFAULT_THEME = R.style.Theme_Philips_DarkBlue_WhiteBackground;

    private Runnable mPauseSiteCatalystRunnable = new Runnable() {

        @Override
        public void run() {
            AppTagging.pauseCollectingLifecycleData();
        }
    };

    private Runnable mResumeSiteCatalystRunnable = new Runnable() {

        @Override
        public void run() {
            AppTagging.collectLifecycleData(ProdRegBaseActivity.this);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUiKitThemeIfRequired();
        initCustomActionBar();
        setContentView(R.layout.prodreg_activity);
        animateThisScreen();
        if (savedInstanceState == null) {
            showFragment();
        }
    }

    private void setUiKitThemeIfRequired() {
        final Bundle extras = getIntent().getExtras();
        int theme = extras.getInt(ProdRegConstants.UI_KIT_THEME);
        if (theme <= 0)
            theme = DEFAULT_THEME;

        setTheme(theme);
    }

    @Override
    protected void onPause() {
        mSiteCatListHandler.post(mPauseSiteCatalystRunnable);
        super.onPause();
    }

    @Override
    protected void onResume() {
        mSiteCatListHandler.post(mResumeSiteCatalystRunnable);
        super.onResume();
    }

    @SuppressWarnings("noinspection unchecked")
    protected void showFragment() {
        try {
            boolean isFirstLaunch = false;
            int imageResID = 0;
            ArrayList<Product> regProdList = null;
            final Bundle extras = getIntent().getExtras();
            if (extras != null) {
                isFirstLaunch = extras.getBoolean(ProdRegConstants.PROD_REG_IS_FIRST_LAUNCH);
                regProdList = (ArrayList<Product>) extras.getSerializable(ProdRegConstants.MUL_PROD_REG_CONSTANT);
                imageResID = extras.getInt(ProdRegConstants.PROD_REG_FIRST_IMAGE_ID);
            }
            FragmentLauncher fragLauncher = new FragmentLauncher(
                    this, R.id.mainContainer, new ActionBarListener() {
                @Override
                public void updateActionBar(@StringRes final int resId, final boolean enableBack) {
                    setTitle(resId);
                }

                @Override
                public void updateActionBar(final String s, final boolean b) {

                }
            });
            fragLauncher.setCustomAnimation(0, 0);
            final PRUiHelper prUiHelper = PRUiHelper.getInstance();
            final PRLaunchInput prLaunchInput = new PRLaunchInput(regProdList, isFirstLaunch);
            prLaunchInput.setProdRegUiListener(prUiHelper.getProdRegUiListener());
            prLaunchInput.setBackgroundImageResourceId(imageResID);
            new PRInterface().launch(fragLauncher, prLaunchInput);
        } catch (IllegalStateException e) {
            ProdRegLogger.e(TAG, e.getMessage());
        }
    }

    private void animateThisScreen() {
        Bundle bundleExtras = getIntent().getExtras();
        int startAnimation = bundleExtras.getInt(ProdRegConstants.START_ANIMATION_ID);
        int endAnimation = bundleExtras.getInt(ProdRegConstants.STOP_ANIMATION_ID);
        int orientation = bundleExtras.getInt(ProdRegConstants.SCREEN_ORIENTATION);

        if (startAnimation == 0 && endAnimation == 0) {
            return;
        }

        final String startAnim = getResources().getResourceName(startAnimation);
        final String endAnim = getResources().getResourceName(endAnimation);

        String packageName = getPackageName();
        final int mEnterAnimation = getApplicationContext().getResources().getIdentifier(startAnim,
                "anim", packageName);
        final int mExitAnimation = getApplicationContext().getResources().getIdentifier(endAnim, "anim",
                packageName);
        overridePendingTransition(mEnterAnimation, mExitAnimation);
        //noinspection WrongConstant
        setRequestedOrientation(orientation);
    }

    private void initCustomActionBar() {
        ActionBar mActionBar = this.getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayShowHomeEnabled(false);
            mActionBar.setDisplayShowTitleEnabled(false);
            mActionBar.setDisplayShowCustomEnabled(true);
            ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the text view in the ActionBar !
                    ActionBar.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER);
            View mCustomView = LayoutInflater.from(this).inflate(R.layout.prodreg_home_action_bar, null); // layout which contains your button.

            mTitleTextView = (TextView) mCustomView.findViewById(R.id.text);

            final FrameLayout frameLayout = (FrameLayout) mCustomView.findViewById(R.id.UpButton);
            frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    onBackPressed();
                }
            });
            ImageView arrowImage = (ImageView) mCustomView
                    .findViewById(R.id.arrow);
            //noinspection deprecation
            arrowImage.setBackground(getResources().getDrawable(R.drawable.prodreg_left_arrow));
            mActionBar.setCustomView(mCustomView, params);
            setTitle(getString(R.string.app_name));
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFrag = fragmentManager
                .findFragmentById(R.id.mainContainer);
        if (fragmentManager.getBackStackEntryCount() == 1) {
            finish();
        } else if (currentFrag != null && currentFrag instanceof BackEventListener && !((BackEventListener) currentFrag).handleBackEvent()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle outState, final PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean("retain_state", true);
    }

    @Override
    public void setTitle(int titleId) {
        if (mTitleTextView != null)
            mTitleTextView.setText(titleId);
        else
            super.setTitle(titleId);
    }
}
