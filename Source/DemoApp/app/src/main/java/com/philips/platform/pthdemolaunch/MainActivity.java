package com.philips.platform.pthdemolaunch;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;

import com.philips.amwelluapp.PTHMicroAppDependencies;
import com.philips.amwelluapp.PTHMicroAppInterface;
import com.philips.amwelluapp.PTHMicroAppLaunchInput;
import com.philips.amwelluapp.PTHMicroAppSettings;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;

public class MainActivity extends FragmentActivity implements ActionBarListener{

    private static final String KEY_ACTIVITY_THEME = "KEY_ACTIVITY_THEME";
    private final int DEFAULT_THEME = R.style.Theme_DLS_GroupBlue_UltraLight;

    private FragmentLauncher fragmentLauncher;
    private PTHMicroAppLaunchInput PTHMicroAppLaunchInput;
    private PTHMicroAppInterface PTHMicroAppInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pth_launch_activity);
        initAppInfra();
        fragmentLauncher = new FragmentLauncher(this,R.id.uappFragmentLayout,this);
        PTHMicroAppLaunchInput = new PTHMicroAppLaunchInput("Launch Uapp Input");
        PTHMicroAppInterface = new PTHMicroAppInterface();
        PTHMicroAppInterface.init(new PTHMicroAppDependencies(((AmwellDemoApplication)this.getApplicationContext()).getAppInfra()),new PTHMicroAppSettings(this.getApplicationContext()));
        PTHMicroAppInterface.launch(fragmentLauncher, PTHMicroAppLaunchInput);

    }

    private void initAppInfra() {
        ((AmwellDemoApplication)getApplicationContext()).initializeAppInfra(new AppInitializationCallback.AppInfraInitializationCallback() {
            @Override
            public void onAppInfraInitialization() {

            }
        });
    }

    @Override
    public void updateActionBar(@StringRes int i, boolean b) {

    }

    @Override
    public void updateActionBar(String s, boolean b) {

    }

    private void initTheme() {
        int themeIndex = getIntent().getIntExtra(KEY_ACTIVITY_THEME, DEFAULT_THEME);
        if (themeIndex <= 0) {
            themeIndex = DEFAULT_THEME;
        }
        getTheme().applyStyle(themeIndex, true);
        UIDHelper.init(new ThemeConfiguration(this, ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT));
    }
}
