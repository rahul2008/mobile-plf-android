/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.traditional;

import android.content.pm.ActivityInfo;
import android.os.*;
import android.provider.Settings;
import android.support.v4.app.*;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.app.tagging.AppTagging;
import com.philips.cdp.registration.configuration.RegistrationLaunchMode;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.ui.utils.*;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.*;
import com.philips.platform.uid.thememanager.*;

public class RegistrationActivity extends FragmentActivity implements OnClickListener,
        ActionBarListener {

    private TextView ivBack;
    private RegistrationLaunchMode mRegistrationLaunchMode = RegistrationLaunchMode.DEFAULT;
    private RegistrationContentConfiguration registrationContentConfiguration;

    private UIFlow uiFlow;
    private Handler mSiteCatalistHandler = new Handler();
    private Runnable mPauseSiteCatalystRunnable = new Runnable() {

        @Override
        public void run() {
            AppTagging.pauseCollectingLifecycleData();
        }
    };

    private Runnable mResumeSiteCatalystRunnable = new Runnable() {

        @Override
        public void run() {
            AppTagging.collectLifecycleData(RegistrationActivity.this);
        }
    };

    public static UserRegistrationUIEventListener getUserRegistrationUIEventListener() {
        return userRegistrationUIEventListener;
    }

    public static void setUserRegistrationUIEventListener(
            UserRegistrationUIEventListener userRegistrationUIEventListener) {
        RegistrationActivity.userRegistrationUIEventListener = userRegistrationUIEventListener;
    }

    private static UserRegistrationUIEventListener userRegistrationUIEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UIDHelper.init(new ThemeConfiguration(this, ContentColor.ULTRA_LIGHT, NavigationColor.ULTRA_LIGHT, AccentRange.AQUA));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            mRegistrationLaunchMode = (RegistrationLaunchMode) bundle.get(RegConstants.REGISTRATION_LAUNCH_MODE);
            int orientation = bundle.getInt(RegConstants.ORIENTAION, -1);
            if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            } else if (orientation == ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            }
            setRegistrationContentConfiguration((RegistrationContentConfiguration) bundle.get(RegConstants.REGISTRATION_CONTENT_CONFIG));

            uiFlow = (UIFlow) bundle.get(RegConstants.REGISTRATION_UI_FLOW);

        }

        int alwaysFinishActivity = 0;
        try {
            alwaysFinishActivity = savedInstanceState.getInt("ALWAYS_FINISH_ACTIVITIES");
        } catch (NullPointerException e) {
        }

        setContentView(R.layout.reg_activity_registration);
        ivBack = (TextView) findViewById(R.id.iv_reg_back);
        ivBack.setOnClickListener(this);

        if (alwaysFinishActivity == 0) {
            initUI();
        }

        RLog.i(RLog.EVENT_LISTENERS, "RegistrationActivity  Register: NetworStateListener");
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        RLog.i("Exception ", " RegistrationActivity protected onSaveInstanceState");
        @SuppressWarnings("deprecation") int alwaysFinishActivity = Settings.System.
                getInt(getContentResolver(),
                        Settings.System.ALWAYS_FINISH_ACTIVITIES, 0);
        bundle.putInt("ALWAYS_FINISH_ACTIVITIES", alwaysFinishActivity);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        @SuppressWarnings("deprecation") int alwaysFinishActivity = Settings.System.
                getInt(getContentResolver(),
                        Settings.System.ALWAYS_FINISH_ACTIVITIES, 0);
        savedInstanceState.putInt("ALWAYS_FINISH_ACTIVITIES", alwaysFinishActivity);
    }

    @Override
    protected void onStart() {
        RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationActivity : onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationActivity : onResume");
        mSiteCatalistHandler.removeCallbacksAndMessages(null);
        mSiteCatalistHandler.post(mResumeSiteCatalystRunnable);
        super.onResume();
    }

    @Override
    protected void onPause() {
        RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationActivity : onPause");
        mSiteCatalistHandler.removeCallbacksAndMessages(null);
        mSiteCatalistHandler.post(mPauseSiteCatalystRunnable);
        super.onPause();
    }

    @Override
    protected void onStop() {
        RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationActivity : onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationActivity : onDestroy");
        RLog.i(RLog.EVENT_LISTENERS, "RegistrationActivity Unregister: NetworStateListener," +
                "Context");
        RegistrationActivity.setUserRegistrationUIEventListener(null);
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

    private void initUI() {
        launchRegistrationFragment(mRegistrationLaunchMode);
    }

    private void launchRegistrationFragment(RegistrationLaunchMode registrationLaunchMode) {
        URLaunchInput urLaunchInput = new URLaunchInput();
        urLaunchInput.setEndPointScreen(registrationLaunchMode);
        urLaunchInput.setRegistrationFunction(RegistrationFunction.Registration);
        urLaunchInput.setRegistrationContentConfiguration(getRegistrationContentConfiguration());
        urLaunchInput.setUserRegistrationUIEventListener(userRegistrationUIEventListener);
        urLaunchInput.setUIFlow(uiFlow);
        FragmentLauncher fragmentLauncher = new FragmentLauncher
                (this,R.id.fl_reg_fragment_container,this);
        URInterface urInterface = new URInterface();
        urInterface.launch(fragmentLauncher, urLaunchInput);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_reg_back) {
            onBackPressed();
        }
    }

    @Override
    public void updateActionBar(int titleResourceID, boolean isShowBack) {
        if(isShowBack){
            ivBack.setVisibility(View.VISIBLE);
            TextView tvTitle = ((TextView) findViewById(R.id.tv_reg_header_title));
            tvTitle.setText(getString(titleResourceID));
        }else{
            ivBack.setVisibility(View.VISIBLE);
            TextView tvTitle = ((TextView) findViewById(R.id.tv_reg_header_title));
            tvTitle.setText(getString(titleResourceID));
        }
    }

    @Override
    public void updateActionBar(String titleResourceText, boolean isShowBack) {
        TextView tvTitle = ((TextView) findViewById(R.id.tv_reg_header_title));
        tvTitle.setText(titleResourceText);
        ivBack.setVisibility(View.VISIBLE);
        if (!isShowBack) {
            ivBack.setVisibility(View.INVISIBLE);
        }
    }


    private void setRegistrationContentConfiguration(RegistrationContentConfiguration regContentConfig){
        registrationContentConfiguration = regContentConfig;
    }
    private RegistrationContentConfiguration getRegistrationContentConfiguration() {

        return registrationContentConfiguration;
    }


}
