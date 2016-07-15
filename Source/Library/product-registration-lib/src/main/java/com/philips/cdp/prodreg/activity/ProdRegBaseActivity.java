/**
 * ProdRegBaseActivity is the parent abstract class for Product Registration
 * Activity.
 *
 * @author: ritesh.jha@philips.com
 * @since: Dec 5, 2014
 * Copyright (c) 2016 Philips. All rights reserved.
 */
package com.philips.cdp.prodreg.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.launcher.FragmentLauncher;
import com.philips.cdp.prodreg.launcher.ProdRegUiHelper;
import com.philips.cdp.prodreg.listener.ActionbarUpdateListener;
import com.philips.cdp.prodreg.listener.ProdRegUiListener;
import com.philips.cdp.prodreg.logging.ProdRegLogger;
import com.philips.cdp.prodreg.register.Product;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.prodreg.register.UserWithProducts;
import com.philips.cdp.product_registration_lib.R;
import com.philips.cdp.tagging.Tagging;
import com.philips.cdp.uikit.UiKitActivity;

import java.util.ArrayList;

public class ProdRegBaseActivity extends UiKitActivity {
    private static final String TAG = ProdRegBaseActivity.class.getSimpleName();
    private TextView mTitleTextView;
    private Handler mSiteCatListHandler = new Handler();

    private Runnable mPauseSiteCatalystRunnable = new Runnable() {

        @Override
        public void run() {
            Tagging.pauseCollectingLifecycleData();
        }
    };

    private Runnable mResumeSiteCatalystRunnable = new Runnable() {

        @Override
        public void run() {
            Tagging.collectLifecycleData();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initCustomActionBar();
        setContentView(R.layout.prodreg_activity);
        animateThisScreen();
        if (savedInstanceState == null) {
            showFragment();
        }
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    protected void showFragment() {
        try {
            boolean isFirstLaunch = false;
            ArrayList<Product> regProdList = null;
            final Bundle extras = getIntent().getExtras();
            if (extras != null) {
                isFirstLaunch = extras.getBoolean(ProdRegConstants.PROD_REG_IS_FIRST_LAUNCH);
                regProdList = (ArrayList<Product>) extras.getSerializable(ProdRegConstants.MUL_PROD_REG_CONSTANT);
            }
            FragmentLauncher fragLauncher = new FragmentLauncher(
                    this, R.id.mainContainer, new ActionbarUpdateListener() {
                @Override
                public void updateActionbar(final String var1) {
                    setTitle(var1);
                }
            });
            fragLauncher.setAnimation(0, 0);
            fragLauncher.setFirstLaunch(isFirstLaunch);
            ProdRegUiHelper.getInstance().invokeProductRegistration(fragLauncher, regProdList, new ProdRegUiListener() {
                @Override
                public void onProdRegContinue(final RegisteredProduct registeredProduct, final UserWithProducts userWithProduct) {
                }

                @Override
                public void onProdRegBack(final RegisteredProduct registeredProduct, final UserWithProducts userWithProduct) {

                }
            });
        } catch (IllegalStateException e) {
            ProdRegLogger.e(TAG, e.getMessage());
        }
    }

    private void animateThisScreen() {
        Bundle bundleExtras = getIntent().getExtras();
        int startAnimation = bundleExtras.getInt(ProdRegConstants.START_ANIMATION_ID);
        int endAnimation = bundleExtras.getInt(ProdRegConstants.STOP_ANIMATION_ID);

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
    }

    private void initCustomActionBar() {
        ActionBar mActionBar = this.getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);

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
        arrowImage.setBackground(getResources().getDrawable(R.drawable.prodreg_left_arrow));
        mActionBar.setCustomView(mCustomView, params);
        setTitle(getString(R.string.app_name));
        mActionBar.setDisplayShowCustomEnabled(true);
    }

    @Override
    public void onBackPressed() {

        final FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() == 1) {
            finish();
        } else if (!ProdRegUiHelper.getInstance().onBackPressed(this)) {
            super.onBackPressed();
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle outState, final PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putBoolean("test", true);
    }

    @Override
    public void setTitle(final CharSequence title) {
        super.setTitle(title);
        mTitleTextView.setText(title);
    }
}
