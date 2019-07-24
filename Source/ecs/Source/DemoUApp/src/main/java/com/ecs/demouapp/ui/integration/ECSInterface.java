
package com.ecs.demouapp.ui.integration;

import android.content.Context;
import android.net.ConnectivityManager;


import com.ecs.demouapp.R;
import com.ecs.demouapp.ui.container.CartModelContainer;
import com.ecs.demouapp.ui.iapHandler.ECSExposedAPI;
import com.ecs.demouapp.ui.utils.ECSConstant;
import com.ecs.demouapp.ui.utils.ECSUtility;
import com.ecs.demouapp.ui.utils.NetworkUtility;
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
public class ECSInterface implements UappInterface, ECSExposedAPI {
    protected ECSHandler mIAPHandler;
    protected ECSSettings mIAPSettings;
    private ECSConfigure mIapServiceDiscoveryWrapper;
    private UserDataInterface mUserDataInterface;

    /**
     * API to initialize IAP
     *
     * @param uappDependencies pass instance of UappDependencies
     * @param uappSettings     pass instance of UappSettings
     * @since 1.0.0
     */
    @Override
    public void init(UappDependencies uappDependencies, UappSettings uappSettings) {
        ECSDependencies ECSDependencies = (ECSDependencies) uappDependencies;
        mUserDataInterface = ECSDependencies.getUserDataInterface();
        if (null == mUserDataInterface)
            throw new RuntimeException("UserDataInterface is not injected in IAPDependencies.");
        ECSUtility.getInstance().setUserDataInterface(mUserDataInterface);
        ECSUtility.getInstance().setAppName(uappDependencies.getAppInfra().getAppIdentity().getAppName());
        ECSUtility.getInstance().setLocaleTag(uappDependencies.getAppInfra().getInternationalization().getUILocaleString());
        mIAPSettings = (ECSSettings) uappSettings;
        mIAPHandler = new ECSHandler(mIAPSettings);
        mIapServiceDiscoveryWrapper = new ECSConfigure(mIAPSettings);
    }

    /**
     * API to launch IAP
     *
     * @param uiLauncher      pass instance of UiLauncher
     * @param uappLaunchInput pass instance of UappLaunchInput
     * @throws RuntimeException
     * @since 1.0.0
     */
    @Override
    public void launch(UiLauncher uiLauncher, UappLaunchInput uappLaunchInput) throws RuntimeException {

        //This is added to clear pagination data from app memory . This should be taken in tech debt .
        CartModelContainer.getInstance().clearProductList();
        ECSUtility.getInstance().resetPegination();

        ConnectivityManager connectivityManager
                = (ConnectivityManager) mIAPSettings.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!NetworkUtility.getInstance().isNetworkAvailable(connectivityManager)) {
            ((ECSLaunchInput) uappLaunchInput).getIapListener().onFailure(ECSConstant.IAP_ERROR_NO_CONNECTION);
            throw new RuntimeException(mIAPSettings.getContext().getString(R.string.iap_no_internet));// Confirm the behaviour on error Callback
        }
        mIapServiceDiscoveryWrapper.configureECS(uiLauncher, mIAPHandler, (ECSLaunchInput) uappLaunchInput, null, null);
    }

    /**
     * IAPInterface getProductCartCount will fetch the cart count
     *
     * @param iapListener instance of IAPListener
     * @since 1.0.0
     */
    @Override
    public void getProductCartCount(ECSListener iapListener) {
        if (mUserDataInterface != null && mUserDataInterface.getUserLoggedInState().ordinal() >= UserLoggedInState.PENDING_HSDP_LOGIN.ordinal()){
            mIapServiceDiscoveryWrapper.configureECS(null, mIAPHandler, null, iapListener, "productCartCount");
       } else {
            iapListener.onFailure(ECSConstant.IAP_ERROR_AUTHENTICATION_FAILURE);
        }
    }

    /**
     * IAPInterface fetch complete product ctn list from backend
     *
     * @param iapListener instance of IAPListener
     * @since 1.0.0
     */
    @Override
    public void getCompleteProductList(ECSListener iapListener) {
        mIapServiceDiscoveryWrapper.configureECS(null, mIAPHandler, null, iapListener, "completeProductList");
    }

    /**
     * IAPInterface isCartVisible method will inform the uApp for cart visibility. It’s an optional method
     *
     * @param iapListener instance of IAPListener
     * @return
     * @since 1.0.0
     */
    @Override
    public void isCartVisible(ECSListener iapListener) {
        if (mIAPHandler != null && mUserDataInterface != null && mUserDataInterface.getUserLoggedInState().ordinal() >= UserLoggedInState.PENDING_HSDP_LOGIN.ordinal()) {
             mIapServiceDiscoveryWrapper.getCartVisiblityByConfigUrl(iapListener, mIAPHandler);
        } else {
            iapListener.onSuccess(false);
            iapListener.onFailure(ECSConstant.IAP_ERROR_AUTHENTICATION_FAILURE);
        }

    }
}



