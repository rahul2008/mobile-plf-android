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
    private IAPExposedAPI mImplementationHandler;
    private IAPHandler iapHandler;
    private User mUser;
    private IAPSettings mIapSettings;

    @Override
    public void init(UappDependencies uappDependencies, UappSettings uappSettings) {
        IAPDependencies mIAPDependencies = (IAPDependencies) uappDependencies;
        mIapSettings = (IAPSettings) uappSettings;
        iapHandler = new IAPHandler(mIAPDependencies, mIapSettings);
        iapHandler.initTaggingLogging(mIAPDependencies);
        iapHandler.initIAP(mIapSettings);
        mImplementationHandler = iapHandler.getExposedAPIImplementor(mIapSettings);
    }

    @Override
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput) throws RuntimeException {
        mUser = new User(mIapSettings.getContext());
        if (mUser.isUserSignIn()) {
            IAPLaunchInput mLaunchInput = (IAPLaunchInput) uappLaunchInput;
            if (iapHandler.isStoreInitialized()) iapHandler.launchIAP(uiLauncher, mLaunchInput);
            else
                iapHandler.initIAP(uiLauncher, mLaunchInput, ((IAPLaunchInput) uappLaunchInput).getIapListener());
        } else {
            throw new RuntimeException("User is not logged in.");
        }
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
