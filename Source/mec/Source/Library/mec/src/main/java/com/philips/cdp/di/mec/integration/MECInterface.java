
package com.philips.cdp.di.mec.integration;
import com.philips.cdp.di.mec.screens.catalog.ECSCatalogRepository;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

/**
 * MECInterface is the public class for any proposition to consume MEC micro app. Its the starting initialization point.
 * @since 1.0.0
 */
public class MECInterface implements UappInterface {
    protected MECSettings mMECSettings;
    private UappDependencies mUappDependencies;
    private UserDataInterface mUserDataInterface;

    /**
     * @param uappDependencies Object of UappDependencies
     * @param uappSettings     Object of UppSettings
     */
    @Override
    public void init(UappDependencies uappDependencies, UappSettings uappSettings) {
        MECDependencies MECDependencies = (MECDependencies) uappDependencies;
        mUserDataInterface = MECDependencies.getUserDataInterface();


        if (null == mUserDataInterface)
            throw new RuntimeException("UserDataInterface is not injected in IAPDependencies.");

        mMECSettings = (MECSettings) uappSettings;
        mUappDependencies = uappDependencies;
    }

    /**
     * @param uiLauncher      Object of UiLauncher
     * @param uappLaunchInput Object of  UappLaunchInput
     * @throws RuntimeException
     */
    @Override
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput) throws RuntimeException {
        MECHandler mecHandler = new MECHandler((MECDependencies)mUappDependencies,mMECSettings,uiLauncher,(MECLaunchInput) uappLaunchInput);
        mecHandler.launchMEC();
    }

    /**
     * @param mecListener
     */
    public void getProductCartCount(MECListener mecListener) {
    }

    public void getCompleteProductList(MECListener mecListener) {
    }

}



