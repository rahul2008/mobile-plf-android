/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.container;

import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartData;
import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.response.State.RegionsList;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;

import java.util.ArrayList;
import java.util.HashMap;

public class CartModelContainer {
    private static CartModelContainer container;
    private AddressFields mBillingAddress;
    private boolean isOrderPlaced;
    private ArrayList<ShoppingCartData> mShoppingCartData;
    private AddressFields mShippingAddressFields;
    private String cartNumber;
    private String regionIsoCode;
    private String mAddressId;
    private String mOrderNumber;
    private RegionsList mRegionList;
    private HashMap<String, SummaryModel> mPRXDataObjects;
    private HashMap<String, ArrayList<String>> mPRXAssetObjects;

    private boolean switchToBillingAddress;

    private CartModelContainer() {
        mPRXDataObjects = new HashMap<>();
        mPRXAssetObjects = new HashMap<>();
    }

    public static CartModelContainer getInstance() {
        synchronized (CartModelContainer.class) {
            if (container == null) {
                container = new CartModelContainer();
            }
        }
        return container;
    }

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

    public void setOrderNumber(String orderNumber) {
        mOrderNumber = orderNumber;
    }

    public String getOrderNumber() {
        return mOrderNumber;
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
        mOrderNumber = null;
    }

    public void setSwitchToBillingAddress(boolean switchToBillingAddress) {
        this.switchToBillingAddress = switchToBillingAddress;
    }

    public boolean isSwitchToBillingAddress() {
        return switchToBillingAddress;
    }

    public void setRegionList(RegionsList regionList) {
        mRegionList = regionList;
    }

    public RegionsList getRegionList() {
        return mRegionList;
    }

    public SummaryModel getProductData(String ctn) {
        return mPRXDataObjects.get(ctn);
    }

    public boolean isPRXDataPresent(String ctn) {
        return mPRXDataObjects.containsKey(ctn);
    }

    public void addProductDataToList(String ctn, SummaryModel model) {
        mPRXDataObjects.put(ctn, model);
    }

    public HashMap<String, SummaryModel> getPRXDataObjects() {
        return mPRXDataObjects;
    }

    public boolean isPRXAssetPresent(String ctn) {
        return mPRXAssetObjects.containsKey(ctn);
    }

    public void addAssetDataToList(String ctn, ArrayList<String> assets) {
        mPRXAssetObjects.put(ctn, assets);
    }

    public HashMap<String, ArrayList<String>> getPRXAssetObjects() {
        return mPRXAssetObjects;
    }
}
