package com.philips.platform.mya.demouapp;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.philips.platform.mya.interfaces.MyaListener;
import com.philips.platform.mya.launcher.MyaDependencies;
import com.philips.platform.mya.launcher.MyaInterface;
import com.philips.platform.mya.launcher.MyaLaunchInput;
import com.philips.platform.mya.util.MYALog;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.UIDActivity;

public class DemoAppActivity extends UIDActivity {
    private final int DEFAULT_THEME = R.style.Theme_DLS_GroupBlue_Bright;

    public int checkedId = R.id.radioButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initThemeIfExists();
        super.onCreate(savedInstanceState);


        MYALog.enableLogging(true);
        setContentView(R.layout.demo_u_app_layout);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        showAppVersion();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                DemoAppActivity.this.checkedId = checkedId;
            }
        });
    }

    public void onClickMyAccounts(View view) {
        if (checkedId == R.id.radioButton) {
            MyaInterface myaInterface = new MyaInterface();
            myaInterface.init(new MyaDependencies(MyAccountDemoUAppInterface.getAppInfra()), null);
            myaInterface.launch(new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED, getDLSThemeConfiguration(this), R.style.Theme_DLS_Orange_Bright, null), new MyaLaunchInput(this, new MyaListener() {
                @Override
                public boolean onClickMyaItem(String itemName) {
                    return false;
                }
            }));
        } else {
            startActivity(new Intent(this, MyaLaunchFragmentActivity.class));
        }
    }

    private void initThemeIfExists() {
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

    protected ThemeConfiguration getDLSThemeConfiguration(Context context) {
        return new ThemeConfiguration(context, ColorRange.ORANGE, ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT, AccentRange.ORANGE);
    }

}
