/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.session;

import android.content.Context;

import com.philips.cdp.di.iap.core.IAPExposedAPI;
import com.philips.cdp.di.iap.hybris.HybrisHandler;

public class IAPHandler implements IAPExposedAPI {

    private IAPExposedAPI mImplementationHandler;

    private IAPHandler() {
    }

    public static IAPHandler init(Context context, IAPSettings config) {
        IAPHandler handler = new IAPHandler();
        handler.mImplementationHandler = new HybrisHandler(context, config);
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
}