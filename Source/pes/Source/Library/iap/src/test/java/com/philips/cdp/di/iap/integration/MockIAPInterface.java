/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.integration;

import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

public class MockIAPInterface extends IAPInterface {

    private IAPHandler iapHandler;

    @Override
    public void init(UappDependencies uappDependencies, UappSettings uappSettings) {
        IAPDependencies mIAPDependencies = (IAPDependencies) uappDependencies;
        mIAPSettings = (IAPSettings) uappSettings;
        iapHandler = new IAPHandler(mIAPDependencies, mIAPSettings);
    }

    @Override
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput) {
        if (!mIAPSettings.isUseLocalData() && (!iapHandler.isStoreInitialized(mIAPSettings.getContext()))) {
            iapHandler.initIAP(uiLauncher, (IAPLaunchInput) uappLaunchInput);
        } else {
            iapHandler.launchIAP(uiLauncher, (IAPLaunchInput) uappLaunchInput);
        }
    }

    @Override
    public void getProductCartCount(IAPListener iapListener) {
    }

    @Override
    public void getCompleteProductList(IAPListener iapListener) {
    }

}
