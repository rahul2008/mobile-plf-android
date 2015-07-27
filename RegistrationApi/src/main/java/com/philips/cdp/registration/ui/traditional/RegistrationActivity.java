
package com.philips.cdp.registration.ui.traditional;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.adobe.mobile.Config;
import com.philips.cdp.registration.AppTagging.AppTagging;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.listener.RegistrationTitleBarListener;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;

public class RegistrationActivity extends FragmentActivity implements OnClickListener,
        RegistrationTitleBarListener {

    private ImageView ivBack;
    private Handler mSiteCatalistHandler = new Handler();

    private Runnable mPauseSiteCatalystRunnable = new Runnable() {

        @Override
        public void run() {
            Config.pauseCollectingLifecycleData();
        }
    };

    private Runnable mResumeSiteCatalystRunnable = new Runnable() {

        @Override
        public void run() {
            Config.collectLifecycleData();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int orientation = bundle.getInt(RegConstants.ORIENTAION);
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

        setContentView(R.layout.activity_registration);
        RLog.i(RLog.EVENT_LISTENERS, "RegistrationActivity  Register: NetworStateListener");
        initUI();
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
        RLog.i(RLog.EVENT_LISTENERS, "RegistrationActivity Unregister: NetworStateListener,Context");
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationActivity : onBackPressed");
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager
                .findFragmentByTag(RegConstants.REGISTRATION_FRAGMENT_TAG);
        if (fragment != null) {
            if (((RegistrationFragment) fragment).onBackPressed()) {
                // not consumed vertical code goes here // actual code
                super.onBackPressed();
            }
        }
    }

    private void initUI() {
        ivBack = (ImageView) findViewById(R.id.iv_reg_back);
        ivBack.setOnClickListener(this);
        launchRegistrationFragment(R.id.fl_reg_fragment_container, this);
    }

    /**
     * Launch registration fragment
     */
    private void launchRegistrationFragment(int container, FragmentActivity fragmentActivity) {
        try {
            FragmentManager mFragmentManager = fragmentActivity.getSupportFragmentManager();
            RegistrationFragment registrationFragment = new RegistrationFragment();
            registrationFragment.setOnUpdateTitleListener(this);
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(container, registrationFragment,
                    RegConstants.REGISTRATION_FRAGMENT_TAG);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            RLog.e(RLog.EXCEPTION,
                    "RegistrationActivity :FragmentTransaction Exception occured in addFragment  :"
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
    public void updateRegistrationTitle(int titleResourceID) {
        // Update title and show hamberger
        //ivBack.setVisibility(View.INVISIBLE);
        TextView tvTitle = ((TextView) findViewById(R.id.tv_reg_header_title));
        tvTitle.setText(getString(titleResourceID));
    }

    @Override
    public void updateRegistrationTitleWithBack(int titleResourceID) {
        // update title and show back
        //ivBack.setVisibility(View.VISIBLE);
        TextView tvTitle = ((TextView) findViewById(R.id.tv_reg_header_title));
        tvTitle.setText(getString(titleResourceID));
    }

}
