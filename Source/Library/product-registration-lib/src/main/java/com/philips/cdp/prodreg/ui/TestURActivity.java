package com.philips.cdp.prodreg.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.philips.cdp.product_registration_lib.R;
import com.philips.cdp.registration.listener.RegistrationTitleBarListener;
import com.philips.cdp.registration.ui.traditional.RegistrationFragment;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;

public class TestURActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_ur);
    }

    public void onClickActivity(View view) {

    }

    public void onClickFragment(View view) {
        launchRegistrationFragment(R.id.parent_layout, this, false);
    }

    private void launchRegistrationFragment(int container, FragmentActivity
            fragmentActivity, boolean isAccountSettings) {
        try {
            FragmentManager mFragmentManager = fragmentActivity.getSupportFragmentManager();
            RegistrationFragment registrationFragment = new RegistrationFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(RegConstants.ACCOUNT_SETTINGS, isAccountSettings);
            registrationFragment.setArguments(bundle);
            registrationFragment.setOnUpdateTitleListener(new RegistrationTitleBarListener() {
                @Override
                public void updateRegistrationTitle(final int i) {

                }

                @Override
                public void updateRegistrationTitleWithBack(final int i) {

                }

                @Override
                public void updateRegistrationTitleWithOutBack(final int i) {

                }
            });
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
}
