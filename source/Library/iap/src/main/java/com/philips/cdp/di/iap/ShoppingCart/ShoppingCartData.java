package com.philips.cdp.di.iap.ShoppingCart;

import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.response.carts.DeliveryAddressEntity;
import com.philips.cdp.di.iap.response.carts.DeliveryCostEntity;
import com.philips.cdp.di.iap.response.carts.EntriesEntity;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ShoppingCartData {

    private EntriesEntity mEntry;
    private String mCtnNumber;
    private String mProductTitle;
    private String mImageURL;
    private int mQuantity;

    private int mTotalItems;
    private int mEntryNumber;
    private String mCartNumber;
    private int mStockLevel;
    private DeliveryCostEntity mDeliveryCost;
    private DeliveryAddressEntity mDeliveryAddressEntity;
    private String mTotalPriceFormatedPrice;
    private String mTotalPriceWithTaxFormatedPrice;
    private String mFormatedPrice;
    private String mValuePrice;
    private String mVatValue;

    public boolean isVatInclusive() {
        return mVatInclusive;
    }

    public void setVatInclusive(boolean mVatInclusive) {
        this.mVatInclusive = mVatInclusive;
    }

    private boolean mVatInclusive;

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(final String mCategory) {
        this.mCategory = mCategory;
    }

    private String mCategory;

    public String getValuePrice() {
        return mValuePrice;
    }

    public void setValuePrice(String mValuePrice) {
        this.mValuePrice = mValuePrice;
    }

    public int getDeliveryItemsQuantity() {
        return mDeliveryItemsQuantity;
    }

    public void setDeliveryItemsQuantity(final int mDeliveryItemsQuantity) {
        this.mDeliveryItemsQuantity = mDeliveryItemsQuantity;
    }

    private int mDeliveryItemsQuantity;

    public String getMarketingTextHeader() {
        return mMarketingTextHeader;
    }

    public void setMarketingTextHeader(final String pMarketingTextHeader) {
        this.mMarketingTextHeader = pMarketingTextHeader;
    }

    private String mMarketingTextHeader;


    public ShoppingCartData() {
    }

    public DeliveryAddressEntity getDeliveryAddressEntity() {
        return mDeliveryAddressEntity;
    }

    public void setDeliveryAddressEntity(DeliveryAddressEntity mDeliveryAddressEntity) {
        this.mDeliveryAddressEntity = mDeliveryAddressEntity;
    }

    public ShoppingCartData(EntriesEntity entry, DeliveryCostEntity deliveryCost) {
        mEntry = entry;
        mDeliveryCost = deliveryCost;

    }

    public DeliveryCostEntity getDeliveryCost() {
        return mDeliveryCost;
    }

    public String getCartNumber() {
        return mCartNumber;
    }

    public void setCartNumber(String cartNumber) {
        mCartNumber = cartNumber;
        CartModelContainer.getInstance().setCartNumber(mCartNumber);
    }

    public int getEntryNumber() {
        return mEntry.getEntryNumber();
    }

    public void setEntryNumber(int entryNumber) {
        mEntryNumber = entryNumber;
    }

    public String getCtnNumber() {
        return mCtnNumber;
    }

    public void setCtnNumber(String ctnNumber) {
        mCtnNumber = ctnNumber;
    }

    public void setProductTitle(String productTitle) {
        mProductTitle = productTitle;
    }

    public String getProductTitle() {
        return mProductTitle;
    }

    public void setImageUrl(String url) {
        mImageURL = url;
    }

    public String getImageURL() {
        return mImageURL;
    }

    public void setQuantity(int items) {
        mQuantity = items;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setTotalItems(int totalItems) {
        mTotalItems = totalItems;
    }

    public int getTotalItems() {
        return mTotalItems;
    }

    public void setStockLevel(final int stockLevel) {
        mStockLevel = stockLevel;
    }

    public int getStockLevel() {
        return mEntry.getProduct().getStock().getStockLevel();
    }

    @Override
    public String toString() {
        return "ShoppingCartData == " +
                "CTN Number = " + mCtnNumber +
                "Product title =" + mProductTitle +
                "Image URL =" + mImageURL +
                "Quantity = " + mQuantity +
                "Total Item" + mTotalItems +
                "EntryNumber =" + mEntryNumber +
                "CartNumber =" + mCartNumber +
                "StockLevel" + mStockLevel;
    }


    public String getTotalPriceWithTaxFormatedPrice() {
        return mTotalPriceWithTaxFormatedPrice;
    }

    public void setTotalPriceWithTaxFormatedPrice(String mTotalPriceWithTaxFormatedPrice) {
        this.mTotalPriceWithTaxFormatedPrice = mTotalPriceWithTaxFormatedPrice;
    }

    public String getFormatedPrice() {
        return mFormatedPrice;
    }

    public void setFormatedPrice(String mFormatedPrice) {
        this.mFormatedPrice = mFormatedPrice;
    }

    public String getTotalPriceFormatedPrice() {
        return mTotalPriceFormatedPrice;
    }

    public void setTotalPriceFormatedPrice(String mTotalPriceFormatedPrice) {
        this.mTotalPriceFormatedPrice = mTotalPriceFormatedPrice;
    }

    public String getVatValue() {
        return mVatValue;
    }

    public void setVatValue(String mVatValue) {
        this.mVatValue = mVatValue;
    }
}