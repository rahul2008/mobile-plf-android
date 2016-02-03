package com.philips.cdp.di.iap.data;

import java.util.LinkedList;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProductData {

    private String ctnNumber;
    private String productTitle;
    private String imageURL;
    private int quantity;
    private int price;
    private String currency;
    private static int totalItems;

    public String getCtnNumber() {
        return ctnNumber;
    }
    public void setCtnNumber(String ctnNumber) {
        this.ctnNumber = ctnNumber;
    }

    public void setProductTitle(String product_title){
        productTitle = product_title;
    }

    public String getProductTitle(){
        return productTitle;
    }

    public void setImageUrl(String url){
        imageURL = url;
    }

    public String getImageURL(){
        return imageURL;
    }

    public void setQuantity(int quantity_of_items){
        quantity = quantity_of_items;
    }

    public int getQuantity(){
        return quantity;
    }

    public void setPrice(int totalprice){
        price = totalprice;
    }

    public int getPrice(){
        return price;
    }

    public void setCurrency(String currency1){
        currency = currency1;
    }

    public String getCurrency(){
        return currency;
    }

    public void setTotalItems(int totalItems1){
        totalItems = totalItems1;
    }

    public int getTotalItems(){
        return totalItems;
    }
}
