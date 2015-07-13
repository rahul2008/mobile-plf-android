
package com.philips.cl.di.regsample.app;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.adobe.mobile.Config;
import com.philips.cl.di.reg.listener.UserRegistrationListener;
import com.philips.cl.di.reg.settings.RegistrationHelper;
import com.philips.cl.di.reg.ui.traditional.RegistrationActivity;
import com.philips.cl.di.reg.ui.utils.RLog;
import com.philips.cl.di.reg.ui.utils.RegConstants;
import com.philips.cl.di.reg.ui.utils.RegistrationLaunchHelper;

public class RegistrationSampleActivity extends Activity implements OnClickListener,
        UserRegistrationListener {


    private Button mBtnRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationSampleActivity : onCreate");
        RLog.i(RLog.EVENT_LISTENERS, "RegistrationSampleActivity register: UserRegistrationListener");
        setContentView(R.layout.activity_main);
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
        Config.collectLifecycleData();
        RLog.d(RLog.ACTIVITY_LIFECYCLE, "RegistrationSampleActivity : onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Config.pauseCollectingLifecycleData();
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
                RegistrationLaunchHelper.launchRegistrationActivity(this);
                break;

            default:
                break;
        }

    }

    @Override
    public void onUserRegistrationComplete(Activity activity) {
        RLog.d(RLog.EVENT_LISTENERS, "RegistrationSampleActivity : onUserRegistrationComplete");
        activity.finish();
    }

    @Override
    public void onPrivacyPolicyClick(Activity activity) {
        RLog.d(RLog.EVENT_LISTENERS, "RegistrationSampleActivity : onPrivacyPolicyClick");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(
                com.philips.cl.di.reg.R.string.PrivacyPolicyURL)));
        activity.startActivity(browserIntent);
    }

    @Override
    public void onTermsAndConditionClick(Activity activity) {
        RLog.d(RLog.EVENT_LISTENERS, "RegistrationSampleActivity : onPrivacyPolicyClick");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(
                com.philips.cl.di.reg.R.string.PrivacyPolicyURL)));
        activity.startActivity(browserIntent);
    }
}
