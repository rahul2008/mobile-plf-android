/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.session;

import android.content.Context;

import com.philips.cdp.di.iap.applocal.AppLocalHandler;
import com.philips.cdp.di.iap.core.ControllerFactory;
import com.philips.cdp.di.iap.core.IAPExposedAPI;
import com.philips.cdp.di.iap.core.NetworkEssentials;
import com.philips.cdp.di.iap.core.NetworkEssentialsFactory;
import com.philips.cdp.di.iap.hybris.HybrisHandler;
import com.philips.cdp.di.iap.utils.IAPConstant;

import java.util.ArrayList;

public class IAPHandler implements IAPExposedAPI {

    private IAPExposedAPI mImplementationHandler;

    private IAPHandler() {
    }

    public static IAPHandler init(Context context, IAPSettings config) {
        IAPHandler handler = new IAPHandler();
        handler.mImplementationHandler = handler.getExposedAPIImplementor(context, config);
        handler.initHybrisDelegate(context, config);
        handler.initControllerFactory(config);
        handler.setLangAndCountry(config.getLanguage(), config.getCountry());
        handler.getCatalogCountAndCallCatalog();
        return handler;
    }

    public void launchCategorizedCatalog(ArrayList<String> pProductCTNs) {
        mImplementationHandler.launchCategorizedCatalog(pProductCTNs);
    }

    @Override
    public void launchIAP(int landingView, String ctnNumber, IAPHandlerListener listener) {
        if(landingView == IAPConstant.IAPLandingViews.IAP_PRODUCT_DETAIL_VIEW &&
                (ctnNumber == null || ctnNumber.trim().equalsIgnoreCase("")) ){
            throw new RuntimeException("Product Ctn passed is null");
        }
        mImplementationHandler.launchIAP(landingView, ctnNumber, listener);
    }

    @Override
    public void getProductCartCount(final IAPHandlerListener iapHandlerListener) {
        mImplementationHandler.getProductCartCount(iapHandlerListener);
    }

    @Override
    public void getCompleteProductList(IAPHandlerProductListListener iapHandlerListener) {
        mImplementationHandler.getCompleteProductList(iapHandlerListener);
    }

    @Override
    public void getCatalogCountAndCallCatalog() {
        mImplementationHandler.getCatalogCountAndCallCatalog();
    }

    @Override
    public void buyDirect(String ctn) {
        mImplementationHandler.buyDirect(ctn);
    }

    private void setLangAndCountry(final String language, final String country) {
        HybrisDelegate.getInstance().getStore().setLangAndCountry(language, country);
    }

    private IAPExposedAPI getExposedAPIImplementor(Context context, IAPSettings settings) {
        IAPExposedAPI api = null;
        if (settings.isUseLocalData()) {
            api = new AppLocalHandler(context, settings);
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

    private void initControllerFactory(IAPSettings config) {
        int requestCode = getNetworkEssentialReqeustCode(config.isUseLocalData());
        ControllerFactory.getInstance().init(requestCode);
    }
}