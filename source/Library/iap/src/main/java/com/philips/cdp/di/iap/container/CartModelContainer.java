/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.container;

import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.productCatalog.ProductCatalogData;
import com.philips.cdp.di.iap.response.State.RegionsList;
import com.philips.cdp.di.iap.response.addresses.DeliveryModes;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CartModelContainer {
    private static CartModelContainer container;

    private AddressFields mBillingAddress;
    private AddressFields mShippingAddressFields;

    private String regionIsoCode;
    private String mAddressId;
    private String mOrderNumber;
    private RegionsList mRegionList;

    private HashMap<String, ProductCatalogData> mProductList;
    private HashMap<String, SummaryModel> mPRXSummaryObjects;
    private HashMap<String, ArrayList<String>> mPRXAssetObjects;

    private List<DeliveryModes> mDeliveryModes;

    private boolean switchToBillingAddress;

    private String language;
    private String country;

    private CartModelContainer() {
        mPRXSummaryObjects = new HashMap<>();
        mPRXAssetObjects = new HashMap<>();
        mProductList = new HashMap<>();
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

    public String setAddressId(String mAddressId) {
        this.mAddressId = mAddressId;
        return mAddressId;
    }

    public String getRegionIsoCode() {
        return regionIsoCode;
    }

    public void setRegionIsoCode(String regionIsoCode) {
        this.regionIsoCode = regionIsoCode;
    }

    public List<DeliveryModes> getDeliveryModes() {
        return mDeliveryModes;
    }

    public void setDeliveryModes(List<DeliveryModes> mDeliveryModes) {
        this.mDeliveryModes = mDeliveryModes;
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

    public void setOrderNumber(String orderNumber) {
        mOrderNumber = orderNumber;
    }

    public String getOrderNumber() {
        return mOrderNumber;
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    //PRX Summary
    public boolean isPRXSummaryPresent(String ctn) {
        return mPRXSummaryObjects.containsKey(ctn);
    }

    public SummaryModel getProductSummary(String ctn) {
        return mPRXSummaryObjects.get(ctn);
    }

    public void addProductSummary(String ctn, SummaryModel model) {
        mPRXSummaryObjects.put(ctn, model);
    }

    public HashMap<String, SummaryModel> getPRXSummaryList() {
        return mPRXSummaryObjects;
    }

    //PRX Assets
    public boolean isPRXAssetPresent(String ctn) {
        return mPRXAssetObjects.containsKey(ctn);
    }

    public void addProductAsset(String ctn, ArrayList<String> assets) {
        mPRXAssetObjects.put(ctn, assets);
    }

    public HashMap<String, ArrayList<String>> getPRXAssetList() {
        return mPRXAssetObjects;
    }

    //Product Data
    public boolean isProductCatalogDataPresent(String ctn) {
        return mProductList.containsKey(ctn);
    }

    public void addProduct(String ctn, ProductCatalogData data) {
        if (!mProductList.containsKey(ctn)) {
            mProductList.put(ctn, data);
        }
    }

    public ProductCatalogData getProduct(String ctn) {
        if (mProductList.containsKey(ctn)) {
            return mProductList.get(ctn);
        }
        return null;
    }

    public HashMap<String, ProductCatalogData> getProductList() {
        return mProductList;
    }

    public void clearCategorisedProductList() {
        mProductList.clear();
    }

    public void resetApplicationFields() {
        mBillingAddress = null;
        mShippingAddressFields = null;
        mOrderNumber = null;
    }
}
