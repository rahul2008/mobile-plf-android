package com.philips.cdp.di.iap.response.placeorder;

import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class PlaceOrder {

    private String type;
    private boolean calculated;
    private String code;

    private DeliveryAddressEntity deliveryAddress;

    private DeliveryCostEntity deliveryCost;
    private int deliveryItemsQuantity;

    private DeliveryModeEntity deliveryMode;
    private String guid;
    private boolean net;

    private OrderDiscountsEntity orderDiscounts;

    private PaymentInfoEntity paymentInfo;
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

    private UserEntity user;
    private String created;
    private boolean guestCustomer;
    private String statusDisplay;

    private List<DeliveryOrderGroupsEntity> deliveryOrderGroups;

    private List<EntriesEntity> entries;

    private List<UnconsignedEntriesEntity> unconsignedEntries;

    public String getType() {
        return type;
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

    public PaymentInfoEntity getPaymentInfo() {
        return paymentInfo;
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

    public UserEntity getUser() {
        return user;
    }

    public String getCreated() {
        return created;
    }

    public boolean isGuestCustomer() {
        return guestCustomer;
    }

    public String getStatusDisplay() {
        return statusDisplay;
    }

    public List<DeliveryOrderGroupsEntity> getDeliveryOrderGroups() {
        return deliveryOrderGroups;
    }

    public List<EntriesEntity> getEntries() {
        return entries;
    }

    public List<UnconsignedEntriesEntity> getUnconsignedEntries() {
        return unconsignedEntries;
    }
}
