package com.philips.cdp.di.iap.container;

import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartData;
import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.payment.PaymentMethodFields;

import java.util.ArrayList;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class CartModelContainer {
    private static CartModelContainer container;

    public static CartModelContainer getInstance() {
        synchronized (CartModelContainer.class) {
            if (container == null) {
                container = new CartModelContainer();
            }
        }
        return container;
    }

    private ArrayList<ShoppingCartData> mShoppingCartData;
    private AddressFields mShippingAddressFields;
    private AddressFields mBillingAddressFields;
    private PaymentMethodFields mPaymentMethodFields;

    public ArrayList<ShoppingCartData> getShoppingCartData() {
        return mShoppingCartData;
    }

    public void setShoppingCartData(final ArrayList<ShoppingCartData> mShoppingCartData) {
        this.mShoppingCartData = mShoppingCartData;
    }

    public AddressFields getShippingAddressFields() {
        return mShippingAddressFields;
    }

    public void setShippingAddressFields(final AddressFields mShippingAddressFields) {
        this.mShippingAddressFields = mShippingAddressFields;
    }

    public AddressFields getBillingAddressFields() {
        return mBillingAddressFields;
    }

    public void setBillingAddressFields(final AddressFields mBillingAddressFields) {
        this.mBillingAddressFields = mBillingAddressFields;
    }

    public PaymentMethodFields getPaymentMethodFields() {
        return mPaymentMethodFields;
    }

    public void setPaymentMethodFields(final PaymentMethodFields mPaymentMethodFields) {
        this.mPaymentMethodFields = mPaymentMethodFields;
    }
}
