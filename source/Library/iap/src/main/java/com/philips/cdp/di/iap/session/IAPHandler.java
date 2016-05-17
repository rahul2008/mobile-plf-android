/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.session;

import android.content.Context;

import com.philips.cdp.di.iap.core.IAPExposedAPI;
import com.philips.cdp.di.iap.core.NetworkEssentials;
import com.philips.cdp.di.iap.core.NetworkEssentialsFactory;
import com.philips.cdp.di.iap.hybris.HybrisHandler;

public class IAPHandler implements IAPExposedAPI {

    private IAPExposedAPI mImplementationHandler;

    private IAPHandler() {
    }

    public static IAPHandler init(Context context, IAPSettings config) {
        IAPHandler handler = new IAPHandler();
        handler.mImplementationHandler = handler.getExposedAPIImplementor(context, config);
        handler.initHybrisDelegate(context, config);
        return handler;
    }

    @Override
    public void launchIAP(int landingView, String ctnNumber, IAPHandlerListener listener) {
        mImplementationHandler.launchIAP(landingView, ctnNumber, listener);
    }

    @Override
    public void getProductCartCount(final IAPHandlerListener iapHandlerListener) {
        mImplementationHandler.getProductCartCount(iapHandlerListener);
    }


    private IAPExposedAPI getExposedAPIImplementor(Context context, IAPSettings settings) {
        IAPExposedAPI api = null;
        if (settings.isUseLocalData()) {
            //Still need to implement
        } else {
            api = new HybrisHandler(context, settings);
        }
        return api;
    }

    private void initHybrisDelegate(Context context, IAPSettings config) {
        int requestCode = getNetworkEssentialReqeustCode(config.isUseLocalData());
        NetworkEssentials essentials = NetworkEssentialsFactory.getNetworkEssentials(requestCode);
        HybrisDelegate.getDelegateWithNetworkEssentials(context, essentials);
    }


    private int getNetworkEssentialReqeustCode(boolean useLocalData) {
        int requestCode = NetworkEssentialsFactory.LOAD_HYBRIS_DATA;
        if (useLocalData) {
            requestCode = NetworkEssentialsFactory.LOAD_LOCAL_DATA;
        }

        return requestCode;
    }
}