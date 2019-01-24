
package com.udi.demouapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.Button;

import java.util.ArrayList;

public class DemoAppActivity extends AppCompatActivity implements View.OnClickListener {
    private final int DEFAULT_THEME = R.style.Theme_DLS_Blue_UltraLight;
    //Theme
    public static final String KEY_ACTIVITY_THEME = "KEY_ACTIVITY_THEME";
    Button mLogin, mLogout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_app_layout);

        mLogin = findViewById(R.id.btn_login);
        mLogout = findViewById(R.id.btn_logout);
        mLogin.setOnClickListener(this);
        mLogout.setOnClickListener(this);
    }

    private void initTheme() {
        int themeIndex = getIntent().getIntExtra(KEY_ACTIVITY_THEME, DEFAULT_THEME);
        if (themeIndex <= 0) {
            themeIndex = DEFAULT_THEME;
        }
        getTheme().applyStyle(themeIndex, true);
        UIDHelper.init(new ThemeConfiguration(this, ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT, AccentRange.ORANGE));

    }

    private void launchUdiRegisteration(ActivityLauncher activityLauncher, UdiInterface urInterface) {
        URLaunchInput urLaunchInput;
        RLog.d(TAG, " : Registration");
        urLaunchInput = new URLaunchInput();
        urLaunchInput.setRegistrationFunction(RegistrationFunction.SignIn);
        urLaunchInput.setUserRegistrationUIEventListener(this);
        urLaunchInput.setEndPointScreen(RegistrationLaunchMode.USER_DETAILS);
        urLaunchInput.setRegistrationContentConfiguration(getRegistrationContentConfiguration());
        urInterface.launch(activityLauncher, urLaunchInput);
    }
    @Override
    public void onClick(View v) {
        if (v == mLogin) {

        } else if (v == mLogout) {

        }
    }
}
