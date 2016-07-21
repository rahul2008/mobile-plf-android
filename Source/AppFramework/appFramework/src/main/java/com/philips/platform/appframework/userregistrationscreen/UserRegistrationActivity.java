package com.philips.platform.appframework.userregistrationscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.registration.listener.RegistrationTitleBarListener;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.traditional.RegistrationFragment;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HomeActivity;

/**
 * Created by 310240027 on 7/12/2016.
 */
public class UserRegistrationActivity extends AppFrameworkBaseActivity implements UserRegistrationListener, RegistrationTitleBarListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.af_user_registration_activity);
        RegistrationHelper.getInstance().registerUserRegistrationListener(this);
        launchRegistrationFragment(R.id.frame_container_user_reg, this, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RegistrationHelper.getInstance().unRegisterUserRegistrationListener(this);
    }

    /**
     * Launch registration fragment
     */
    private void launchRegistrationFragment(int container, FragmentActivity fragmentActivity, boolean isAccountSettings) {
        try {
            FragmentManager mFragmentManager = fragmentActivity.getSupportFragmentManager();
            RegistrationFragment registrationFragment = new RegistrationFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(RegConstants.ACCOUNT_SETTINGS, isAccountSettings);
            registrationFragment.setArguments(bundle);
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
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UserRegistrationActivity.this, HomeActivity.class));
    }

    @Override
    public void updateRegistrationTitle(int i) {

    }

    @Override
    public void updateRegistrationTitleWithBack(int i) {

    }

    @Override
    public void updateRegistrationTitleWithOutBack(int i) {

    }

    @Override
    public void onUserRegistrationComplete(Activity activity) {
        if (null != activity) {
            startActivity(new Intent(UserRegistrationActivity.this, HomeActivity.class));
        }
    }

    @Override
    public void onPrivacyPolicyClick(Activity activity) {

    }

    @Override
    public void onTermsAndConditionClick(Activity activity) {

    }

    @Override
    public void onUserLogoutSuccess() {

    }

    @Override
    public void onUserLogoutFailure() {

    }

    @Override
    public void onUserLogoutSuccessWithInvalidAccessToken() {

    }

}
