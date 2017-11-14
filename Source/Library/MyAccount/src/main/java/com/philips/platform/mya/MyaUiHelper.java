/*
 *
 *  * Copyright (c) Koninklijke Philips N.V. 2017
 *  * All rights are reserved. Reproduction or dissemination in whole or in part
 *  * is prohibited without the prior written consent of the copyright holder.
 *
 */

package com.philips.platform.mya;


import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.mya.interfaces.MyaListener;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uid.thememanager.ThemeConfiguration;

public class MyaUiHelper {

    private static MyaUiHelper myaUiHelper;

    private AppInfra appInfra;
    private MyaListener myaListener;
    private FragmentLauncher fragmentLauncher;
    private ThemeConfiguration themeConfiguration;

    private MyaUiHelper() {
    }

    public static MyaUiHelper getInstance() {
        if (myaUiHelper == null) {
            synchronized (MyaUiHelper.class) {
                if (myaUiHelper == null) {
                    myaUiHelper = new MyaUiHelper();
                }
            }
        }
        return myaUiHelper;
    }

    public AppInfra getAppInfra() {
        return appInfra;
    }

    public void setAppInfra(AppInfra appInfra) {
        this.appInfra = appInfra;
    }

    public MyaListener getMyaListener() {
        return myaListener;
    }

    public void setMyaListener(MyaListener myaListener) {
        this.myaListener = myaListener;
    }

    public FragmentLauncher getFragmentLauncher() {
        return fragmentLauncher;
    }

    public void setFragmentLauncher(FragmentLauncher fragmentLauncher) {
        this.fragmentLauncher = fragmentLauncher;
    }

    public ThemeConfiguration getThemeConfiguration() {
        return themeConfiguration;
    }

    public void setThemeConfiguration(ThemeConfiguration themeConfiguration) {
        this.themeConfiguration = themeConfiguration;
    }
}
