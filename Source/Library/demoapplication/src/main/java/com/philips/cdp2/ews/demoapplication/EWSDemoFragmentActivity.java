package com.philips.cdp2.ews.demoapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.philips.cdp2.ews.microapp.EWSInterface;
import com.philips.cdp2.ews.microapp.EWSLauncherInput;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.uappinput.UappSettings;

/*
 * Copyright (c) Mobiquityinc, 2017.
 * All rights reserved.
 */
public class EWSDemoFragmentActivity extends EWSDemoBaseActivity implements ActionBarListener {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_activity_demo);
        launchEWSFragmentUApp();
    }

    private void launchEWSFragmentUApp() {
        String selectedOption = getIntent().getExtras().getString("SelectedConfig");
        AppInfraInterface appInfra = new AppInfra.Builder().build(getApplicationContext());
        EWSInterface ewsInterface = new EWSInterface();
        ewsInterface.init(createUappDependencies(appInfra, createProductMap(), isDefaultValueSelected(selectedOption)), new UappSettings(getApplicationContext()));
        //ewsInterface.launch(new ActivityLauncher(SCREEN_ORIENTATION_PORTRAIT, null,-1, null), new EWSLauncherInput());

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
