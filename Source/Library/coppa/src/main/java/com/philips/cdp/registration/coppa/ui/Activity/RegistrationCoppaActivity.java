/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 *
 */

package com.philips.cdp.registration.coppa.ui.Activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.philips.cdp.registration.apptagging.AppTagging;
import com.philips.cdp.registration.coppa.R;
import com.philips.cdp.registration.coppa.ui.fragment.RegistrationCoppaFragment;
import com.philips.cdp.registration.coppa.utils.CoppaConstants;
import com.philips.cdp.registration.coppa.utils.RegistrationCoppaLaunchHelper;
import com.philips.cdp.registration.listener.RegistrationTitleBarListener;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;

/**
 * Registration Coppa Actitivty handle back navigation and loading all Fragments
 */


public class RegistrationCoppaActivity extends FragmentActivity implements OnClickListener,
        RegistrationTitleBarListener {

    private boolean isAccountSettings = true;
    private boolean isParentalConsent;
    private TextView ivBack;
    final private Handler mSiteCatalistHandler = new Handler();
    final private Runnable mPauseSiteCatalystRunnable = new Runnable() {

        @Override
        public void run() {
            AppTagging.pauseCollectingLifecycleData();
        }
    };

    final private Runnable mResumeSiteCatalystRunnable = new Runnable() {

        @Override
        public void run() {
            AppTagging.collectLifecycleData(RegistrationCoppaActivity.this);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isAccountSettings = bundle.getBoolean(RegConstants.ACCOUNT_SETTINGS, true);
            isParentalConsent = bundle.getBoolean(CoppaConstants.LAUNCH_PARENTAL_FRAGMENT, false);
            final int orientation = bundle.getInt(RegConstants.ORIENTAION, -1);
            if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }
        }

        int alwaysFinishActivity = 0;
        try {
            alwaysFinishActivity = savedInstanceState.getInt("ALWAYS_FINISH_ACTIVITIES");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.reg_activity_coppa_registration);
        ivBack = (TextView) findViewById(R.id.iv_reg_back);
        ivBack.setOnClickListener(this);

        if (alwaysFinishActivity == 0) {
            initUi();
        }
        RLog.i(RLog.EVENT_LISTENERS, "RegistrationCoppaActivity  Register: NetworStateListener");
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        RLog.i("Exception ", " RegistrationActivity protected onSaveInstanceState");
        final int alwaysFinishActivity = Settings.System.getInt(getContentResolver(),
                Settings.System.ALWAYS_FINISH_ACTIVITIES, 0);
        bundle.putInt("ALWAYS_FINISH_ACTIVITIES", alwaysFinishActivity);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        final int alwaysFinishActivity = Settings.System.getInt(getContentResolver(),
                Settings.System.ALWAYS_FINISH_ACTIVITIES, 0);
        savedInstanceState.putInt("ALWAYS_FINISH_ACTIVITIES", alwaysFinishActivity);
    }

    @Override
    protected void onStart() {
        RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationCoppaActivity : onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationCoppaActivity : onResume");
        mSiteCatalistHandler.removeCallbacksAndMessages(null);
        mSiteCatalistHandler.post(mResumeSiteCatalystRunnable);
        super.onResume();
    }

    @Override
    protected void onPause() {
        RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationCoppaActivity : onPause");
        mSiteCatalistHandler.removeCallbacksAndMessages(null);
        mSiteCatalistHandler.post(mPauseSiteCatalystRunnable);
        super.onPause();
    }

    @Override
    protected void onStop() {
        RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationCoppaActivity : onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationCoppaActivity : onDestroy");
        RLog.i(RLog.EVENT_LISTENERS, "RegistrationCoppaActivity Unregister:" +
                " NetworStateListener,Context");
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        if (!RegistrationCoppaLaunchHelper.isBackEventConsumedByRegistration(this)) {
            // not consumed vertical code goes here // actual code
            super.onBackPressed();
        }
    }

    private void initUi() {
        launchRegistrationFragment(isAccountSettings, isParentalConsent);
    }

    private void launchRegistrationFragment(boolean isAccountSettings, boolean isParentalConsent) {
        try {
            final FragmentManager mFragmentManager = getSupportFragmentManager();
            final RegistrationCoppaFragment registrationFragment = new RegistrationCoppaFragment();
            final Bundle bundle = new Bundle();
            bundle.putBoolean(RegConstants.ACCOUNT_SETTINGS, isAccountSettings);
            bundle.putBoolean(CoppaConstants.LAUNCH_PARENTAL_FRAGMENT, isParentalConsent);
            registrationFragment.setArguments(bundle);
            registrationFragment.setOnUpdateTitleListener(this);
            final FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fl_reg_fragment_container, registrationFragment,
                    RegConstants.REGISTRATION_COPPA_FRAGMENT_TAG);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            RLog.e(RLog.EXCEPTION,
                    "RegistrationCoppaActivity :FragmentTransaction Exception occured in " +
                            "addFragment  :"
                            + e.getMessage());
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_reg_back) {
            onBackPressed();
        }
    }

    @Override
    public void updateRegistrationTitle(int titleResourceId) {
        // Update title and show hamberger
        //ivBack.setVisibility(View.INVISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        final TextView tvTitle = ((TextView) findViewById(R.id.tv_reg_header_title));
        if(titleResourceId > 0){
            tvTitle.setText(getString(titleResourceId));
        }
    }

    @Override
    public void updateRegistrationTitleWithBack(int titleResourceId) {
        // update title and show back
        ivBack.setVisibility(View.VISIBLE);
        final TextView tvTitle = ((TextView) findViewById(R.id.tv_reg_header_title));
        if(titleResourceId > 0){
            tvTitle.setText(getString(titleResourceId));
        }
    }

    @Override
    public void updateRegistrationTitleWithOutBack(int titleResourceId) {
        // update title and show back
        //ivBack.setVisibility(View.INVISIBLE);
        final TextView tvTitle = ((TextView) findViewById(R.id.tv_reg_header_title));
        if(titleResourceId > 0){
           tvTitle.setText(getString(titleResourceId));
        }
    }
}
