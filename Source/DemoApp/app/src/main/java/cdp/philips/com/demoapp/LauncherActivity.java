/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package cdp.philips.com.demoapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationLaunchMode;
import com.philips.cdp.registration.handlers.LogoutHandler;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URLaunchInput;
import com.philips.cdp.uikit.UiKitActivity;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.dprdemo.uappdependencies.DevicePairingUappInterface;
import com.philips.platform.dprdemo.uappdependencies.DevicePairingUappSettings;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.uappinput.UappDependencies;

public class LauncherActivity extends UiKitActivity implements UserRegistrationListener, UserRegistrationUIEventListener, ActionBarListener {

    private DevicePairingUappInterface devicePairingUappInterface;
    private Button mLaunchAsActivity;
    private Button mLaunchAsFragment;
    private Button mBtnLogout;
    private ActionBarListener actionBarListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        mLaunchAsActivity = (Button) findViewById(R.id.btn_launch_activity);
        mLaunchAsFragment = (Button) findViewById(R.id.btn_launch_fragment);
        mBtnLogout = (Button) findViewById(R.id.btn_logout);

        User user = new User(this);

        if (savedInstanceState == null)
            if (user.isUserSignIn()) {
                showLaunchButton();
            } else {
                startRegistrationFragment();
            }
        else {
            onRestoreInstanceState(savedInstanceState);
        }

        devicePairingUappInterface = new DevicePairingUappInterface();
        AppInfra gAppInfra = new AppInfra.Builder().build(getApplicationContext());

        UappDependencies uappDependencies = new UappDependencies(gAppInfra);
        devicePairingUappInterface.init(uappDependencies, new DevicePairingUappSettings(getApplicationContext()));
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
        int containerID = R.id.frame_container;
        URLaunchInput urLaunchInput = new URLaunchInput();
        urLaunchInput.setUserRegistrationUIEventListener(this);
        urLaunchInput.setEndPointScreen(RegistrationLaunchMode.ACCOUNT_SETTINGS);
        urLaunchInput.setAccountSettings(true);
        urLaunchInput.enableAddtoBackStack(true);
        urLaunchInput.setRegistrationFunction(RegistrationFunction.Registration);
        FragmentLauncher fragmentLauncher = new FragmentLauncher
                (LauncherActivity.this, containerID, actionBarListener);
        URInterface urInterface = new URInterface();
        urInterface.launch(fragmentLauncher, urLaunchInput);
    }

    public void launchAsActivity(View v) {
        hideLaunchButton();
        ActivityLauncher activityLauncher =
                new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED, 0);
        devicePairingUappInterface.launch(activityLauncher, null);
    }

    public void launchAsFragment(View v) {
        hideLaunchButton();
        FragmentLauncher fragmentLauncher =
                new FragmentLauncher(this, R.id.frame_container, this);
        devicePairingUappInterface.launch(fragmentLauncher, null);
    }

    public void logout(View v) {
        mBtnLogout.setEnabled(false);

        User user = new User(LauncherActivity.this);
        if (!user.isUserSignIn()) return;

        user.logout(new LogoutHandler() {
            @Override
            public void onLogoutSuccess() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LauncherActivity.this, "Logout Success", Toast.LENGTH_SHORT).show();
                        startRegistrationFragment();
                    }
                });
            }

            @Override
            public void onLogoutFailure(int i, String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LauncherActivity.this, "Logout Failed", Toast.LENGTH_SHORT).show();
                        mBtnLogout.setEnabled(true);
                    }
                });
            }
        });
    }

    @Override
    public void updateActionBar(@StringRes int i, boolean b) {

    }

    @Override
    public void updateActionBar(String s, boolean b) {

    }

    private void hideLaunchButton() {
        mLaunchAsActivity.setVisibility(View.GONE);
        mLaunchAsFragment.setVisibility(View.GONE);
        mBtnLogout.setVisibility(View.GONE);
    }

    private void showLaunchButton() {
        mLaunchAsActivity.setVisibility(View.VISIBLE);
        mLaunchAsFragment.setVisibility(View.VISIBLE);
        mBtnLogout.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        showLaunchButton();
    }

    @Override
    public void onPrivacyPolicyClick(Activity activity) {

    }

    @Override
    public void onTermsAndConditionClick(Activity activity) {

    }
}
