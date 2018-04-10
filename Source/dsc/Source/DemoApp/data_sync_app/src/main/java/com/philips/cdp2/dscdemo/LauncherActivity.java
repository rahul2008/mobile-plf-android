package com.philips.cdp2.dscdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URLaunchInput;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.dscdemo.DSDemoAppuAppDependencies;
import com.philips.platform.dscdemo.DSDemoAppuAppInterface;
import com.philips.platform.dscdemo.DSDemoAppuAppLaunchInput;
import com.philips.platform.mya.csw.justintime.JustInTimeTextResources;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappSettings;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;

public class LauncherActivity extends AppCompatActivity {
    private static final String TAG = "LauncherActivity";
    private DSDemoAppuAppInterface dsDemoAppuAppInterface;

    private View.OnClickListener launchClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.dscdemo_launcher_button_activity) {
                // Launch as Activity
                launchAsActivity();
            }
            else if(view.getId() == R.id.dscdemo_launcher_button_fragment) {
                // Launch as fragment
                launchAsFragment();
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher_activity);
        dsDemoAppuAppInterface = new DSDemoAppuAppInterface();

        findViewById(R.id.dscdemo_launcher_button_activity).setOnClickListener(launchClickListener);
        findViewById(R.id.dscdemo_launcher_button_fragment).setOnClickListener(launchClickListener);
    }

    private void launchAsActivity() {
        UiLauncher dsLauncher = new ActivityLauncher(
                ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED,
                new ThemeConfiguration(this, ColorRange.PURPLE, ContentColor.VERY_LIGHT, NavigationColor.BRIGHT, AccentRange.PINK),
                R.style.Theme_DLS_Purple_VeryLight,
                null
        );
        launch(dsLauncher);
    }

    private void launchAsFragment() {
        UiLauncher fragmentLauncher = new FragmentLauncher(
                this,
                R.id.dscdemo_app_container,
                null
        );
        launch(fragmentLauncher);
    }

    private void launch(UiLauncher launcher) {
        UappDependencies dsDemoDeps = createDependencies();
        UappSettings dsAppSettings = createSettings();

        dsDemoAppuAppInterface.init(dsDemoDeps, dsAppSettings);
        dsDemoAppuAppInterface.launch(launcher, new DSDemoAppuAppLaunchInput());
    }

    private UappDependencies createDependencies() {
        DemoApplication app = (DemoApplication) getApplication();

        AppInfraInterface ail = app.getAppInfra();
        JustInTimeTextResources jitTextRes = new JustInTimeTextResources();
        return new DSDemoAppuAppDependencies(ail, jitTextRes);
    }

    private UappSettings createSettings() {
        return new UappSettings(getApplicationContext());
    }

    private void openSignInMicroApp() {
        URLaunchInput userRegInput = new URLaunchInput();
        userRegInput.setRegistrationFunction(RegistrationFunction.SignIn);
        userRegInput.setUserRegistrationUIEventListener(new UserRegistrationUIEventListener() {
            @Override
            public void onUserRegistrationComplete(Activity activity) {
                Log.e(TAG, "onUserRegistrationComplete: user registration complete ");
            }

            @Override
            public void onPrivacyPolicyClick(Activity activity) {

            }

            @Override
            public void onTermsAndConditionClick(Activity activity) {

            }
        });

        URInterface userRegInterface = new URInterface();

    }

}
