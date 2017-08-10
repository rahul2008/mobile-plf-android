/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package cdp.philips.com.demoapp;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.LogoutHandler;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.devicepair.uappdependencies.DevicePairingUappInterface;
import com.philips.platform.devicepair.uappdependencies.DevicePairingUappSettings;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.uappinput.UappDependencies;

public class LauncherActivity extends AppCompatActivity implements ActionBarListener {

    private DevicePairingUappInterface devicePairingUappInterface;
    private Button mLaunchAsActivity;
    private Button mLaunchAsFragment;
    private Button mBtnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher_layout);

        mLaunchAsActivity = (Button) findViewById(R.id.btn_launch_activity);
        mLaunchAsFragment = (Button) findViewById(R.id.btn_launch_fragment);
        mBtnLogout = (Button) findViewById(R.id.btn_logout);

        devicePairingUappInterface = new DevicePairingUappInterface();
        AppInfra gAppInfra = new AppInfra.Builder().build(getApplicationContext());

        UappDependencies uappDependencies = new UappDependencies(gAppInfra);
        devicePairingUappInterface.init(uappDependencies, new DevicePairingUappSettings(getApplicationContext()));
    }

    public void launchAsActivity(View v) {
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
    public void updateActionBar(@StringRes int i, boolean b) {
        setTitle(getResources().getString(i));
    }

    @Override
    public void updateActionBar(String s, boolean b) {
        setTitle(s);
    }
}
