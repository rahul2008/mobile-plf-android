/**
 * DigitalCareBaseActivity is the parent abstract class for DigitalCare
 * Activity.
 *
 * @author: ritesh.jha@philips.com
 * @since: Dec 5, 2014
 * Copyright (c) 2016 Philips. All rights reserved.
 */
package com.philips.cdp.digitalcare.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.customview.DigitalCareFontTextView;
import com.philips.cdp.digitalcare.util.DigiCareLogger;
import com.philips.cdp.digitalcare.util.DigitalCareConstants;


public abstract class DigitalCareBaseActivity extends FragmentActivity {
    private static String TAG = DigitalCareBaseActivity.class.getSimpleName();

    protected RelativeLayout mActionbarlayout = null;
    protected ImageView mActionBarMenuIcon = null;
    protected ImageView mActionBarArrow = null;
    protected DigitalCareFontTextView mActionBarTitle = null;
    protected FragmentManager fragmentManager = null;
    protected DigitalCareConfigManager mDigitalCareConfigManager = null;
    protected OnClickListener actionBarClickListener = new OnClickListener() {

        @Override
        public void onClick(View view) {
            int _id = view.getId();
            if (_id == R.id.action_bar_icon_parent) {
                if (mActionBarMenuIcon.getVisibility() == View.VISIBLE)
                    finish();
                else if (mActionBarArrow.getVisibility() == View.VISIBLE)
                    backstackFragment();
            } else if (_id == R.id.home_icon) {
                finish();
            } else if (_id == R.id.back_to_home_img)
                backstackFragment();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
     /*   DigiCareLogger.i(TAG, "onCreate");*/
        DigitalCareConfigManager.getInstance();
        fragmentManager = getSupportFragmentManager();
    }

    protected void initActionBar() throws ClassCastException {
        mActionbarlayout = (RelativeLayout) findViewById(R.id.action_bar_icon_parent);
        mActionBarMenuIcon = (ImageView) findViewById(R.id.home_icon);
        mActionBarArrow = (ImageView) findViewById(R.id.back_to_home_img);
        mActionBarTitle = (DigitalCareFontTextView) findViewById(R.id.action_bar_title);

        mActionBarMenuIcon.setOnClickListener(actionBarClickListener);
        mActionBarArrow.setOnClickListener(actionBarClickListener);
        mActionbarlayout.setOnClickListener(actionBarClickListener);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
       /* DigiCareLogger.i(TAG, TAG + " : onConfigurationChanged ");*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        //AnalyticsTracker.startCollectLifecycleData();
        DigitalCareConfigManager.getInstance().getTaggingInterface().collectLifecycleInfo(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //AnalyticsTracker.stopCollectLifecycleData();
        DigitalCareConfigManager.getInstance().getTaggingInterface().pauseLifecycleInfo();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return backstackFragment();
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        if (mDigitalCareConfigManager != null) {
            mDigitalCareConfigManager = null;
        }
    }

    private boolean backstackFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
//            enableActionBarHome();
            fragmentManager.popBackStack();
            removeCurrentFragment();
        }
        return true;
    }

    private void removeCurrentFragment() {
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Fragment currentFrag = fragmentManager
                .findFragmentById(R.id.mainContainer);

        if (currentFrag != null) {
            transaction.remove(currentFrag);
        }
        transaction.commit();
    }

    private void enableActionBarLeftArrow() {
        mActionBarMenuIcon.setVisibility(View.GONE);
        mActionBarArrow.setVisibility(View.VISIBLE);
        mActionBarArrow.bringToFront();
    }

    protected void enableActionBarHome() {
        mActionBarMenuIcon.setVisibility(View.VISIBLE);
        mActionBarMenuIcon.bringToFront();
        mActionBarArrow.setVisibility(View.GONE);
        mActionBarTitle.setText(getResources().getString(
                R.string.actionbar_title_support));
    }

    protected void showFragment(Fragment fragment) {
        try {
            enableActionBarLeftArrow();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();

            fragmentTransaction.replace(R.id.mainContainer, fragment, DigitalCareConstants.DIGITALCARE_FRAGMENT_TAG);
            fragmentTransaction.addToBackStack(fragment.getTag());
            fragmentTransaction.commit();
        } catch (IllegalStateException e) {
            DigiCareLogger.e(TAG, e.getMessage());
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getWindow() != null && getWindow().getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
                    .getWindowToken(), 0);
        }
    }

}
