package com.philips.cdp.di.iap.Response.Cart;
/**
 * Created by 310228564 on 2/2/2016.
 */
public class Entries {
    private int entryNumber;
    private Product product;
    private int quantity;
    private TotalPrice totalPrice;

    public void setEntryNumber(int entryNumber) {
        this.entryNumber = entryNumber;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setTotalPrice(TotalPrice totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getEntryNumber() {
        return entryNumber;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public TotalPrice getTotalPrice() {
        return totalPrice;
    }

}
