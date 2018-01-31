/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.thsdemolaunch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationLaunchMode;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.ui.utils.Gender;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URLaunchInput;
import com.philips.platform.myapplicationlibrary.R;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.uappclasses.THSCompletionProtocol;
import com.philips.platform.ths.uappclasses.THSMicroAppDependencies;
import com.philips.platform.ths.uappclasses.THSMicroAppInterfaceImpl;
import com.philips.platform.ths.uappclasses.THSMicroAppLaunchInput;
import com.philips.platform.ths.uappclasses.THSMicroAppSettings;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.UIDActivity;

import java.util.ArrayList;
import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends UIDActivity implements ActionBarListener, UserRegistrationListener, UserRegistrationUIEventListener, THSCompletionProtocol {

    private static final String KEY_ACTIVITY_THEME = "KEY_ACTIVITY_THEME";
    private final int DEFAULT_THEME = R.style.Theme_DLS_Purple_Bright;
    private FragmentLauncher fragmentLauncher;
    private THSMicroAppLaunchInput PTHMicroAppLaunchInput;
    private THSMicroAppInterfaceImpl PTHMicroAppInterface;
    private Toolbar toolbar;
    private ThemeConfiguration themeConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        initTheme();
        themeConfiguration = new ThemeConfiguration(this, ColorRange.PURPLE, ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT, AccentRange.ORANGE);
        UIDHelper.init(themeConfiguration);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ths_activity_launch);
        toolbar = (Toolbar) findViewById(R.id.uid_toolbar);
        toolbar.setNavigationIcon(VectorDrawableCompat.create(getApplicationContext().getResources(), R.drawable.pth_back_icon, getTheme()));
        setSupportActionBar(toolbar);
        UIDHelper.setTitle(this, "Am well");
        fragmentLauncher = new FragmentLauncher(this, R.id.uappFragmentLayout, this);

        User user = new User(this);
        if (user != null && !user.isUserSignIn()) {
            startRegistrationFragment();
        } else {
            launchAmwell();
        }
        // launchAmwell();
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
        this.finish();
/*

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFrag = fragmentManager.findFragmentById(R.id.uappFragmentLayout);
        if (fragmentManager.getBackStackEntryCount() == 1) {
            finish();
        } else if (currentFrag != null && currentFrag instanceof BackEventListener && !((BackEventListener) currentFrag).handleBackEvent()) {
            super.onBackPressed();
        }*/
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
        UIDHelper.init(new ThemeConfiguration(this, ColorRange.GROUP_BLUE, ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT, AccentRange.ORANGE));
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
        PTHMicroAppLaunchInput = new THSMicroAppLaunchInput("Launch Uapp Input", this);
        PTHMicroAppInterface = new THSMicroAppInterfaceImpl();

        User user = new User(this);

        THSConsumer baby1 = new THSConsumer();
        baby1.setFirstName("checkbaby1");
        baby1.setLastName("Hosur");
        baby1.setDisplayName("Heyyyyyy");
        baby1.setHsdpToken(user.getHsdpAccessToken());
        baby1.setGender(Gender.MALE);
        baby1.setEmail(user.getEmail());
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016,8,24);
        baby1.setDob(calendar.getTime());
        baby1.setHsdpUUID("baby1+ggggg");
        baby1.setDependent(true);
        baby1.setBloodPressureSystolic("120");
        baby1.setBloodPressureDiastolic("80");
        baby1.setTemperature(90.0);
        baby1.setWeight(56);

        ArrayList dependants = new ArrayList();
        dependants.add(baby1);

        THSConsumer baby2 = new THSConsumer();
        baby2.setFirstName("baby2");
        baby2.setLastName("Hallur");
        baby2.setDisplayName("Hiiiiiiii");
        baby2.setHsdpToken("0190c6eb-b8ad-4d3c-a7b3-fee0ace65d78_12390");
        baby2.setGender(Gender.FEMALE);
        baby2.setEmail(user.getEmail());
        Calendar calendar2 = Calendar.getInstance();
        calendar.set(2015, 8, 24);
        baby2.setDob(calendar2.getTime());
        baby2.setHsdpUUID("baby2");
        baby2.setDependent(true);
        baby2.setBloodPressureSystolic("120");
        baby2.setBloodPressureDiastolic("80");
        baby2.setTemperature(90.0);
        baby2.setWeight(56);
        dependants.add(baby2);


        THSConsumer baby3 = new THSConsumer();
        baby3.setFirstName("baby3");
        baby3.setLastName("Hosur");
        baby3.setDisplayName("Wassup");
        baby3.setHsdpToken(user.getHsdpAccessToken());
        baby3.setGender(Gender.MALE);
        baby3.setEmail(user.getEmail());

        calendar.set(2016,8,24);
        baby3.setDob(calendar.getTime());
        baby3.setHsdpUUID("baby3");
        baby3.setDependent(true);
        baby3.setBloodPressureSystolic("120");
        baby3.setBloodPressureDiastolic("80");
        baby3.setTemperature(90.0);
        baby3.setWeight(56);
        dependants.add(baby3);


        THSConsumer thsConsumer = new THSConsumer();
        thsConsumer.setDob(user.getDateOfBirth());
        thsConsumer.setEmail(user.getEmail());
        thsConsumer.setFirstName(user.getGivenName());
        thsConsumer.setGender(Gender.MALE);
        thsConsumer.setHsdpToken(user.getHsdpAccessToken());
        thsConsumer.setLastName(user.getFamilyName());
        thsConsumer.setDisplayName("Spoorti hihi");
        thsConsumer.setDependents(dependants);
        thsConsumer.setHsdpUUID(user.getHsdpUUID());
        thsConsumer.setBloodPressureSystolic("120");
        thsConsumer.setBloodPressureDiastolic("80");
        thsConsumer.setTemperature(90.0);
        thsConsumer.setWeight(56);
        thsConsumer.setDependent(false);


        final THSMicroAppDependencies uappDependencies = new THSMicroAppDependencies(THSAppInfraInstance.getInstance().getAppInfraInterface());
        uappDependencies.setThsConsumer(thsConsumer);
        PTHMicroAppInterface.init(uappDependencies, new THSMicroAppSettings(this.getApplicationContext()));
        ActivityLauncher activityLauncher = new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT,new ThemeConfiguration(this, ColorRange.GROUP_BLUE, ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT, AccentRange.ORANGE),R.style.Theme_DLS_GroupBlue_UltraLight, null);
        PTHMicroAppInterface.launch(activityLauncher, PTHMicroAppLaunchInput);

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
        urLaunchInput.setEndPointScreen(RegistrationLaunchMode.DEFAULT);
        urLaunchInput.enableAddtoBackStack(true);
        urLaunchInput.setRegistrationFunction(RegistrationFunction.Registration);
        URInterface urInterface = new URInterface();
        urInterface.launch(fragmentLauncher, urLaunchInput);
    }

    @Override
    public void didExitTHS(THSExitType thsExitType) {
        AmwellLog.d(this.getClass().getName(), thsExitType.toString());
        Intent intent = new Intent(this, FirstActivity.class);
        startActivity(intent);
    }
}
