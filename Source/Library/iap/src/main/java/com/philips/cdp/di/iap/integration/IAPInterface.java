/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.integration;

import com.philips.cdp.di.iap.iapHandler.IAPExposedAPI;
import com.philips.cdp.di.iap.session.IAPListener;
import com.philips.cdp.registration.User;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

public class IAPInterface implements UappInterface, IAPExposedAPI {
    protected IAPExposedAPI mImplementationHandler;
    protected IAPHandler mIAPHandler;
    protected IAPSettings mIAPSettings;
    private User mUser;

    @Override
    public void init(UappDependencies uappDependencies, UappSettings uappSettings) {
        IAPDependencies mIAPDependencies = (IAPDependencies) uappDependencies;
        mIAPSettings = (IAPSettings) uappSettings;
        mIAPHandler = new IAPHandler(mIAPDependencies, mIAPSettings);
        mImplementationHandler = mIAPHandler.getExposedAPIImplementor();
    }

    @Override
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput) throws RuntimeException {
        mUser = new User(mIAPSettings.getContext());// User can be inject as dependencies
        if (mUser.isUserSignIn()) {
            if (!mIAPSettings.isUseLocalData())
                launchHybris(uiLauncher, (IAPLaunchInput) uappLaunchInput);
            else
                mIAPHandler.launchIAP(uiLauncher, (IAPLaunchInput) uappLaunchInput);
        } else {
            throw new RuntimeException("User is not logged in.");// Confirm the behaviour on error Callback
        }
    }

    protected void launchHybris(UiLauncher uiLauncher, IAPLaunchInput pIAPLaunchInput) {
        if (mIAPHandler.isStoreInitialized(mIAPSettings.getContext()))
            mIAPHandler.launchIAP(uiLauncher, pIAPLaunchInput);
        else
            mIAPHandler.initIAP(uiLauncher, pIAPLaunchInput);
    }

    @Override
    public void getProductCartCount(IAPListener iapListener) {
        mUser = new User(mIAPSettings.getContext());
        if (mUser.isUserSignIn())
            mImplementationHandler.getProductCartCount(iapListener);
        else throw new RuntimeException("User is not logged in.");
    }

    @Override
    public void getCompleteProductList(IAPListener iapListener) {
        mUser = new User(mIAPSettings.getContext());
        if (mUser.isUserSignIn())
            mImplementationHandler.getCompleteProductList(iapListener);
        else throw new RuntimeException("User is not logged in.");
    }
}
