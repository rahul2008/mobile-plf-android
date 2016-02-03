package com.philips.cdp.di.iap.Response.Cart.CreateCart;

import com.philips.cdp.di.iap.Response.Cart.TotalPrice;
import com.philips.cdp.di.iap.Response.Cart.TotalPriceWithTax;

/**
 * Created by 310228564 on 2/3/2016.
 */
public class CreateCartData {

    private String type;
    private String code;
    private String guid;
    private int totalItems;
    private TotalPrice totalPrice;
    private TotalPriceWithTax totalPriceWithTax;

    public void setType(String type) {
        this.type = type;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public void setTotalPrice(TotalPrice totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setTotalPriceWithTax(TotalPriceWithTax totalPriceWithTax) {
        this.totalPriceWithTax = totalPriceWithTax;
    }

    public String getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    public String getGuid() {
        return guid;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public TotalPrice getTotalPrice() {
        return totalPrice;
    }

    public TotalPriceWithTax getTotalPriceWithTax() {
        return totalPriceWithTax;
    }

}
