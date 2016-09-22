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

    private HashMap<String, SummaryModel> mPRXDataObjects;
    private HashMap<String, ProductCatalogData> mProductCatalogData;
    private HashMap<String, ArrayList<String>> mPRXAssetObjects;

    private List<DeliveryModes> mDeliveryModes;

    private boolean switchToBillingAddress;
    private boolean mIsCartCreated;

    private String language;
    private String country;

    private CartModelContainer() {
        mPRXDataObjects = new HashMap<>();
        mPRXAssetObjects = new HashMap<>();
        mProductCatalogData = new HashMap<>();
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

    public void resetApplicationFields() {
        mBillingAddress = null;
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

    public ProductCatalogData getProductCatalogData(String ctn) {
        return mProductCatalogData.get(ctn);
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

    public void addProductCatalogDataDataToList(String ctn, ProductCatalogData data) {
        if (!mProductCatalogData.containsKey(ctn)) {
            mProductCatalogData.put(ctn, data);
        }
    }

    public boolean isProductCatalogDataPresent(String ctn) {
        return mProductCatalogData.containsKey(ctn);
    }

    public ProductCatalogData getProduct(String ctn) {
        if (mProductCatalogData.containsKey(ctn)) {
            return mProductCatalogData.get(ctn);
        }
        return null;
    }

    public void clearCategoriezedProductList() {
        mProductCatalogData.clear();
    }

    public HashMap<String, ArrayList<String>> getPRXAssetObjects() {
        return mPRXAssetObjects;
    }

    public HashMap<String, ProductCatalogData> getProductCatalogData() {
        return mProductCatalogData;
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
}
