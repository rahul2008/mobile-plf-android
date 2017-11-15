/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.thsdemolaunch;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.uappclasses.THSMicroAppDependencies;
import com.philips.platform.ths.uappclasses.THSMicroAppInterfaceImpl;
import com.philips.platform.ths.uappclasses.THSMicroAppLaunchInput;
import com.philips.platform.ths.uappclasses.THSMicroAppSettings;
import com.philips.platform.ths.uappclasses.THSVisitCompletionListener;
import com.philips.platform.ths.utility.AmwellLog;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends UIDActivity implements ActionBarListener, UserRegistrationListener, UserRegistrationUIEventListener,THSVisitCompletionListener {

    private static final String KEY_ACTIVITY_THEME = "KEY_ACTIVITY_THEME";
    private final int DEFAULT_THEME = R.style.Theme_DLS_GroupBlue_Bright;
    private FragmentLauncher fragmentLauncher;
    private THSMicroAppLaunchInput PTHMicroAppLaunchInput;
    private THSMicroAppInterfaceImpl PTHMicroAppInterface;
    private Toolbar toolbar;
    private ThemeConfiguration themeConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        initTheme();
        themeConfiguration = new ThemeConfiguration(this, ColorRange.GROUP_BLUE, ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT, AccentRange.ORANGE);
        UIDHelper.init(themeConfiguration);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ths_activity_launch);
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


        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFrag = fragmentManager.findFragmentById(R.id.uappFragmentLayout);
        if (fragmentManager.getBackStackEntryCount() == 1) {
            finish();
        } else if (currentFrag != null && currentFrag instanceof BackEventListener && !((BackEventListener) currentFrag).handleBackEvent()) {
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

        Drawable drawable = getResources().getDrawable(R.drawable.ths_welcome,getTheme());
        BitmapDrawable bitmapDrawable = ((BitmapDrawable) drawable);
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream); //use the compression format of your need
        ByteArrayInputStream is = new ByteArrayInputStream(stream.toByteArray());

        User user = new User(this);
        THSConsumer thsConsumer = new THSConsumer();

        THSConsumer baby = new THSConsumer();
        baby.setFirstName("baby1");
        baby.setLastName("Hosur");
        baby.setHsdoToken(user.getHsdpAccessToken());
        baby.setGender(Gender.MALE);
        baby.setEmail(user.getEmail());
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016,8,24);
        baby.setDob(calendar.getTime());
        baby.setHsdpUUID(calendar.getTime()+"baby1");
        baby.setDependent(true);
        baby.setBloodPressureSystolic("120");
        baby.setBloodPressureDiastolic("80");
        baby.setTemperature(90.0);
        baby.setWeight(56);
        baby.setProfilePic(is);
        ArrayList dependants = new ArrayList();
        dependants.add(baby);

        THSConsumer baby2 = new THSConsumer();
        baby2.setFirstName("baby2");
        baby2.setLastName("Hallur");
        baby2.setHsdoToken("0190c6eb-b8ad-4d3c-a7b3-fee0ace65d78_12390");
        baby2.setGender(Gender.MALE);
        baby2.setEmail(user.getEmail());
        Calendar calendar2 = Calendar.getInstance();
        calendar.set(2015,8,24);
        baby2.setDob(calendar2.getTime());
        baby2.setHsdpUUID(calendar.getTime() + "baby2");
        baby2.setDependent(true);
        baby2.setBloodPressureSystolic("120");
        baby2.setBloodPressureDiastolic("80");
        baby2.setTemperature(90.0);
        baby2.setWeight(56);
        baby2.setProfilePic(is);
        //ArrayList dependants = new ArrayList();
        dependants.add(baby2);

        thsConsumer.setDob(user.getDateOfBirth());
        thsConsumer.setEmail(user.getEmail());
        thsConsumer.setFirstName(user.getGivenName());
        thsConsumer.setGender(Gender.FEMALE);
        thsConsumer.setHsdoToken(user.getHsdpAccessToken());
        thsConsumer.setLastName(user.getFamilyName());
        thsConsumer.setDependents(dependants);
        thsConsumer.setHsdpUUID(user.getHsdpUUID());
        thsConsumer.setBloodPressureSystolic("120");
        thsConsumer.setBloodPressureDiastolic("80");
        thsConsumer.setTemperature(90.0);
        thsConsumer.setWeight(56);
        thsConsumer.setDependent(false);


        final THSMicroAppDependencies uappDependencies = new THSMicroAppDependencies(((THSDemoApplication) this.getApplicationContext()).getAppInfra());
        uappDependencies.setThsConsumer(thsConsumer);
        PTHMicroAppInterface.init(uappDependencies, new THSMicroAppSettings(this.getApplicationContext()));
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
        urLaunchInput.setEndPointScreen(RegistrationLaunchMode.DEFAULT);
        urLaunchInput.enableAddtoBackStack(true);
        urLaunchInput.setRegistrationFunction(RegistrationFunction.Registration);
        URInterface urInterface = new URInterface();
        urInterface.launch(fragmentLauncher, urLaunchInput);
    }

    @Override
    public void onTHSVisitComplete(boolean isTHSVisitComplete) {
        AmwellLog.d(this.getClass().getName(),Boolean.toString(isTHSVisitComplete));
    }
}
