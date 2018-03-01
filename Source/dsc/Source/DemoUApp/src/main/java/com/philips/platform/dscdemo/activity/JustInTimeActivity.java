package com.philips.platform.dscdemo.activity;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.philips.platform.dscdemo.R;
import com.philips.platform.mya.csw.justintime.JustInTimeConsentDependencies;
import com.philips.platform.mya.csw.justintime.JustInTimeConsentFragment;
import com.philips.platform.mya.csw.justintime.JustInTimeConsentPresenter;
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
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.csw_just_in_time_activity);
        Toolbar toolbar = findViewById(R.id.jit_toolbar);
        mTitle = toolbar.findViewById(R.id.jit_toolbar_title);
        showFragment();
    }

    private void showFragment() {
        int containerId = R.id.csw_justintime_frame_container;
        FragmentLauncher fragmentLauncher = new FragmentLauncher(this, containerId, this);
        JustInTimeConsentFragment justInTimeFragment = JustInTimeConsentFragment.newInstance(containerId);
        justInTimeFragment.setUpdateTitleListener(this);

        new JustInTimeConsentPresenter(justInTimeFragment, JustInTimeConsentDependencies.appInfra, JustInTimeConsentDependencies.consentRegistryInterface, JustInTimeConsentDependencies.consentDefinition, JustInTimeConsentDependencies.completionListener);

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
    public void setTitle(int titleId) {
        mTitle.setText(titleId);
    }

    @Override
    public void setTitle(final CharSequence title) {
        mTitle.setText(title);
    }

    @Override
    public void updateActionBar(int resId, boolean enableBackKey) {
        setTitle(resId);
    }

    @Override
    public void updateActionBar(String resString, boolean enableBackKey) {
        setTitle(resString);
    }
}
