package com.philips.cdp.di.iap.ShoppingCart;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ShoppingCartData {

    private String mCtnNumber;
    private String mProductTitle;
    private String mImageURL;
    private int mQuantity;
    private int mPrice;
    private String mCurrency;
    private int mTotalItems;
    private int mEntryNumber;
    private String mCartNumber;
    private int mStockLevel;

    public String getCartNumber() {
        return mCartNumber;
    }

    public void setCartNumber(String cartNumber) {
        mCartNumber = cartNumber;
    }

    public int getEntryNumber() {
        return mEntryNumber;
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

    public void setTotalPrice(int price) {
        mPrice = price;
    }

    public int getTotalPrice() {
        return mPrice;
    }

    public void setCurrency(String currency) {
        mCurrency = currency;
    }

    public String getCurrency() {
        return mCurrency;
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
        return mStockLevel;
    }

    @Override
    public String toString() {
        return "ShoppingCartData == " +
                "CTN Number = " + mCtnNumber +
                "Product title =" + mProductTitle +
                "Image URL =" + mImageURL +
                "Quantity = " + mQuantity +
                "Price = " + mPrice +
                "Currnecy =" + mCurrency +
                "Total Item" + mTotalItems +
                "EntryNumber =" + mEntryNumber +
                "CartNumber =" + mCartNumber +
                "StockLevel" + mStockLevel;
    }
}
