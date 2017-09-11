package com.philips.cdp.di.iap.integration;

import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

public class MockIAPInterface extends IAPInterface {

    private MockIAPHandler mMockIAPHandler;

    public MockIAPInterface() {

    }

    @Override
    public void init(UappDependencies uappDependencies, UappSettings uappSettings) {
        IAPDependencies mIAPDependencies = (IAPDependencies) uappDependencies;
        mIAPSettings = (IAPSettings) uappSettings;
        mMockIAPHandler = new MockIAPHandler(mIAPDependencies, mIAPSettings);
    }

    @Override
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput) {
        if (!mIAPSettings.isUseLocalData() && (!mMockIAPHandler.isStoreInitialized(mIAPSettings.getContext()))) {
            mMockIAPHandler.initIAP(uiLauncher, (IAPLaunchInput) uappLaunchInput);
        } else {
            mMockIAPHandler.launchIAP(uiLauncher, (IAPLaunchInput) uappLaunchInput);
        }
    }

    @Override
    public void getProductCartCount(IAPListener iapListener) {
    }

    @Override
    public void getCompleteProductList(IAPListener iapListener) {
    }

}
