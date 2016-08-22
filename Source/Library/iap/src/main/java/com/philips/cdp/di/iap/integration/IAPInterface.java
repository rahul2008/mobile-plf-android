/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.integration;

import com.philips.cdp.di.iap.core.IAPExposedAPI;
import com.philips.cdp.di.iap.session.IAPHandlerProductListListener;
import com.philips.cdp.di.iap.session.IAPListener;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

public class IAPInterface implements UappInterface, IAPExposedAPI{
    private IAPExposedAPI mImplementationHandler;
    private IAPHandler iapHandler;

    @Override
    public void init(UappDependencies uappDependencies, UappSettings uappSettings) {
        IAPDependencies mIAPDependencies = (IAPDependencies) uappDependencies;
        IAPSettings mIapSettings = (IAPSettings) uappSettings;
        iapHandler = new IAPHandler(mIAPDependencies, mIapSettings);
        iapHandler.initTaggingLogging(mIAPDependencies);
        iapHandler.initIAP(mIapSettings);
        mImplementationHandler = iapHandler.getExposedAPIImplementor(mIapSettings);
    }

    @Override
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput) throws RuntimeException {
        //Check for user signed in or not, in case not return exception
        IAPLaunchInput mLaunchInput = (IAPLaunchInput) uappLaunchInput;
        if (iapHandler.isStoreInitialized()) iapHandler.launchIAP(uiLauncher, mLaunchInput);
        else
            iapHandler.initIAP(uiLauncher, mLaunchInput, ((IAPLaunchInput) uappLaunchInput).getIapListener());
    }

    @Override
    public void getProductCartCount(IAPListener iapListener) {
        mImplementationHandler.getProductCartCount(iapListener);
    }

    @Override
    public void getCompleteProductList(IAPListener iapListener) {
        mImplementationHandler.getCompleteProductList(iapListener);
    }

}
