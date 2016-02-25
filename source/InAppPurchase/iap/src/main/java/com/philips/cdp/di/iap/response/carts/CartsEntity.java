package com.philips.cdp.di.iap.response.carts;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class CartsEntity {

    private String code;
    private String guid;
    private int totalItems;
    private Price totalPrice;
    private Price totalPriceWithTax;
    private List<EntriesEntity> entries;

    private boolean calculated;
    private int deliveryItemsQuantity;
    private boolean net;
    private OrderDiscountsEntity orderDiscounts;
    private int pickupItemsQuantity;
    private ProductDiscountsEntity productDiscounts;
    private String site;
    private String store;
    private SubTotalEntity subTotal;
    private TotalDiscountsEntity totalDiscounts;
    private TotalTaxEntity totalTax;
    private int totalUnitCount;
    private List<DeliveryOrderGroupsEntity> deliveryOrderGroups;
    private DeliveryCostEntity deliveryCost;

    public DeliveryCostEntity getDeliveryCost() {
        return deliveryCost;
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

    public Price getTotalPrice() {
        return totalPrice;
    }

    public Price getTotalPriceWithTax() {
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
