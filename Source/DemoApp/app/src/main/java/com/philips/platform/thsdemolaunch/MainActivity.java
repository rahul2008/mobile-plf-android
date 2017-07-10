package com.philips.platform.thsdemolaunch;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.philips.platform.ths.uappclasses.THSMicroAppDependencies;
import com.philips.platform.ths.uappclasses.THSMicroAppInterface;
import com.philips.platform.ths.uappclasses.THSMicroAppLaunchInput;
import com.philips.platform.ths.uappclasses.THSMicroAppSettings;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.UIDActivity;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends UIDActivity implements ActionBarListener{

    private static final String KEY_ACTIVITY_THEME = "KEY_ACTIVITY_THEME";
    private final int DEFAULT_THEME = R.style.Theme_DLS_GroupBlue_UltraLight;
    private FragmentLauncher fragmentLauncher;
    private THSMicroAppLaunchInput THSMicroAppLaunchInput;
    private THSMicroAppInterface THSMicroAppInterface;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ths_launch_activity);
        initAppInfra();
        toolbar = (Toolbar) findViewById(R.id.uid_toolbar);
        toolbar.setNavigationIcon(VectorDrawableCompat.create(getApplicationContext().getResources(), R.drawable.pth_back_icon,getTheme()));
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        UIDHelper.setTitle(this, "Am well");
        fragmentLauncher = new FragmentLauncher(this,R.id.uappFragmentLayout,this);
        THSMicroAppLaunchInput = new THSMicroAppLaunchInput("Launch Uapp Input");
        THSMicroAppInterface = new THSMicroAppInterface();
        THSMicroAppInterface.init(new THSMicroAppDependencies(((THSDemoApplication)this.getApplicationContext()).getAppInfra()),new THSMicroAppSettings(this.getApplicationContext()));
        THSMicroAppInterface.launch(fragmentLauncher, THSMicroAppLaunchInput);

    }

    private void initAppInfra() {
        ((THSDemoApplication)getApplicationContext()).initializeAppInfra(new AppInitializationCallback.AppInfraInitializationCallback() {
            @Override
            public void onAppInfraInitialization() {

            }
        });
    }

    private void showBackImage(boolean isVisible){
        if(isVisible){
            toolbar.setNavigationIcon(VectorDrawableCompat.create(getApplicationContext().getResources(), R.drawable.pth_back_icon,getTheme()));
        }
        else {
            toolbar.setNavigationIcon(null);
        }

    }

    @Override
    public void updateActionBar(@StringRes int i, boolean b) {
        UIDHelper.setTitle(this, getString(i));
            showBackImage(b);
    }

    @Override
    public void updateActionBar(String s, boolean b) {
        UIDHelper.setTitle(this, s);
            showBackImage(b);
    }


    @Override
    public void onBackPressed() {

        boolean backState;
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFrag = fragmentManager.findFragmentById(R.id.uappFragmentLayout);
        if (fragmentManager.getBackStackEntryCount() == 2) {
            finishAffinity();
        } else if (currentFrag instanceof BackEventListener) {
            backState = ((BackEventListener) currentFrag).handleBackEvent();
            if (!backState) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    private void initTheme() {
        int themeIndex = getIntent().getIntExtra(KEY_ACTIVITY_THEME, DEFAULT_THEME);
        if (themeIndex <= 0) {
            themeIndex = DEFAULT_THEME;
        }
        getTheme().applyStyle(themeIndex, true);
        UIDHelper.init(new ThemeConfiguration(this, ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT));
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
