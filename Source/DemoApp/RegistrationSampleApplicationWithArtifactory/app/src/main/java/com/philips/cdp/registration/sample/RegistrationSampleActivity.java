package com.philips.cdp.registration.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.philips.cdp.registration.apptagging.AppTagging;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URLaunchInput;
import com.philips.platform.uappframework.launcher.ActivityLauncher;

public class RegistrationSampleActivity extends Activity implements OnClickListener, UserRegistrationUIEventListener,UserRegistrationListener {

    private Button mBtnRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationSampleActivity : onCreate");
        RLog.i(RLog.EVENT_LISTENERS, "RegistrationSampleActivity register: UserRegistrationListener");
        setContentView(R.layout.activity_registration_sample);
        RegistrationHelper.getInstance().registerUserRegistrationListener(this);
        mBtnRegistration = (Button) findViewById(R.id.btn_registration);
        mBtnRegistration.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationSampleActivity : onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        AppTagging.collectLifecycleData(this);
        RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationSampleActivity : onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        AppTagging.pauseCollectingLifecycleData();
        RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationSampleActivity : onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationSampleActivity : onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        RegistrationHelper.getInstance().unRegisterUserRegistrationListener(this);
        RLog.d(RLog.EVENT_LISTENERS, "RegistrationSampleActivity unregister : RegisterUserRegistrationListener");
        super.onDestroy();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_registration:
                RLog.d(RLog.ONCLICK, "RegistrationSampleActivity : Registration");
                URLaunchInput urLaunchInput = new URLaunchInput();
                urLaunchInput.setAccountSettings(true);
                urLaunchInput.setRegistrationFunction(RegistrationFunction.Registration);
                urLaunchInput.setUserRegistrationUIEventListener(this);
                ActivityLauncher activityLauncher = new ActivityLauncher(ActivityLauncher.
                        ActivityOrientation.SCREEN_ORIENTATION_SENSOR, 0);

                URInterface urInterface = new URInterface();
                urInterface.launch(activityLauncher, urLaunchInput);

                break;

            default:
                break;
        }

    }

    @Override
    public void onUserRegistrationComplete(Activity activity) {
        RLog.d(RLog.EVENT_LISTENERS, "RegistrationSampleActivity : onUserRegistrationComplete");
    }

    @Override
    public void onPrivacyPolicyClick(Activity activity) {
        RLog.d(RLog.EVENT_LISTENERS, "RegistrationSampleActivity : onPrivacyPolicyClick");
    }

    @Override
    public void onTermsAndConditionClick(Activity activity) {
        RLog.d(RLog.EVENT_LISTENERS, "RegistrationSampleActivity : onTermsAndConditionClick");
    }

    @Override
    public void onUserLogoutSuccess() {
        RLog.d(RLog.EVENT_LISTENERS, "RegistrationSampleActivity : onUserLogoutSuccess");
    }

    @Override
    public void onUserLogoutFailure() {
        RLog.d(RLog.EVENT_LISTENERS, "RegistrationSampleActivity : onUserLogoutFailure");
    }

    @Override
    public void onUserLogoutSuccessWithInvalidAccessToken() {
        RLog.d(RLog.EVENT_LISTENERS, "RegistrationSampleActivity : onUserLogoutSuccessWithInvalidAccessToken");
    }
}
