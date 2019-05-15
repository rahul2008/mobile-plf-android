
package com.pim.demouapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.pif.DataInterface.USR.UserCustomClaims;
import com.philips.platform.pif.DataInterface.USR.enums.Error;
import com.philips.platform.pif.DataInterface.USR.enums.UserLoggedInState;
import com.philips.platform.pif.DataInterface.USR.listeners.LogoutSessionListener;
import com.philips.platform.pim.PIMInterface;
import com.philips.platform.pim.PIMLaunchInput;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.Switch;

import java.util.ArrayList;

public class PIMDemoUAppActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG = PIMDemoUAppActivity.class.getSimpleName();
    final int DEFAULT_THEME = R.style.Theme_DLS_Blue_UltraLight;
    //Theme
    public static final String KEY_ACTIVITY_THEME = "KEY_ACTIVITY_THEME";
    Button btnLoginActivity, btnLoginFragment, btnLogout;
    Switch aSwitch;
    PIMInterface pimInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pim_demo_uapp);

        Label appversion = findViewById(R.id.appversion);
        appversion.setText("Version : " + BuildConfig.VERSION_NAME);
        btnLoginActivity = findViewById(R.id.btn_login_activity);
        btnLoginActivity.setOnClickListener(this);
        btnLoginFragment = findViewById(R.id.btn_login_fragment);
        btnLoginFragment.setOnClickListener(this);
        btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(this);
        aSwitch = findViewById(R.id.switch_cookies_consent);
        AppInfraInterface appInfraInterface = PIMDemoUAppInterface.mAppInfra;
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    appInfraInterface.getTagging().setPrivacyConsent(AppTaggingInterface.PrivacyStatus.OPTIN);
                else
                    appInfraInterface.getTagging().setPrivacyConsent(AppTaggingInterface.PrivacyStatus.OPTOUT);
            }
        });
        PIMDemoUAppDependencies pimDemoUAppDependencies = new PIMDemoUAppDependencies(appInfraInterface);
        PIMDemoUAppSettings pimDemoUAppSettings = new PIMDemoUAppSettings(this);
        pimInterface = new PIMInterface();
        pimInterface.init(pimDemoUAppDependencies, pimDemoUAppSettings);
    }

    private void initTheme() {
        int themeIndex = getIntent().getIntExtra(KEY_ACTIVITY_THEME, DEFAULT_THEME);
        if (themeIndex <= 0) {
            themeIndex = DEFAULT_THEME;
        }
        getTheme().applyStyle(themeIndex, true);
        UIDHelper.init(new ThemeConfiguration(this, ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT, AccentRange.ORANGE));
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (pimInterface.getUserDataInterface().getUserLoggedInState() == UserLoggedInState.USER_LOGGED_IN) {
//            btnLoginActivity.setVisibility(View.VISIBLE);
//            btnLoginFragment.setVisibility(View.VISIBLE);
//            btnLogout.setVisibility(View.VISIBLE);
//        }
//    }

    @Override
    public void onClick(View v) {
        PIMLaunchInput launchInput = new PIMLaunchInput();
        ArrayList<String> pimCustomClaims = setCustomClaims();
        launchInput.setCustomClaims(pimCustomClaims);
        if (v == btnLoginActivity) {
            ActivityLauncher activityLauncher = new ActivityLauncher(this, ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_SENSOR, null, 0, null);
            pimInterface.launch(activityLauncher, launchInput);
        } else if (v == btnLoginFragment) {
//            btnLoginActivity.setVisibility(View.GONE);
//            btnLoginFragment.setVisibility(View.GONE);
//            btnLogout.setVisibility(View.GONE);
            FragmentLauncher fragmentLauncher = new FragmentLauncher(this, R.id.pimDemoU_mainFragmentContainer, null);
            pimInterface.launch(fragmentLauncher, launchInput);
        } else if (v == btnLogout) {
            if (pimInterface.getUserDataInterface().getUserLoggedInState() == UserLoggedInState.USER_LOGGED_IN) {
//                btnLoginActivity.setVisibility(View.GONE);
//                btnLoginFragment.setVisibility(View.GONE);
//                btnLogout.setVisibility(View.GONE);
                pimInterface.getUserDataInterface().logoutSession(new LogoutSessionListener() {
                    @Override
                    public void logoutSessionSuccess() {
                        Log.d(TAG, "PIM Logout success");
                    }

                    @Override
                    public void logoutSessionFailed(Error error) {
                        Log.d(TAG, "PIM Logout failed due to : " + error.getErrDesc());
                    }
                });
            } else {
                Toast.makeText(this, "User is not loged-in, Please login!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private ArrayList<String> setCustomClaims() {
        ArrayList<String> pimCustomClaims = new ArrayList<>();
        pimCustomClaims.add(UserCustomClaims.RECEIVE_MARKETING_EMAIL);
        pimCustomClaims.add(UserCustomClaims.RECEIVE_MARKETING_EMAIL_TIMESTAMP);
        pimCustomClaims.add(UserCustomClaims.SOCIAL_PROFILES);
        pimCustomClaims.add(UserCustomClaims.UUID);
        return pimCustomClaims;
    }
}
