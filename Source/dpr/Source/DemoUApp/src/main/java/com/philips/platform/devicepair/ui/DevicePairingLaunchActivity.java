/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.devicepair.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationLaunchMode;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URLaunchInput;
import com.philips.platform.devicepair.R;
import com.philips.platform.dscdemo.utility.SyncScheduler;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;

public class DevicePairingLaunchActivity extends AppCompatActivity implements UserRegistrationListener, UserRegistrationUIEventListener, ActionBarListener {

    private static final String KEY_ACTIVITY_THEME = "KEY_ACTIVITY_THEME";
    private final int DEFAULT_THEME = R.style.Theme_DLS_Blue_UltraLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_pairing_launch_layout);

        User user = new User(this);
        if (user.isUserSignIn()) {
            SyncScheduler.getInstance().scheduleSync();
            showFragment();
        } else {
            startRegistrationFragment();
        }
    }

    private void initTheme() {
        int themeIndex = getIntent().getIntExtra(KEY_ACTIVITY_THEME, DEFAULT_THEME);
        if (themeIndex <= 0) {
            themeIndex = DEFAULT_THEME;
        }
        getTheme().applyStyle(themeIndex, true);
        UIDHelper.init(new ThemeConfiguration(this, ColorRange.GROUP_BLUE, ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT, AccentRange.ORANGE));
    }

    public void showFragment() {
        int containerId = R.id.dpr_frame_container;

        FragmentLauncher fragmentLauncher = new FragmentLauncher(this, containerId, this);
        DevicePairingBaseFragment pairingFragment = new PairingFragment();
        pairingFragment.showFragment(pairingFragment, fragmentLauncher);
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
        launchRegistrationFragment();
    }

    private void launchRegistrationFragment() {
        int containerID = R.id.dpr_frame_container;
        URLaunchInput urLaunchInput = new URLaunchInput();
        urLaunchInput.setUserRegistrationUIEventListener(this);
        urLaunchInput.setEndPointScreen(RegistrationLaunchMode.DEFAULT);
        urLaunchInput.enableAddtoBackStack(true);
        urLaunchInput.setRegistrationFunction(RegistrationFunction.Registration);
        FragmentLauncher fragmentLauncher = new FragmentLauncher
                (DevicePairingLaunchActivity.this, containerID, this);
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
        Fragment currentFrag = fragmentManager.findFragmentById(R.id.dpr_frame_container);
        if (currentFrag instanceof PairingFragment || !(new User(this).isUserSignIn())) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void updateActionBar(@StringRes int i, boolean b) {
        setTitle(getResources().getString(i));
    }

    @Override
    public void updateActionBar(String s, boolean b) {
        setTitle(s);
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

    @Override
    public void onUserRegistrationComplete(Activity activity) {
        runOnUiThread(new Runnable() {
            public void run() {
                SyncScheduler.getInstance().scheduleSync();
            }
        });
        showFragment();
    }

    @Override
    public void onPrivacyPolicyClick(Activity activity) {

    }

    @Override
    public void onTermsAndConditionClick(Activity activity) {

    }
}
