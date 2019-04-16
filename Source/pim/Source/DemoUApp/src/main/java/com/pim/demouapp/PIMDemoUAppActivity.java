
package com.pim.demouapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.pim.integration.PIMLaunchInput;
import com.philips.platform.pim.utilities.PIMCustomClaims;
import com.philips.platform.pim.utilities.PIMScopes;
import com.philips.platform.pim.utilities.PIMStandardClaims;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;

import com.philips.platform.pim.integration.PIMInterface;

import java.util.ArrayList;

public class PIMDemoUAppActivity extends AppCompatActivity implements View.OnClickListener {
    final int DEFAULT_THEME = R.style.Theme_DLS_Blue_UltraLight;
    //Theme
    public static final String KEY_ACTIVITY_THEME = "KEY_ACTIVITY_THEME";
    Button btnLoginActivity, btnLoginFragment;
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
        PIMDemoUAppDependencies pimDemoUAppDependencies = new PIMDemoUAppDependencies(new AppInfra.Builder().build(this));
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


    @Override
    public void onClick(View v) {
        PIMLaunchInput launchInput = new PIMLaunchInput();
        ArrayList<String> pimScopes = setScopes();
        ArrayList<String> pimCustomClaims = setCustomClaims();
        launchInput.setPimScopes(pimScopes);
        launchInput.setPimCustomClaims(pimCustomClaims);
        if (v == btnLoginActivity) {
            ActivityLauncher activityLauncher = new ActivityLauncher(this, ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_SENSOR, null, 0, null);
            pimInterface.launch(activityLauncher, launchInput);
        } else if (v == btnLoginFragment) {
            btnLoginActivity.setVisibility(View.GONE);
            btnLoginFragment.setVisibility(View.GONE);
            FragmentLauncher fragmentLauncher = new FragmentLauncher(this, R.id.pimDemoU_mainFragmentContainer, null);
            pimInterface.launch(fragmentLauncher, launchInput);
        }
    }

    private ArrayList<String> setStandardClaims() {
        ArrayList<String> pimStandardClaims = new ArrayList<>();
        pimStandardClaims.add(PIMStandardClaims.FAMILY_NAME);
        pimStandardClaims.add(PIMStandardClaims.EMAIL);
        pimStandardClaims.add(PIMStandardClaims.BIRTH_DATE);
        pimStandardClaims.add(PIMStandardClaims.EMAIL_VERIFIED);
        pimStandardClaims.add(PIMStandardClaims.GENDER);
        pimStandardClaims.add(PIMStandardClaims.PHONE_NUMBER);
        pimStandardClaims.add(PIMStandardClaims.PHONE_NUMBER_VERIFIED);
        pimStandardClaims.add(PIMStandardClaims.GIVEN_NAME);
        pimStandardClaims.add(PIMStandardClaims.ADDRESS);
        pimStandardClaims.add(PIMStandardClaims.UPDATED_AT);
        return pimStandardClaims;
    }

    private ArrayList<String> setCustomClaims() {
        ArrayList<String> pimCustomClaims = new ArrayList<>();
        pimCustomClaims.add(PIMCustomClaims.RECEIVE_MARKETING_EMAIL);
        pimCustomClaims.add(PIMCustomClaims.RECEIVE_MARKETING_EMAIL_TIMESTAMP);
        pimCustomClaims.add(PIMCustomClaims.SOCIAL_PROFILES);
        pimCustomClaims.add(PIMCustomClaims.UUID);
        return pimCustomClaims;
    }

    private ArrayList<String> setScopes() {
        ArrayList<String> pimScopes = new ArrayList<>();
        pimScopes.add(PIMScopes.EMAIL);
        pimScopes.add(PIMScopes.OPENID);
        pimScopes.add(PIMScopes.ADDRESS);
        pimScopes.add(PIMScopes.PROFILE);
        pimScopes.add(PIMScopes.EMAIL);
        pimScopes.add(PIMScopes.PHONE);
        return pimScopes;
    }
}
