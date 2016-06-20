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
import android.os.PersistableBundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp.prodreg.launcher.FragmentLauncher;
import com.philips.cdp.prodreg.listener.ActionbarUpdateListener;
import com.philips.cdp.prodreg.register.Product;
import com.philips.cdp.prodreg.util.ProdRegConfigManager;
import com.philips.cdp.prodreg.util.ProdRegConstants;
import com.philips.cdp.product_registration_lib.R;
import com.philips.cdp.uikit.UiKitActivity;

import java.util.ArrayList;

public class ProdRegBaseActivity extends UiKitActivity {
    private TextView mTitleTextView;

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
            fragLauncher.setRegProdList(regProdList);
            fragLauncher.setFirstLaunch(isFirstLaunch);
            ProdRegConfigManager.getInstance().invokeProductRegistration(fragLauncher);
        } catch (IllegalStateException e) {
            e.printStackTrace();
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
        setRequestedOrientation(orientation);
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
        } else if (!ProdRegConfigManager.getInstance().onBackPressed(this)) {
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
