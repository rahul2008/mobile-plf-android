
package com.udi.demouapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.udi.integration.UDIDependencies;
import com.philips.platform.udi.integration.UDIInterface;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.Button;

public class DemoAppActivity extends AppCompatActivity implements View.OnClickListener {
    private final int DEFAULT_THEME = R.style.Theme_DLS_Blue_UltraLight;
    //Theme
    public static final String KEY_ACTIVITY_THEME = "KEY_ACTIVITY_THEME";
    Button mLogin;
    UDIInterface udiInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_app_layout);

        mLogin = findViewById(R.id.btn_login);
        mLogin.setOnClickListener(this);
        UdiDemoUAppDependencies udiDemoUAppDependencies = new UdiDemoUAppDependencies(new AppInfra.Builder().build(this));
        UdiDemoAppSettings udiDemoAppSettings = new UdiDemoAppSettings(this);
        udiInterface = new UDIInterface();
        udiInterface.init(udiDemoUAppDependencies, udiDemoAppSettings);
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
        if (v == mLogin) {
            ActivityLauncher activityLauncher = new ActivityLauncher(this, ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_SENSOR, null, 0, null);
            udiInterface.launch(activityLauncher, null);
        }
    }
}
