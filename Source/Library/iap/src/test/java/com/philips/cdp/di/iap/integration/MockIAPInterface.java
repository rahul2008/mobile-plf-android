package com.philips.cdp.di.iap.integration;

import com.philips.cdp.di.iap.session.IAPListener;
import com.philips.cdp.registration.User;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

/**
 * Created by indrajitkumar on 22/09/16.
 */

public class MockIAPInterface extends IAPInterface {
    @Override
    public void init(UappDependencies uappDependencies, UappSettings uappSettings) {
        IAPDependencies mIAPDependencies = (IAPDependencies) uappDependencies;
        mIapSettings = (IAPSettings) uappSettings;
        iapHandler = new IAPHandler(mIAPDependencies, mIapSettings);
        mImplementationHandler = iapHandler.getExposedAPIImplementor(mIapSettings);
    }

    @Override
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput) {
        if (!mIapSettings.isUseLocalData())
            launchHybris(uiLauncher, (IAPLaunchInput) uappLaunchInput);
        else
            iapHandler.launchIAP(uiLauncher, (IAPLaunchInput) uappLaunchInput);
    }

    @Override
    public void getProductCartCount(IAPListener iapListener) {
    }

    @Override
    public void getCompleteProductList(IAPListener iapListener) {
    }
}
