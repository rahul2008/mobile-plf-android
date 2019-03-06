
package com.philips.cdp.di.iap.integration;

import android.content.Context;
import android.net.ConnectivityManager;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.iapHandler.IAPExposedAPI;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;
import com.philips.platform.pif.DataInterface.USR.enums.UserLoggedInState;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

/**
 * IAPInterface is the public class for any proposition to consume InAppPurchase micro app. Its the starting initialization point.
 * @since 1.0.0
 */
public class IAPInterface implements UappInterface, IAPExposedAPI {
    protected IAPHandler mIAPHandler;
    protected IAPSettings mIAPSettings;
    private IAPServiceDiscoveryWrapper mIapServiceDiscoveryWrapper;
    private UserDataInterface mUserDataInterface;
    private IAPLaunchInput iapLaunchInput;

    /**
     * API to initialize IAP
     * @param uappDependencies  pass instance of UappDependencies
     * @param uappSettings  pass instance of UappSettings
     * @since 1.0.0
     */
    @Override
    public void init(UappDependencies uappDependencies, UappSettings uappSettings) {
        IAPDependencies mIAPDependencies = (IAPDependencies) uappDependencies;
        mIAPSettings = (IAPSettings) uappSettings;
        mIAPHandler = new IAPHandler(mIAPDependencies, mIAPSettings);
        mIAPHandler.initPreRequisite();
        mIapServiceDiscoveryWrapper = new IAPServiceDiscoveryWrapper(mIAPSettings);
    }

    /**
     * API to launch IAP
     * @param uiLauncher  pass instance of UiLauncher
     * @param uappLaunchInput  pass instance of UappLaunchInput
     * @throws RuntimeException
     * @since 1.0.0
     */
    @Override
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput) throws RuntimeException {
        //mUser = new User(mIAPSettings.getContext());// User can be inject as dependencies
        iapLaunchInput = (IAPLaunchInput) uappLaunchInput;
        if(iapLaunchInput != null && iapLaunchInput.getUserDataInterface() != null)
            mUserDataInterface = ((IAPLaunchInput) uappLaunchInput).getUserDataInterface();
        else
            return;

        if (mUserDataInterface.getUserLoggedInState() == UserLoggedInState.USER_LOGGED_IN) {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) mIAPSettings.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (!NetworkUtility.getInstance().isNetworkAvailable(connectivityManager)) {
                throw new RuntimeException(mIAPSettings.getContext().getString(R.string.iap_no_internet));// Confirm the behaviour on error Callback
            }
            mIapServiceDiscoveryWrapper.getLocaleFromServiceDiscovery(uiLauncher, mIAPHandler, (IAPLaunchInput) uappLaunchInput, null, null);
        } else {
            throw new RuntimeException("User is not logged in.");// Confirm the behaviour on error Callback
        }
    }

    /**
     * IAPInterface getProductCartCount will fetch the cart count
     * @param iapListener  instance of IAPListener
     * @since 1.0.0
     */
    @Override
    public void getProductCartCount(IAPListener iapListener) {
        if(mUserDataInterface == null)
            return;
        if (mUserDataInterface.getUserLoggedInState() == UserLoggedInState.USER_LOGGED_IN)
            mIapServiceDiscoveryWrapper.getLocaleFromServiceDiscovery(null, mIAPHandler, iapLaunchInput, iapListener, "productCartCount");
        else throw new RuntimeException("User is not logged in.");
    }

    /**
     * IAPInterface fetch complete product ctn list from backend
     * @param iapListener  instance of IAPListener
     * @since 1.0.0
     */
    @Override
    public void getCompleteProductList(IAPListener iapListener) {
        if(mUserDataInterface == null)
            return ;

        if (mUserDataInterface.getUserLoggedInState() == UserLoggedInState.USER_LOGGED_IN) {
            mIapServiceDiscoveryWrapper.getLocaleFromServiceDiscovery(null, mIAPHandler, iapLaunchInput, iapListener, "completeProductList");
        } else throw new RuntimeException("User is not logged in.");
    }

    /**
     * IAPInterface isCartVisible method will inform the uApp for cart visibility. It’s an optional method
     * @param iapListener  instance of IAPListener
     * @return
     * @since 1.0.0
     */
    @Override
    public boolean isCartVisible(IAPListener iapListener) {
        //mUser = new User(mIAPSettings.getContext());
        if(mUserDataInterface == null) {
            return false;
        }
        if ( mUserDataInterface.getUserLoggedInState() == UserLoggedInState.USER_LOGGED_IN) {
            return mIAPHandler != null && mIapServiceDiscoveryWrapper.getCartVisiblityByConfigUrl(iapListener, mIAPHandler,mUserDataInterface);
        } else throw new RuntimeException("User is not logged in.");
    }
}
