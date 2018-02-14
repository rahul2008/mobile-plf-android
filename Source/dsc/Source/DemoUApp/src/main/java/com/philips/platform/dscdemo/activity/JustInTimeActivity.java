package com.philips.platform.dscdemo.activity;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.philips.platform.dscdemo.R;
import com.philips.platform.mya.csw.justintime.JustInTimeConsentFragment;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;

public class JustInTimeActivity extends AppCompatActivity implements ActionBarListener {

    private static final String KEY_ACTIVITY_THEME = "KEY_ACTIVITY_THEME";
    private final int DEFAULT_THEME = R.style.Theme_DLS_Blue_UltraLight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.af_user_registration_activity);
        showFragment();
    }

    private void showFragment() {
        int containerId = R.id.frame_container_user_reg;
        FragmentLauncher fragmentLauncher = new FragmentLauncher(this, containerId, this);
        JustInTimeConsentFragment justInTimeFragment = JustInTimeConsentFragment.newInstance(containerId);
        FragmentActivity fragmentActivity = fragmentLauncher.getFragmentActivity();
        FragmentTransaction fragmentTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        String simpleName = justInTimeFragment.getClass().getSimpleName();
        fragmentTransaction.replace(containerId, justInTimeFragment, simpleName);
        fragmentTransaction.commitAllowingStateLoss();
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
    public void updateActionBar(int resId, boolean enableBackKey) {

    }

    @Override
    public void updateActionBar(String resString, boolean enableBackKey) {

    }
}
