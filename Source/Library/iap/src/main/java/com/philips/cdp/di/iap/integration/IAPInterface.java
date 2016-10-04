/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.integration;

import com.philips.cdp.di.iap.core.IAPExposedAPI;
import com.philips.cdp.di.iap.session.IAPListener;
import com.philips.cdp.registration.User;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

public class IAPInterface implements UappInterface, IAPExposedAPI {
    protected IAPExposedAPI mImplementationHandler;
    protected IAPHandler iapHandler;
    private User mUser;
    protected IAPSettings mIapSettings;

    @Override
    public void init(UappDependencies uappDependencies, UappSettings uappSettings) {
        IAPDependencies mIAPDependencies = (IAPDependencies) uappDependencies;
        mIapSettings = (IAPSettings) uappSettings;
        iapHandler = new IAPHandler(mIAPDependencies, mIapSettings);
        iapHandler.initTaggingLogging();
        iapHandler.initIAP();
        mImplementationHandler = iapHandler.getExposedAPIImplementor(mIapSettings);
    }

    @Override
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput) throws RuntimeException {
        mUser = new User(mIapSettings.getContext());// User can be inject as dependencies
        if (mUser.isUserSignIn()) {
            if (!mIapSettings.isUseLocalData())
                launchHybris(uiLauncher, (IAPLaunchInput) uappLaunchInput);
            else
                iapHandler.launchIAP(uiLauncher, (IAPLaunchInput) uappLaunchInput);
        } else {
            throw new RuntimeException("User is not logged in.");// Confirm the behaviour on error Callback
        }
    }

    protected void launchHybris(UiLauncher uiLauncher, IAPLaunchInput uappLaunchInput) {
        IAPLaunchInput mLaunchInput = uappLaunchInput;
        if (iapHandler.isStoreInitialized(mIapSettings.getContext())) iapHandler.launchIAP(uiLauncher, mLaunchInput);
        else
            iapHandler.initIAP(uiLauncher, mLaunchInput);
    }

    @Override
    public void getProductCartCount(IAPListener iapListener) {
        mUser = new User(mIapSettings.getContext());
        if (mUser.isUserSignIn())
            mImplementationHandler.getProductCartCount(iapListener);
        else throw new RuntimeException("User is not logged in.");
    }

    @Override
    public void getCompleteProductList(IAPListener iapListener) {
        mUser = new User(mIapSettings.getContext());
        if (mUser.isUserSignIn())
            mImplementationHandler.getCompleteProductList(iapListener);
        else throw new RuntimeException("User is not logged in.");
    }
}
