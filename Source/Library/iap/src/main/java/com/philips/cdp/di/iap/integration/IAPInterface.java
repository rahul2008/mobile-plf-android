/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.integration;

import com.philips.cdp.di.iap.iapHandler.IAPExposedAPI;
import com.philips.cdp.registration.User;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

public class IAPInterface implements UappInterface, IAPExposedAPI {
    protected IAPHandler mIAPHandler;
    protected IAPSettings mIAPSettings;
    private User mUser;
    private IAPServiceDiscoveryWrapper mIapServiceDiscoveryWrapper;

    @Override
    public void init(UappDependencies uappDependencies, UappSettings uappSettings) {
        IAPDependencies mIAPDependencies = (IAPDependencies) uappDependencies;
        mIAPSettings = (IAPSettings) uappSettings;
        mIAPHandler = new IAPHandler(mIAPDependencies, mIAPSettings);
        mIAPHandler.initPreRequisite();
        mIapServiceDiscoveryWrapper = new IAPServiceDiscoveryWrapper(mIAPSettings);
    }

    @Override
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput) throws RuntimeException {
        mUser = new User(mIAPSettings.getContext());// User can be inject as dependencies
        if (mUser.isUserSignIn()) {
            mIapServiceDiscoveryWrapper.getLocaleFromServiceDiscovery(uiLauncher, mIAPHandler, (IAPLaunchInput) uappLaunchInput);
//            if (!mIAPSettings.isUseLocalData() && (!mIAPHandler.isStoreInitialized(mIAPSettings.getContext()))) {
//                mIAPHandler.initIAP(uiLauncher, (IAPLaunchInput) uappLaunchInput);
//            } else {
//                mIAPHandler.launchIAP(uiLauncher, (IAPLaunchInput) uappLaunchInput);
//            }
        } else {
            throw new RuntimeException("User is not logged in.");// Confirm the behaviour on error Callback
        }
    }

    @Override
    public void getProductCartCount(IAPListener iapListener) {
        mUser = new User(mIAPSettings.getContext());
        if (mUser.isUserSignIn())
            mIAPHandler.getExposedAPIImplementor().getProductCartCount(iapListener);
        else throw new RuntimeException("User is not logged in.");
    }

    @Override
    public void getCompleteProductList(IAPListener iapListener) {
        mUser = new User(mIAPSettings.getContext());
        if (mUser.isUserSignIn())
        {
            mIapServiceDiscoveryWrapper.getLocaleFromServiceDiscovery(mIAPHandler, iapListener);
//            mIAPHandler.initIAPRequisite();
//            mIAPHandler.getExposedAPIImplementor().getCompleteProductList(iapListener);
        }
        else throw new RuntimeException("User is not logged in.");
    }
}
