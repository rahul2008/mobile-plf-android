package com.philips.cdp.di.iap.container;

import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartData;
import com.philips.cdp.di.iap.address.AddressFields;

import java.util.ArrayList;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class CartModelContainer {
    private static CartModelContainer container;
    private AddressFields mBillingAddress;
    private boolean isOrderPlaced;
    private ArrayList<ShoppingCartData> mShoppingCartData;
    private AddressFields mShippingAddressFields;
    private String cartNumber;
    private String regionIsoCode;
    private String mAddressId;
    private String shippingAddressId;

    public String getAddressId() {
        return mAddressId;
    }

    public void setAddressId(String mAddressId) {
        this.mAddressId = mAddressId;
    }

    public String getRegionIsoCode() {
        return regionIsoCode;
    }

    public void setRegionIsoCode(String regionIsoCode) {
        this.regionIsoCode = regionIsoCode;
    }

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

    public void setBillingAddress(final AddressFields mBillingAddress) {
        this.mBillingAddress = mBillingAddress;
    }

    public AddressFields getBillingAddress() {
        return mBillingAddress;
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
        mBillingAddress = null;
        mShoppingCartData = null;
        mShippingAddressFields = null;
    }

    //Note : This can be remove once Address and AddressField become same then we can getId
    public void setShippingAddressId(String shippingAddressId) {
        this.shippingAddressId = shippingAddressId;
    }

    public String getShippingAddressId() {
        return shippingAddressId;
    }

}
