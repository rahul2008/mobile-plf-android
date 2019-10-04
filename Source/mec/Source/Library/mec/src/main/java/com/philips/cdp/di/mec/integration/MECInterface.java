
package com.philips.cdp.di.mec.integration;

import android.content.Context;
import android.net.ConnectivityManager;

import com.philips.cdp.di.mec.R;
import com.philips.cdp.di.mec.container.CartModelContainer;
import com.philips.cdp.di.mec.mecHandler.MECExposedAPI;
import com.philips.cdp.di.mec.screens.catalog.ECSServiceRepository;
import com.philips.cdp.di.mec.utils.MECConstant;
import com.philips.cdp.di.mec.utils.MECUtility;
import com.philips.cdp.di.mec.utils.NetworkUtility;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;
import com.philips.platform.pif.DataInterface.USR.enums.UserLoggedInState;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

import static com.philips.cdp.di.mec.integration.MECLaunchInput.MECFlows.MEC_SHOPPING_CART_VIEW;

/**
 * IAPInterface is the public class for any proposition to consume InAppPurchase micro app. Its the starting initialization point.
 * @since 1.0.0
 */
public class MECInterface implements UappInterface, MECExposedAPI {
    protected MECHandler mMECHandler;
    protected MECSettings mMECSettings;
    private MECServiceDiscoveryWrapper mMecServiceDiscoveryWrapper;
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
        MECDependencies MECDependencies = (MECDependencies) uappDependencies;
        mUserDataInterface = MECDependencies.getUserDataInterface();

        ECSServiceRepository.INSTANCE.appInfra = new AppInfra.Builder().build(uappDependencies.getAppInfra().getAppInfraContext());

        if (null == mUserDataInterface)
            throw new RuntimeException("UserDataInterface is not injected in IAPDependencies.");

        MECUtility.getInstance().setUserDataInterface(mUserDataInterface);
        MECUtility.getInstance().setAppName(uappDependencies.getAppInfra().getAppIdentity().getAppName());
        MECUtility.getInstance().setLocaleTag(uappDependencies.getAppInfra().getInternationalization().getUILocaleString());
        mMECSettings = (MECSettings) uappSettings;
        mMECHandler = new MECHandler(MECDependencies,mMECSettings);
        mMECHandler.initPreRequisite();
        mMecServiceDiscoveryWrapper = new MECServiceDiscoveryWrapper(mMECSettings);
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

        /*if( ((MECLaunchInput) uappLaunchInput).mLandingView!=MEC_SHOPPING_CART_VIEW) {
            CartModelContainer.getInstance().clearProductList();
        }*/
        MECUtility.getInstance().resetPegination();

        ConnectivityManager connectivityManager
                = (ConnectivityManager) mMECSettings.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        mMecServiceDiscoveryWrapper.getLocaleFromServiceDiscovery(uiLauncher, mMECHandler, (MECLaunchInput) uappLaunchInput, null, null);
    }

    /**
     * IAPInterface getProductCartCount will fetch the cart count
     *
     * @param mecListener instance of IAPListener
     * @since 1.0.0
     */
    @Override
    public void getProductCartCount(MECListener mecListener) {
        if (mUserDataInterface != null && mUserDataInterface.getUserLoggedInState().ordinal() >= UserLoggedInState.PENDING_HSDP_LOGIN.ordinal()){
            mMecServiceDiscoveryWrapper.getLocaleFromServiceDiscovery(null, mMECHandler, null, mecListener, "productCartCount");
        } else {
            mecListener.onFailure(MECConstant.MEC_ERROR_AUTHENTICATION_FAILURE);
        }
    }

    /**
     * MECInterface fetch complete product ctn list from backend
     *
     * @param mecListener instance of IAPListener
     * @since 1.0.0
     */
    @Override
    public void getCompleteProductList(MECListener mecListener) {
        mMecServiceDiscoveryWrapper.getLocaleFromServiceDiscovery(null, mMECHandler, null, mecListener, "completeProductList");
    }

    /**
     * MECInterface isCartVisible method will inform the uApp for cart visibility. It’s an optional method
     *
     * @param mecListener instance of IAPListener
     * @return
     * @since 1.0.0
     */
    @Override
    public void isCartVisible(MECListener mecListener) {
        if (mMECHandler != null && mUserDataInterface != null && mUserDataInterface.getUserLoggedInState().ordinal() >= UserLoggedInState.PENDING_HSDP_LOGIN.ordinal()) {
             mMecServiceDiscoveryWrapper.getCartVisiblityByConfigUrl(mecListener, mMECHandler);
        } else {
            mecListener.onSuccess(false);
            mecListener.onFailure(MECConstant.MEC_ERROR_AUTHENTICATION_FAILURE);
        }

    }
}



