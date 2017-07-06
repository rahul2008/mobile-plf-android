/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.dprdemo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationLaunchMode;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URLaunchInput;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.platform.dprdemo.DemoAppManager;
import com.philips.platform.dprdemo.R;
import com.philips.platform.dprdemo.SyncScheduler;
import com.philips.platform.dprdemo.database.DatabaseHelper;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

public class DevicePairingLaunchActivity extends AppCompatActivity implements UserRegistrationListener,
        UserRegistrationUIEventListener, ActionBarListener {
    private static final String TAG = "DevicePairingLaunchActivity";
    private ActionBarListener actionBarListener;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_registration_container);

        User user = new User(this);

        if (savedInstanceState == null)
            if (user.isUserSignIn()) {

                SyncScheduler.getInstance().scheduleSync();
                databaseHelper = DemoAppManager.getInstance().getDatabaseHelper();
                databaseHelper.getWritableDatabase();

                showFragment(new LaunchFragment(), LaunchFragment.TAG);
            } else {
                startRegistrationFragment();
            }
        else{
            onRestoreInstanceState(savedInstanceState);
        }
    }

    public void showFragment(Fragment fragment, String fragmentTag) {
        int containerId = R.id.user_registration_frame_container;
        try {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(containerId, fragment, fragmentTag);
            fragmentTransaction.addToBackStack(fragmentTag);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    void startRegistrationFragment() {
        loadPlugIn();
        runUserRegistration();
    }

    private void loadPlugIn() {
        User userObject = new User(this);
        userObject.registerUserRegistrationListener(this);
    }


    private void runUserRegistration() {
        launchRegistrationFragment(false);
    }

    private void launchRegistrationFragment(boolean isAccountSettings) {
        int containerID = R.id.user_registration_frame_container;
        URLaunchInput urLaunchInput = new URLaunchInput();
        urLaunchInput.setUserRegistrationUIEventListener(this);
        urLaunchInput.setEndPointScreen(RegistrationLaunchMode.ACCOUNT_SETTINGS);
        urLaunchInput.setAccountSettings(true);
        urLaunchInput.enableAddtoBackStack(true);
        urLaunchInput.setRegistrationFunction(RegistrationFunction.Registration);
        FragmentLauncher fragmentLauncher = new FragmentLauncher
                (DevicePairingLaunchActivity.this, containerID, actionBarListener);
        URInterface urInterface = new URInterface();
        urInterface.launch(fragmentLauncher, urLaunchInput);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFrag = fragmentManager.findFragmentById(R.id.user_registration_frame_container);
        if (currentFrag instanceof LaunchFragment) {
            finishAffinity();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onUserLogoutSuccess() {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserLogoutFailure() {
        Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserLogoutSuccessWithInvalidAccessToken() {

    }

    @Override
    public void onUserRegistrationComplete(Activity activity) {
        showFragment(new LaunchFragment(), LaunchFragment.TAG);
    }

    @Override
    public void onPrivacyPolicyClick(Activity activity) {

    }

    @Override
    public void onTermsAndConditionClick(Activity activity) {

    }

    @Override
    public void updateActionBar(@StringRes int i, boolean b) {

    }

    @Override
    public void updateActionBar(String s, boolean b) {

    }
}
