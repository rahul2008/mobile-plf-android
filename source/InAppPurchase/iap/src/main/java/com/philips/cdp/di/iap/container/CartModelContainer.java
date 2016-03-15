package com.philips.cdp.di.iap.container;

import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartData;
import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.utils.IAPConfiguration;

import java.util.ArrayList;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class CartModelContainer {
    private static CartModelContainer container;
    private Addresses mDeliveryAddress;
    private boolean isOrderPlaced;
    private ArrayList<ShoppingCartData> mShoppingCartData;
    private AddressFields mShippingAddressFields;
    private String cartNumber;

    public IAPConfiguration getIapConfiguration() {
        return iapConfiguration;
    }

    public void setIapConfiguration(IAPConfiguration iapConfiguration) {
        this.iapConfiguration = iapConfiguration;
    }

    private IAPConfiguration iapConfiguration;

    public static CartModelContainer getInstance() {
        synchronized (CartModelContainer.class) {
            if (container == null) {
                container = new CartModelContainer();
            }
        }
        return container;
    }

    public String getCartNumber() {
        return cartNumber;
    }

    public void setCartNumber(final String cartNumber) {
        this.cartNumber = cartNumber;
    }

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

    public void setDeliveryAddress(final Addresses mDeliveryAddress) {
        this.mDeliveryAddress = mDeliveryAddress;
    }

    public Addresses getDeliveryAddress() {
        return mDeliveryAddress;
    }

    public void setOrderPlaced(final boolean pIsOrderPlaced) {
        this.isOrderPlaced = pIsOrderPlaced;
    }

    public boolean isOrderPlaced() {
        return isOrderPlaced;
    }

    public void resetApplicationFields() {
        setOrderPlaced(false);
        cartNumber = null;
        mDeliveryAddress = null;
        mShoppingCartData = null;
        mShippingAddressFields = null;
    }
}
