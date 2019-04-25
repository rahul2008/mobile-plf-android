
package com.pim.demouapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.pim.PIMInterface;
import com.philips.platform.pim.PIMLaunchInput;
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
import java.util.ArrayList;
import java.util.HashMap;

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
        PIMDemoUAppDependencies pimDemoUAppDependencies = new PIMDemoUAppDependencies(PIMDemoUAppInterface.mAppInfra);
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
        HashMap<String, String> pimCustomClaims = setCustomClaims();
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

    private HashMap<String, String> setCustomClaims() {
        HashMap<String, String> pimCustomClaims = new HashMap<>();
        pimCustomClaims.put(PIMCustomClaims.RECEIVE_MARKETING_EMAIL, null);
        pimCustomClaims.put(PIMCustomClaims.RECEIVE_MARKETING_EMAIL_TIMESTAMP, null);
        pimCustomClaims.put(PIMCustomClaims.SOCIAL_PROFILES, null);
        pimCustomClaims.put(PIMCustomClaims.UUID, null);
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
