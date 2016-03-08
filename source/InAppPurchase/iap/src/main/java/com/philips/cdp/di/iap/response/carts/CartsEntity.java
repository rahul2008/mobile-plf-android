package com.philips.cdp.di.iap.response.carts;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class CartsEntity {
    private boolean calculated;
    private String code;
    /**
     * country : {"isocode":"US"}
     * firstName : Inder
     * formattedAddress : line1, line2, bangalore, 12345
     * id : 8799796592663
     * lastName : test
     * line1 : line1
     * line2 : line2
     * phone : 12345
     * postalCode : 12345
     * shippingAddress : true
     * titleCode : mr
     * town : bangalore
     * visibleInAddressBook : true
     */

    private DeliveryAddressEntity deliveryAddress;
    /**
     * currencyIso : USD
     * formattedValue : $8.99
     * priceType : BUY
     * value : 8.99
     */

    private DeliveryCostEntity deliveryCost;
    private int deliveryItemsQuantity;
    /**
     * code : standard-gross
     * deliveryCost : {"currencyIso":"USD","formattedValue":"$8.99","priceType":"BUY","value":8.99}
     */

    private DeliveryModeEntity deliveryMode;
    private String guid;
    private boolean net;
    /**
     * currencyIso : USD
     * formattedValue : $0.00
     * priceType : BUY
     * value : 0
     */

    private OrderDiscountsEntity orderDiscounts;
    private int pickupItemsQuantity;
    /**
     * currencyIso : USD
     * formattedValue : $0.00
     * priceType : BUY
     * value : 0
     */

    private ProductDiscountsEntity productDiscounts;
    private String site;
    private String store;
    /**
     * currencyIso : USD
     * formattedValue : $1,702.97
     * priceType : BUY
     * value : 1702.97
     */

    private SubTotalEntity subTotal;
    /**
     * currencyIso : USD
     * formattedValue : $0.00
     * priceType : BUY
     * value : 0
     */

    private TotalDiscountsEntity totalDiscounts;
    private int totalItems;
    /**
     * currencyIso : USD
     * formattedValue : $1,711.96
     * priceType : BUY
     * value : 1711.96
     */

    private TotalPriceEntity totalPrice;
    /**
     * currencyIso : USD
     * formattedValue : $1,711.96
     * priceType : BUY
     * value : 1711.96
     */

    private TotalPriceWithTaxEntity totalPriceWithTax;
    /**
     * currencyIso : USD
     * formattedValue : $0.00
     * priceType : BUY
     * value : 0
     */

    private TotalTaxEntity totalTax;
    private int totalUnitCount;
    /**
     * entries : [{"basePrice":{"currencyIso":"USD","formattedValue":"$400.00","priceType":"BUY","value":400},"entryNumber":0,"product":{"availableForPickup":false,"categories":[{"code":"Tuscany_Campaign","url":"/Tuscany-Category/c/Tuscany_Campaign"}],"code":"HX9042/64","discountPrice":{"currencyIso":"USD","value":400},"purchasable":true,"stock":{"stockLevel":937,"stockLevelStatus":"inStock"},"url":"/Tuscany-Category//p/HX9042_64"},"quantity":2,"totalPrice":{"currencyIso":"USD","formattedValue":"$800.00","priceType":"BUY","value":800},"updateable":true},{"basePrice":{"currencyIso":"USD","formattedValue":"$300.99","priceType":"BUY","value":300.99},"entryNumber":1,"product":{"availableForPickup":false,"categories":[{"code":"Tuscany_Campaign","url":"/Tuscany-Category/c/Tuscany_Campaign"}],"code":"HX8331/11","discountPrice":{"currencyIso":"USD","value":300.99},"purchasable":true,"stock":{"stockLevel":207,"stockLevelStatus":"inStock"},"url":"/Tuscany-Category//p/HX8331_11"},"quantity":3,"totalPrice":{"currencyIso":"USD","formattedValue":"$902.97","priceType":"BUY","value":902.97},"updateable":true}]
     * totalPriceWithTax : {"currencyIso":"USD","formattedValue":"$1,702.97","priceType":"BUY","value":1702.97}
     */

    private List<DeliveryOrderGroupsEntity> deliveryOrderGroups;
    /**
     * basePrice : {"currencyIso":"USD","formattedValue":"$400.00","priceType":"BUY","value":400}
     * entryNumber : 0
     * product : {"availableForPickup":false,"categories":[{"code":"Tuscany_Campaign","url":"/Tuscany-Category/c/Tuscany_Campaign"}],"code":"HX9042/64","discountPrice":{"currencyIso":"USD","value":400},"purchasable":true,"stock":{"stockLevel":937,"stockLevelStatus":"inStock"},"url":"/Tuscany-Category//p/HX9042_64"}
     * quantity : 2
     * totalPrice : {"currencyIso":"USD","formattedValue":"$800.00","priceType":"BUY","value":800}
     * updateable : true
     */

    private List<EntriesEntity> entries;

    public void setCalculated(boolean calculated) {
        this.calculated = calculated;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDeliveryAddress(DeliveryAddressEntity deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public void setDeliveryCost(DeliveryCostEntity deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public void setDeliveryItemsQuantity(int deliveryItemsQuantity) {
        this.deliveryItemsQuantity = deliveryItemsQuantity;
    }

    public void setDeliveryMode(DeliveryModeEntity deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public void setNet(boolean net) {
        this.net = net;
    }

    public void setOrderDiscounts(OrderDiscountsEntity orderDiscounts) {
        this.orderDiscounts = orderDiscounts;
    }

    public void setPickupItemsQuantity(int pickupItemsQuantity) {
        this.pickupItemsQuantity = pickupItemsQuantity;
    }

    public void setProductDiscounts(ProductDiscountsEntity productDiscounts) {
        this.productDiscounts = productDiscounts;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public void setSubTotal(SubTotalEntity subTotal) {
        this.subTotal = subTotal;
    }

    public void setTotalDiscounts(TotalDiscountsEntity totalDiscounts) {
        this.totalDiscounts = totalDiscounts;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public void setTotalPrice(TotalPriceEntity totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setTotalPriceWithTax(TotalPriceWithTaxEntity totalPriceWithTax) {
        this.totalPriceWithTax = totalPriceWithTax;
    }

    public void setTotalTax(TotalTaxEntity totalTax) {
        this.totalTax = totalTax;
    }

    public void setTotalUnitCount(int totalUnitCount) {
        this.totalUnitCount = totalUnitCount;
    }

    public void setDeliveryOrderGroups(List<DeliveryOrderGroupsEntity> deliveryOrderGroups) {
        this.deliveryOrderGroups = deliveryOrderGroups;
    }

    public void setEntries(List<EntriesEntity> entries) {
        this.entries = entries;
    }

    public boolean isCalculated() {
        return calculated;
    }

    public String getCode() {
        return code;
    }

    public DeliveryAddressEntity getDeliveryAddress() {
        return deliveryAddress;
    }

    public DeliveryCostEntity getDeliveryCost() {
        return deliveryCost;
    }

    public int getDeliveryItemsQuantity() {
        return deliveryItemsQuantity;
    }

    public DeliveryModeEntity getDeliveryMode() {
        return deliveryMode;
    }

    public String getGuid() {
        return guid;
    }

    public boolean isNet() {
        return net;
    }

    public OrderDiscountsEntity getOrderDiscounts() {
        return orderDiscounts;
    }

    public int getPickupItemsQuantity() {
        return pickupItemsQuantity;
    }

    public ProductDiscountsEntity getProductDiscounts() {
        return productDiscounts;
    }

    public String getSite() {
        return site;
    }

    public String getStore() {
        return store;
    }

    public SubTotalEntity getSubTotal() {
        return subTotal;
    }

    public TotalDiscountsEntity getTotalDiscounts() {
        return totalDiscounts;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public TotalPriceEntity getTotalPrice() {
        return totalPrice;
    }

    public TotalPriceWithTaxEntity getTotalPriceWithTax() {
        return totalPriceWithTax;
    }

    public TotalTaxEntity getTotalTax() {
        return totalTax;
    }

    public int getTotalUnitCount() {
        return totalUnitCount;
    }

    public List<DeliveryOrderGroupsEntity> getDeliveryOrderGroups() {
        return deliveryOrderGroups;
    }

    public List<EntriesEntity> getEntries() {
        return entries;
    }
}