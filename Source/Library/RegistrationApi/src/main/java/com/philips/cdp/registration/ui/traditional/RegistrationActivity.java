/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.traditional;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.apptagging.AppTagging;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URLaunchInput;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;

public class RegistrationActivity extends FragmentActivity implements OnClickListener,
        ActionBarListener {

    private boolean isAccountSettings = true;
    private TextView ivBack;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            isAccountSettings = bundle.getBoolean(RegConstants.ACCOUNT_SETTINGS, true);
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
        launchRegistrationFragment(isAccountSettings);
    }

    private void launchRegistrationFragment(boolean isAccountSettings) {


        URLaunchInput urLaunchInput = new URLaunchInput();
        urLaunchInput.setAccountSettings(isAccountSettings);
        urLaunchInput.setRegistrationFunction(RegistrationFunction.Registration);


        FragmentLauncher fragmentLauncher = new FragmentLauncher(this,R.id.fl_reg_fragment_container,this);

        URInterface.getInstance().launch(fragmentLauncher, urLaunchInput, RegistrationHelper.getInstance().getUserRegistrationEventListener());



        /*try {
            FragmentManager mFragmentManager = getSupportFragmentManager();
            RegistrationFragment registrationFragment = new RegistrationFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(RegConstants.ACCOUNT_SETTINGS, isAccountSettings);
          *//*  bundle.putSerializable(RegConstants.USER_REGISTRATION_LISTENER,
                    getIntent().getExtras().getSerializable(RegConstants.USER_REGISTRATION_LISTENER));*//*
            registrationFragment.setArguments(bundle);
           registrationFragment.setOnUpdateTitleListener(this);
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fl_reg_fragment_container, registrationFragment,
                    RegConstants.REGISTRATION_FRAGMENT_TAG);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            RLog.e(RLog.EXCEPTION,
                    "RegistrationActivity :FragmentTransaction Exception occured in addFragment  :"
                            + e.getMessage());
        }*/
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_reg_back) {
            onBackPressed();
        }
    }

/*
    @Override
    public void updateRegistrationTitle(int titleResourceID) {
        // Update title and show hamberger
        //ivBack.setVisibility(View.INVISIBLE);
        ivBack.setVisibility(View.VISIBLE);
        TextView tvTitle = ((TextView) findViewById(R.id.tv_reg_header_title));
        tvTitle.setText(getString(titleResourceID));
    }

    @Override
    public void updateRegistrationTitleWithBack(int titleResourceID) {
        // update title and show back
        ivBack.setVisibility(View.VISIBLE);
        TextView tvTitle = ((TextView) findViewById(R.id.tv_reg_header_title));
        tvTitle.setText(getString(titleResourceID));
    }

    @Override
    public void updateRegistrationTitleWithOutBack(int titleResourceID) {
        // update title and show back
        ivBack.setVisibility(View.INVISIBLE);
        TextView tvTitle = ((TextView) findViewById(R.id.tv_reg_header_title));
        tvTitle.setText(getString(titleResourceID));
    }
*/

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
    public void updateActionBar(String s, boolean b) {

    }





}
