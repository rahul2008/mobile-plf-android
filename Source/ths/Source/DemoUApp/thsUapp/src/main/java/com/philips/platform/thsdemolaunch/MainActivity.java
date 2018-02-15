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
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationLaunchMode;
import com.philips.cdp.registration.handlers.LogoutHandler;
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
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static utility.THSDemoAppConstants.DEPENDENT;

public class MainActivity extends UIDActivity implements ActionBarListener, View.OnClickListener,UserRegistrationListener, UserRegistrationUIEventListener, THSCompletionProtocol {

    private static final String KEY_ACTIVITY_THEME = "KEY_ACTIVITY_THEME";
    private final int DEFAULT_THEME = R.style.Theme_DLS_Purple_Bright;
    private FragmentLauncher fragmentLauncher;
    private THSMicroAppLaunchInput PTHMicroAppLaunchInput;
    private THSMicroAppInterfaceImpl PTHMicroAppInterface;
    private Toolbar toolbar;
    private ThemeConfiguration themeConfiguration;
    private RelativeLayout mFirstLayout;
    private THSConsumer mThsConsumer;

    Button launchAmwell;
    Button logout;
    Button addDependent;
    User user;
    ProgressBar mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        initTheme();
        themeConfiguration = new ThemeConfiguration(this, ColorRange.PURPLE, ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT, AccentRange.ORANGE);
        UIDHelper.init(themeConfiguration);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ths_activity_launch);
        toolbar = (Toolbar) findViewById(R.id.uid_toolbar);
        toolbar.setNavigationIcon(VectorDrawableCompat.create(getApplicationContext().getResources(), R.drawable.pth_back_icon, getTheme()));
        mFirstLayout = (RelativeLayout) findViewById(R.id.uappFragmentLayout_first);
        addDependent = (Button) findViewById(R.id.add_dependent);
        addDependent.setOnClickListener(this);
        mFirstLayout.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        UIDHelper.setTitle(this, "Am well");
        fragmentLauncher = new FragmentLauncher(this, R.id.uappFragmentLayout, this);

        user = new User(this);
        launchAmwell = (Button) findViewById(R.id.launch_amwell);
        launchAmwell.setOnClickListener(this);
        mProgress = (ProgressBar) findViewById(R.id.progress);
        logout = (Button) findViewById(R.id.logout);

        final Bundle extras = getIntent().getExtras();
        if(extras!=null){
            mThsConsumer = (THSConsumer) extras.getSerializable(DEPENDENT);
            if(mThsConsumer!=null){
                prepareLaunchAmwell();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!user.isUserSignIn()){
            logout.setVisibility(View.GONE);
        }

        logout.setOnClickListener(this);

        if(user.isUserSignIn()){
            logout.setText("Logout");
        }else {
            logout.setText("Login");
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.launch_amwell){
            prepareLaunchAmwell();
        }if(id == R.id.logout){
            mProgress.setVisibility(View.VISIBLE);
            user.logout(new LogoutHandler() {
                @Override
                public void onLogoutSuccess() {
                    logout.setText("Login");
                    mProgress.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this,"Logout Success!!!",Toast.LENGTH_SHORT).show();
                    logout.setVisibility(View.GONE);
                }

                @Override
                public void onLogoutFailure(int i, String s) {
                    Toast.makeText(MainActivity.this,"Logout failed!!!",Toast.LENGTH_SHORT).show();
                    mProgress.setVisibility(View.GONE);
                }
            });
        }else if(id == R.id.add_dependent){
            Intent intent = new Intent(MainActivity.this, THSDemoAddDependentActivity.class);
            startActivity(intent);
        }
    }

    private void prepareLaunchAmwell() {
        User user = new User(this);
        if (user != null && !user.isUserSignIn()) {
            startRegistrationFragment();
        } else {
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

        final THSMicroAppDependencies uappDependencies = new THSMicroAppDependencies(THSAppInfraInstance.getInstance().getAppInfraInterface());
        uappDependencies.setThsConsumer(mThsConsumer);
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
        //urLaunchInput.setEndPointScreen(RegistrationLaunchMode.DEFAULT);
        urLaunchInput.enableAddtoBackStack(true);
        urLaunchInput.setRegistrationFunction(RegistrationFunction.Registration);
        URInterface urInterface = new URInterface();
        urInterface.launch(fragmentLauncher, urLaunchInput);
    }

    @Override
    public void didExitTHS(THSExitType thsExitType) {

    }
}
