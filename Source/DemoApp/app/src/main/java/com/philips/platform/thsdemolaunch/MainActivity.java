package com.philips.platform.thsdemolaunch;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.configuration.RegistrationLaunchMode;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URLaunchInput;
import com.philips.platform.ths.uappclasses.THSMicroAppDependencies;
import com.philips.platform.ths.uappclasses.THSMicroAppInterface;
import com.philips.platform.ths.uappclasses.THSMicroAppLaunchInput;
import com.philips.platform.ths.uappclasses.THSMicroAppSettings;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.UIDActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends UIDActivity implements ActionBarListener, UserRegistrationListener, UserRegistrationUIEventListener {

    private static final String KEY_ACTIVITY_THEME = "KEY_ACTIVITY_THEME";
    private final int DEFAULT_THEME = R.style.Theme_DLS_GroupBlue_UltraLight;
    private FragmentLauncher fragmentLauncher;
    private THSMicroAppLaunchInput PTHMicroAppLaunchInput;
    private THSMicroAppInterface PTHMicroAppInterface;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ths_launch_activity);
        toolbar = (Toolbar) findViewById(R.id.uid_toolbar);
        toolbar.setNavigationIcon(VectorDrawableCompat.create(getApplicationContext().getResources(), R.drawable.pth_back_icon, getTheme()));
        setSupportActionBar(toolbar);
        UIDHelper.setTitle(this, "Am well");
        fragmentLauncher = new FragmentLauncher(this, R.id.uappFragmentLayout, this);
        User user = new User(this);
        if(user!=null && !user.isUserSignIn()) {
            startRegistrationFragment();
        }else {
            launchAmwell();
        }
    }


    private void showBackImage(boolean isVisible) {
        if (isVisible) {
            toolbar.setNavigationIcon(VectorDrawableCompat.create(getApplicationContext().getResources(), R.drawable.pth_back_icon, getTheme()));
        } else {
            toolbar.setNavigationIcon(null);
        }

    }

    @Override
    public void updateActionBar(@StringRes int i, boolean b) {
        UIDHelper.setTitle(this, getString(i));
        showBackImage(b);
    }

    @Override
    public void updateActionBar(String s, boolean b) {
        UIDHelper.setTitle(this, s);
        showBackImage(b);
    }


    @Override
    public void onBackPressed() {

        boolean backState;
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFrag = fragmentManager.findFragmentById(R.id.uappFragmentLayout);
        if (fragmentManager.getBackStackEntryCount() == 2) {
            finishAffinity();
        } else if (currentFrag instanceof BackEventListener) {
            backState = ((BackEventListener) currentFrag).handleBackEvent();
            if (!backState) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void initTheme() {
        int themeIndex = getIntent().getIntExtra(KEY_ACTIVITY_THEME, DEFAULT_THEME);
        if (themeIndex <= 0) {
            themeIndex = DEFAULT_THEME;
        }
        getTheme().applyStyle(themeIndex, true);
        UIDHelper.init(new ThemeConfiguration(this, ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT, AccentRange.ORANGE));
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
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
        launchAmwell();
    }

    private void launchAmwell() {
        PTHMicroAppLaunchInput = new THSMicroAppLaunchInput("Launch Uapp Input");
        PTHMicroAppInterface = new THSMicroAppInterface();
        PTHMicroAppInterface.init(new THSMicroAppDependencies(((THSDemoApplication) this.getApplicationContext()).getAppInfra()), new THSMicroAppSettings(this.getApplicationContext()));
        PTHMicroAppInterface.launch(fragmentLauncher, PTHMicroAppLaunchInput);
    }

    @Override
    public void onPrivacyPolicyClick(Activity activity) {

    }

    @Override
    public void onTermsAndConditionClick(Activity activity) {

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

    /**
     * Launch registration fragment
     */
    private void launchRegistrationFragment(boolean isAccountSettings) {
        URLaunchInput urLaunchInput = new URLaunchInput();
        urLaunchInput.setUserRegistrationUIEventListener(this);
        urLaunchInput.setEndPointScreen(RegistrationLaunchMode.ACCOUNT_SETTINGS);
        urLaunchInput.setAccountSettings(true);
        urLaunchInput.enableAddtoBackStack(true);
        urLaunchInput.setRegistrationFunction(RegistrationFunction.Registration);
        URInterface urInterface = new URInterface();
        urInterface.launch(fragmentLauncher, urLaunchInput);
    }
}
