package com.philips.platform.mya.demouapp.activity;


import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.philips.platform.mya.demouapp.MyaConstants;
import com.philips.platform.mya.demouapp.R;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.UIDActivity;
import com.philips.platform.mya.util.MYALog;

public class DemoAppActivity extends UIDActivity implements View.OnClickListener {
    private final int DEFAULT_THEME = R.style.Theme_DLS_Blue_UltraLight;

    Button launchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initTheme();
        super.onCreate(savedInstanceState);


        MYALog.enableLogging(true);
        setContentView(R.layout.demo_u_app_layout);

        launchButton = (Button)findViewById(R.id.launch_consent);
        launchButton.setOnClickListener(this);

        showAppVersion();
    }

    @Override
    public void onClick(View view) {
        if (view == launchButton) {
            Log.i("djfhlaksdfhlaf", "askdjfhaksdfhaksdjfhasd");
        }
    }

    private void initTheme() {
        int themeIndex = getIntent().getIntExtra(MyaConstants.MYA_THEME, DEFAULT_THEME);
        if (themeIndex <= 0) {
            themeIndex = DEFAULT_THEME;
        }
        getTheme().applyStyle(themeIndex, true);
        UIDHelper.init(new ThemeConfiguration(this, ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT, AccentRange.ORANGE));

    }

    private void showAppVersion() {
        String code = null;
        try {
            code = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            MYALog.e(MYALog.LOG, e.getMessage());
        }
        TextView versionView = (TextView) findViewById(R.id.appversion);
        versionView.setText(String.valueOf(code));
    }
}
