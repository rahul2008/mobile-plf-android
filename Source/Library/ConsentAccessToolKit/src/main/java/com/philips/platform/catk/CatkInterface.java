package com.philips.platform.catk;


import com.philips.platform.catk.injection.AppInfraModule;
import com.philips.platform.catk.injection.CatkComponent;
import com.philips.platform.catk.injection.CatkModule;
import com.philips.platform.catk.injection.DaggerCatkComponent;
import com.philips.platform.catk.utils.CatkLogger;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

public class CatkInterface implements UappInterface {


    /**
     * Launches the CswInterface interface. The component can be launched either with an ActivityLauncher or a FragmentLauncher.
     *
     * @param uiLauncher      - ActivityLauncher or FragmentLauncher
     * @param uappLaunchInput - CswLaunchInput
     */
    @Override
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput) {
        /**nothing to laucnh*/
    }


    /**
     * Entry point for User registration. Please make sure no User registration components are being used before CswInterface$init.
     *
     * @param uappDependencies - With an AppInfraInterface instance.
     * @param uappSettings     - With an application provideAppContext.
     */
    @Override
    public void init(UappDependencies uappDependencies, UappSettings uappSettings) {

        catkComponent = initDaggerCoponents(uappDependencies, uappSettings);
        CatkLogger.init();
        CatkLogger.enableLogging();
    }

    private CatkComponent initDaggerCoponents(UappDependencies uappDependencies, UappSettings uappSettings) {
        return DaggerCatkComponent.builder()
                .catkModule(new CatkModule(uappSettings.getContext()))
                .appInfraModule(new AppInfraModule(uappDependencies.getAppInfra())).build();
    }

    public static CatkComponent getCatkComponent() {
        return catkComponent;
    }

    private static CatkComponent catkComponent;


}
