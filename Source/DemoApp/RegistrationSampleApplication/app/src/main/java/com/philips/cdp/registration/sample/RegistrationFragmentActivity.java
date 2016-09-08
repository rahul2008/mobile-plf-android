package com.philips.cdp.registration.sample;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Window;

import com.philips.cdp.registration.apptagging.AppTagging;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URLaunchInput;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;

public class RegistrationFragmentActivity extends FragmentActivity implements ActionBarListener, UserRegistrationUIEventListener,UserRegistrationListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Log.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationSampleActivity : onCreate");
        Log.i(RLog.EVENT_LISTENERS, "RegistrationSampleActivity register: UserRegistrationListener");
        setContentView(R.layout.fragment_registration_sample);
        RegistrationHelper.getInstance().registerUserRegistrationListener(this);

        URLaunchInput urLaunchInput = new URLaunchInput();
        urLaunchInput.setAccountSettings(true);
        urLaunchInput.setRegistrationFunction(RegistrationFunction.Registration);
        urLaunchInput.setUserRegistrationUIEventListener(this);
        urLaunchInput.enableAddtoBackStack(true);

        FragmentLauncher fragmentLauncher = new FragmentLauncher
                (this,R.id.fl_reg_fragment_container,this);
        URInterface urInterface = new URInterface();
        urInterface.launch(fragmentLauncher, urLaunchInput);

    }

    @Override
    protected void onStart() {
        Log.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationSampleActivity : onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        AppTagging.collectLifecycleData(this);
        Log.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationSampleActivity : onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        AppTagging.pauseCollectingLifecycleData();
        Log.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationSampleActivity : onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationSampleActivity : onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        RegistrationHelper.getInstance().unRegisterUserRegistrationListener(this);
        Log.d(RLog.EVENT_LISTENERS, "RegistrationSampleActivity unregister : RegisterUserRegistrationListener");
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




    @Override
    public void onUserRegistrationComplete(Activity activity) {
        Log.d(RLog.EVENT_LISTENERS, "RegistrationSampleActivity : onUserRegistrationComplete");
    }

    @Override
    public void onPrivacyPolicyClick(Activity activity) {
        Log.d(RLog.EVENT_LISTENERS, "RegistrationSampleActivity : onPrivacyPolicyClick");
    }

    @Override
    public void onTermsAndConditionClick(Activity activity) {
        Log.d(RLog.EVENT_LISTENERS, "RegistrationSampleActivity : onTermsAndConditionClick");
    }

    @Override
    public void onUserLogoutSuccess() {
        Log.d(RLog.EVENT_LISTENERS, "RegistrationSampleActivity : onUserLogoutSuccess");
    }

    @Override
    public void onUserLogoutFailure() {
        Log.d(RLog.EVENT_LISTENERS, "RegistrationSampleActivity : onUserLogoutFailure");
    }

    @Override
    public void onUserLogoutSuccessWithInvalidAccessToken() {
        Log.d(RLog.EVENT_LISTENERS, "RegistrationSampleActivity : onUserLogoutSuccessWithInvalidAccessToken");
    }

    @Override
    public void updateActionBar(@StringRes int i, boolean b) {
        Log.d(RLog.EVENT_LISTENERS, "RegistrationSampleActivity : updateActionBar tile :"+getString(i)+" BAck status :"+b);


    }

    @Override
    public void updateActionBar(String s, boolean b) {
        Log.d(RLog.EVENT_LISTENERS, "RegistrationSampleActivity : updateActionBar tile :"+s+" BAck status :"+b);

    }
}
