package cdp.philips.com.activity;

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
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.core.utils.UuidGenerator;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import cdp.philips.com.R;
import cdp.philips.com.database.DatabaseHelper;
import cdp.philips.com.temperature.TemperatureTimeLineFragment;
import cdp.philips.com.utility.SyncScheduler;


public class DemoActivity extends AppCompatActivity implements UserRegistrationListener, UserRegistrationUIEventListener, ActionBarListener {

    private ActionBarListener actionBarListener;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.af_user_registration_activity);
        DSLog.enableLogging(true);
        User user = new User(this);

        if (savedInstanceState == null)
            if (user.isUserSignIn()) {
                SyncScheduler.getInstance().scheduleSync();
                showFragment(new TemperatureTimeLineFragment(), TemperatureTimeLineFragment.TAG);
                databaseHelper = DatabaseHelper.getInstance(getApplicationContext(), new UuidGenerator());
                databaseHelper.getWritableDatabase();
            } else {
                startRegistrationFragment();
            }
        else {
            onRestoreInstanceState(savedInstanceState);
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }


    private void runUserRegistration() {
        launchRegistrationFragment(false);
    }

    /**
     * Launch registration fragment
     */
    private void launchRegistrationFragment(boolean isAccountSettings) {
        int containerID = R.id.frame_container_user_reg;
        URLaunchInput urLaunchInput = new URLaunchInput();
        urLaunchInput.setUserRegistrationUIEventListener(this);
        urLaunchInput.setEndPointScreen(RegistrationLaunchMode.ACCOUNT_SETTINGS);
        urLaunchInput.setAccountSettings(true);
        urLaunchInput.enableAddtoBackStack(true);
        urLaunchInput.setRegistrationFunction(RegistrationFunction.Registration);
        FragmentLauncher fragmentLauncher = new FragmentLauncher
                (DemoActivity.this, containerID, actionBarListener);
        URInterface urInterface = new URInterface();
        urInterface.launch(fragmentLauncher, urLaunchInput);
    }

    @Override
    public void onUserLogoutSuccess() {
        runOnUiThread(new Runnable() {
            public void run() {
                SyncScheduler.getInstance().stopSync();
            }
        });
    }

    @Override
    public void onUserLogoutFailure() {
        Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserLogoutSuccessWithInvalidAccessToken() {
        DSLog.i(DSLog.LOG, "CALLBACK FROM UR RECIEVED");
        runOnUiThread(new Runnable() {
            public void run() {
                SyncScheduler.getInstance().stopSync();
            }
        });
    }

    @Override
    public void onUserRegistrationComplete(final Activity activity) {
        runOnUiThread(new Runnable() {
            public void run() {
                SyncScheduler.getInstance().scheduleSync();
            }
        });

        showFragment(new TemperatureTimeLineFragment(), TemperatureTimeLineFragment.TAG);
    }

    public void showFragment(Fragment fragment, String fragmentTag) {
        int containerId = R.id.frame_container_user_reg;

        try {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(containerId, fragment, fragmentTag);
            fragmentTransaction.addToBackStack(fragmentTag);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPrivacyPolicyClick(final Activity activity) {

    }

    @Override
    public void onTermsAndConditionClick(final Activity activity) {

    }

    @Override
    public void updateActionBar(@StringRes final int i, final boolean b) {

    }

    @Override
    public void updateActionBar(final String s, final boolean b) {

    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFrag = fragmentManager.findFragmentById(R.id.frame_container_user_reg);
        if (currentFrag instanceof TemperatureTimeLineFragment) {
            finishAffinity();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (databaseHelper != null && databaseHelper.isOpen()) {
            databaseHelper.close();
        }
        //DataServicesManager.getInstance().releaseDataServicesInstances();
    }
}
