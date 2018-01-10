/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 *
 */

package com.philips.cdp.registration.coppa.ui.activity;

import android.content.pm.*;
import android.os.*;
import android.provider.*;
import android.support.v4.app.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import com.philips.cdp.registration.app.tagging.*;
import com.philips.cdp.registration.configuration.*;
import com.philips.cdp.registration.coppa.*;
import com.philips.cdp.registration.coppa.utils.*;
import com.philips.cdp.registration.listener.*;
import com.philips.cdp.registration.settings.*;
import com.philips.cdp.registration.ui.utils.*;
import com.philips.platform.uappframework.launcher.*;
import com.philips.platform.uappframework.listener.*;
import com.philips.platform.uid.thememanager.*;
import com.philips.platform.uid.utils.*;

/**
 * Registration Coppa Actitivty handle back navigation and loading all Fragments
 */
public class RegistrationCoppaActivity extends UIDActivity implements OnClickListener,
        ActionBarListener {

    private UIFlow uiFlow;

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
    private boolean isParentalConsent;
    private TextView ivBack;
    private RegistrationLaunchMode mRegistrationLaunchMode = RegistrationLaunchMode.DEFAULT;

    RegistrationContentConfiguration registrationContentConfiguration ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(RegistrationHelper.getInstance().getThemeConfiguration() != null) {
            UIDHelper.init(RegistrationHelper.getInstance().getThemeConfiguration());
        }
        if(RegistrationHelper.getInstance().getTheme() != 0) {
            setTheme(RegistrationHelper.getInstance().getTheme());
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mRegistrationLaunchMode = (RegistrationLaunchMode) bundle.get(RegConstants.REGISTRATION_LAUNCH_MODE);
            registrationContentConfiguration = (RegistrationContentConfiguration) bundle.get(RegConstants.REGISTRATION_CONTENT_CONFIG);
            isParentalConsent = bundle.getBoolean(CoppaConstants.LAUNCH_PARENTAL_FRAGMENT, false);
            uiFlow = (UIFlow) bundle.get(RegConstants.REGISTRATION_UI_FLOW);

            final int sOrientation = bundle.getInt(RegConstants.ORIENTAION, -1);
            setOrientation(sOrientation);
        }

        int alwaysFinishActivity = 0;
        if (savedInstanceState != null)
            alwaysFinishActivity = savedInstanceState.getInt("ALWAYS_FINISH_ACTIVITIES");

        setContentView(R.layout.reg_activity_coppa_registration);
        ivBack = (TextView) findViewById(R.id.iv_reg_back);
        ivBack.setOnClickListener(this);

        if (alwaysFinishActivity == 0) {
            initUi();
        }
        RLog.i(RLog.EVENT_LISTENERS, "RegistrationCoppaActivity  Register: NetworStateListener");
    }

    private void setOrientation(int sOrientation) {
        if (sOrientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else if (sOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else if (sOrientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        } else if (sOrientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        RLog.i("Exception ", " RegistrationActivity protected onSaveInstanceState");
        @SuppressWarnings("deprecation") final int alwaysFinishActivity = Settings.System.
                getInt(getContentResolver(),
                        Settings.System.ALWAYS_FINISH_ACTIVITIES, 0);
        bundle.putInt("ALWAYS_FINISH_ACTIVITIES", alwaysFinishActivity);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        @SuppressWarnings("deprecation") final int alwaysFinishActivity = Settings.System.
                getInt(getContentResolver(),
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
        RLog.d(RLog.EVENT_LISTENERS, "RegistrationCoppaActivity Unregister:" +
                " NetworStateListener,Context");
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {


        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager
                .findFragmentById(R.id.fl_reg_fragment_container);
        if (fragment != null && fragment instanceof BackEventListener) {
            boolean isConsumed = ((BackEventListener) fragment).handleBackEvent();
            if (isConsumed)
                return;

            super.onBackPressed();
        }
    }

    private void initUi() {
        launchRegistrationFragment(mRegistrationLaunchMode, isParentalConsent);
    }

    private void launchRegistrationFragment(RegistrationLaunchMode launchMode, boolean isParentalConsent) {
        CoppaLaunchInput urLaunchInput;
        urLaunchInput = new CoppaLaunchInput();
        urLaunchInput.setEndPointScreen(launchMode);
        urLaunchInput.setParentalFragment(isParentalConsent);
        urLaunchInput.setRegistrationContentConfiguration(registrationContentConfiguration);
        urLaunchInput.setRegistrationFunction(RegistrationFunction.Registration);
        urLaunchInput.setUIFlow(uiFlow);
        urLaunchInput.setUserRegistrationUIEventListener(RegistrationConfiguration.getInstance().
                getUserRegistrationUIEventListener());
        FragmentLauncher fragmentLauncher = new FragmentLauncher
                (this, R.id.fl_reg_fragment_container,this);
        CoppaInterface urInterface = new CoppaInterface();
        urInterface.launch(fragmentLauncher, urLaunchInput);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_reg_back) {
            onBackPressed();
        }
    }

   /* @Override
    public void updateRegistrationTitle(int titleResourceId) {
        // Update title and show hamberger
        //ivBack.setVisibility(View.INVISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        if (titleResourceId > 0) {
            final TextView tvTitle = ((TextView) findViewById(R.id.tv_reg_header_title));
            tvTitle.setText(getString(titleResourceId));
        }
    }

    @Override
    public void updateRegistrationTitleWithBack(int titleResourceId) {
        // update title and show back
        ivBack.setVisibility(View.VISIBLE);
        if (titleResourceId > 0) {
            final TextView tvTitle = ((TextView) findViewById(R.id.tv_reg_header_title));
            tvTitle.setText(getString(titleResourceId));
        }
    }

    @Override
    public void updateRegistrationTitleWithOutBack(int titleResourceId) {
        // update title and show back
        //ivBack.setVisibility(View.INVISIBLE);
        if (titleResourceId > 0) {
            final TextView tvTitle = ((TextView) findViewById(R.id.tv_reg_header_title));
            tvTitle.setText(getString(titleResourceId));
        }
    }*/

    @Override
    public void updateActionBar(int titleResourceID, boolean isShowBack) {
        if(isShowBack){
            ivBack.setVisibility(View.VISIBLE);
            if (titleResourceID > 0) {
                final TextView tvTitle = ((TextView) findViewById(R.id.tv_reg_header_title));
                tvTitle.setText(getString(titleResourceID));
            }
        }else{
            ivBack.setVisibility(View.VISIBLE);
            if (titleResourceID > 0) {
                final TextView tvTitle = ((TextView) findViewById(R.id.tv_reg_header_title));
                tvTitle.setText(getString(titleResourceID));
            }
        }
    }

    @Override
    public void updateActionBar(String s, boolean b) {

    }
}
