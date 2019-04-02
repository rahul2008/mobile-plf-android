/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.session;

import android.content.Context;

import com.philips.cdp.di.iap.integration.IAPMockInterface;
import com.philips.cdp.di.iap.integration.IAPSettings;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.model.CartAddProductRequest;
import com.philips.cdp.di.iap.model.CartCreateRequest;
import com.philips.cdp.di.iap.model.CartDeleteProductRequest;
import com.philips.cdp.di.iap.model.CartUpdateProductQuantityRequest;
import com.philips.cdp.di.iap.model.ContactCallRequest;
import com.philips.cdp.di.iap.model.CreateAddressRequest;
import com.philips.cdp.di.iap.model.DeleteAddressRequest;
import com.philips.cdp.di.iap.model.DeleteCartRequest;
import com.philips.cdp.di.iap.model.DeleteVoucherRequest;
import com.philips.cdp.di.iap.model.GetAddressRequest;
import com.philips.cdp.di.iap.model.GetAppliedVoucherRequest;
import com.philips.cdp.di.iap.model.GetApplyVoucherRequest;
import com.philips.cdp.di.iap.model.GetCartsRequest;
import com.philips.cdp.di.iap.model.GetCurrentCartRequest;
import com.philips.cdp.di.iap.model.GetDeliveryModesRequest;
import com.philips.cdp.di.iap.model.GetPaymentDetailRequest;
import com.philips.cdp.di.iap.model.GetProductCatalogRequest;
import com.philips.cdp.di.iap.model.GetRegionsRequest;
import com.philips.cdp.di.iap.model.GetRetailersInfoRequest;
import com.philips.cdp.di.iap.model.GetUserRequest;
import com.philips.cdp.di.iap.model.OAuthRequest;
import com.philips.cdp.di.iap.model.OrderDetailRequest;
import com.philips.cdp.di.iap.model.OrderHistoryRequest;
import com.philips.cdp.di.iap.model.PaymentRequest;
import com.philips.cdp.di.iap.model.ProductDetailRequest;
import com.philips.cdp.di.iap.model.RefreshOAuthRequest;
import com.philips.cdp.di.iap.model.SetDeliveryAddressModeRequest;
import com.philips.cdp.di.iap.model.SetDeliveryAddressRequest;
import com.philips.cdp.di.iap.model.SetPaymentDetailsRequest;
import com.philips.cdp.di.iap.model.UpdateAddressRequest;
import com.philips.cdp.di.iap.networkEssential.NetworkEssentials;
import com.philips.cdp.di.iap.store.StoreListener;

public class HybrisDelegate {

    private static HybrisDelegate delegate = new HybrisDelegate();
    protected NetworkController controller;
    private IAPSettings iapSettings;

    private HybrisDelegate() {
    }

    public static HybrisDelegate getInstance(Context context) {
        if (delegate.controller == null) {
            delegate.controller = delegate.getNetworkController(context);
        }
        return delegate;
    }

    public static HybrisDelegate getInstance() {
        return delegate;
    }

    public NetworkController getNetworkController(Context context) {
        if (controller == null) {
            controller = new NetworkController(context);
        }
        return controller;
    }

    public static NetworkController getNetworkController() {
        return delegate.controller;
    }

    public static HybrisDelegate getDelegateWithNetworkEssentials(NetworkEssentials networkEssentials,
                                                                  IAPSettings iapSettings) {
        delegate.controller = delegate.getNetworkController(iapSettings.getContext());
        delegate.controller.setIapSettings(iapSettings);
        delegate.controller.setNetworkEssentials(networkEssentials);
        return delegate;
    }

    public void sendRequest(int requestCode, AbstractModel model, final RequestListener
            requestListener) {
        controller.sendHybrisRequest(requestCode, model, requestListener);
    }

    public StoreListener getStore() {
        return controller.getStore();
    }
}