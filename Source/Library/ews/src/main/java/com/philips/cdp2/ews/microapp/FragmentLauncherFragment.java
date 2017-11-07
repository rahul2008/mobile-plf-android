package com.philips.cdp2.ews.microapp;

import com.philips.cdp2.ews.navigation.Navigator;

import javax.inject.Inject;

/**
 * Created by architsureja on 07/11/17.
 */

public class FragmentLauncherFragment {

    @Inject
    Navigator navigator;

    FragmentLauncherFragment() {
        EWSDependencyProvider.getInstance().getEwsComponent().inject(this);
    }

    public void show(){
        navigator.navigateToGettingStartedScreen();
    }
}
