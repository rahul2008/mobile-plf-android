package com.philips.cdp.di.iap.response.carts;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class CartsEntity {
    private boolean calculated;
    private String code;
    private int deliveryItemsQuantity;
    private String guid;
    private boolean net;

    private OrderDiscountsEntity orderDiscounts;
    private int pickupItemsQuantity;

    private ProductDiscountsEntity productDiscounts;
    private String site;
    private String store;

    private SubTotalEntity subTotal;

    private TotalDiscountsEntity totalDiscounts;
    private int totalItems;

    private TotalPriceEntity totalPrice;

    private TotalPriceWithTaxEntity totalPriceWithTax;

    private TotalTaxEntity totalTax;
    private int totalUnitCount;

    private List<DeliveryOrderGroupsEntity> deliveryOrderGroups;

    private List<EntriesEntity> entries;

    public void setCalculated(boolean calculated) {
        this.calculated = calculated;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDeliveryItemsQuantity(int deliveryItemsQuantity) {
        this.deliveryItemsQuantity = deliveryItemsQuantity;
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

    public int getDeliveryItemsQuantity() {
        return deliveryItemsQuantity;
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
