package com.philips.platform.prdemoapp;

import android.app.Application;
import androidx.multidex.MultiDex;

import com.philips.cdp.prodreg.launcher.PRDependencies;
import com.philips.cdp.prodreg.launcher.PRInterface;
import com.philips.cdp.prodreg.launcher.PRSettings;
import com.philips.cdp.prodreg.launcher.PRUiHelper;
import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.URDependancies;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URSettings;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uid.thememanager.UIDHelper;

public class ProductRegistrationApplication extends Application {

    private static AppInfraInterface mAppInfraInterface;
    final String AI = "appinfra";
    private final String UR = "UserRegistration";

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        UIDHelper.injectCalligraphyFonts();
        initAppInfra();
        initProductRegistration();
        initRegistration(Configuration.STAGING);
        RLog.enableLogging();
    }

    private void initProductRegistration() {
        PRDependencies prDependencies = new PRDependencies(mAppInfraInterface);
        PRSettings prSettings = new PRSettings(getApplicationContext());
        new PRInterface().init(prDependencies, prSettings);
        mAppInfraInterface.getTagging().setPreviousPage("demoapp:");
    }

    /**
     * For doing dynamic initialisation Of User registration
     *
     * @param configuration The environment ype as required by UR
     */
    public void initRegistration(Configuration configuration) {


        URDependancies urDependancies = new URDependancies(mAppInfraInterface);
        URSettings urSettings = new URSettings(this);
        URInterface urInterface = new URInterface();
        urInterface.init(urDependancies, urSettings);

    }



    @SuppressWarnings("deprecation")
    private void initAppInfra() {
        mAppInfraInterface = new AppInfra.Builder().build(getApplicationContext());
        PRUiHelper.getInstance().setAppInfraInstance(mAppInfraInterface);
    }

}
