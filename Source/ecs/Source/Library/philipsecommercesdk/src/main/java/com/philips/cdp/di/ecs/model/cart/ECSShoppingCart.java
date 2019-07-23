package com.philips.cdp.di.ecs.model.cart;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ECSShoppingCart {
    private boolean calculated;
    private String code;

    private DeliveryAddressEntity deliveryAddress;

    private DeliveryCostEntity deliveryCost;
    private int deliveryItemsQuantity;

    private DeliveryModeEntity deliveryMode;
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

    private List<AppliedOrderPromotionEntity> appliedOrderPromotions;

    public List<AppliedVoucherEntity> getAppliedVouchers() {
        return appliedVouchers;
    }

    public void setAppliedVouchers(List<AppliedVoucherEntity> appliedVouchers) {
        this.appliedVouchers = appliedVouchers;
    }

    private List<AppliedVoucherEntity> appliedVouchers;


    public List<AppliedOrderPromotionEntity> getAppliedOrderPromotions() {
        return appliedOrderPromotions;
    }

    public void setAppliedOrderPromotions(List<AppliedOrderPromotionEntity> appliedOrderPromotions) {
        this.appliedOrderPromotions = appliedOrderPromotions;
    }

    private List<EntriesEntity> entries;

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