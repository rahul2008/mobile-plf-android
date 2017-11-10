package com.philips.cdp2.ews.demoapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.philips.cdp2.ews.microapp.EWSInterface;
import com.philips.cdp2.ews.microapp.EWSLauncherInput;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.uappinput.UappSettings;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class EWSDemoFragmentActivity extends EWSDemoBaseActivity implements ActionBarListener {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(com.philips.cdp2.ews.R.style.Theme_DLS_GroupBlue_UltraLight);
        UIDHelper.init(new ThemeConfiguration(this, ColorRange.GROUP_BLUE, NavigationColor.BRIGHT,
                ContentColor.ULTRA_LIGHT));
        UIDHelper.injectCalligraphyFonts();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_activity_demo);
        launchEWSFragmentUApp();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void launchEWSFragmentUApp() {
        String selectedOption = getIntent().getExtras().getString("SelectedConfig");
        AppInfraInterface appInfra = new AppInfra.Builder().build(getApplicationContext());
        EWSInterface ewsInterface = new EWSInterface();
        ewsInterface.init(createUappDependencies(appInfra, createProductMap(), isDefaultValueSelected(selectedOption)), new UappSettings(getApplicationContext()));

        FragmentLauncher fragmentLauncher = new FragmentLauncher
                (this, R.id.fragmentContainerDemo, this);
        ewsInterface.launch(fragmentLauncher, new EWSLauncherInput());
    }

    @Override
    public void updateActionBar(int i, boolean b) {

    }

    @Override
    public void updateActionBar(String s, boolean b) {

    }
}
