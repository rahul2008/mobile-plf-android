package com.philips.cdp.di.iap.response.orders;

import java.io.Serializable;
import java.util.List;

public class OrderDetail implements Serializable{


    private String type;
    private boolean calculated;
    private String code;

    private Address deliveryAddress;
    private Cost deliveryCost;
    private int deliveryItemsQuantity;

    private DeliveryMode deliveryMode;
    private String guid;
    private boolean net;

    private Cost orderDiscounts;

    private PaymentInfo paymentInfo;
    private int pickupItemsQuantity;

    private Cost productDiscounts;
    private String site;
    private String store;

    private Cost subTotal;

    private Cost totalDiscounts;
    private int totalItems;

    private Cost totalPrice;
    private Cost totalPriceWithTax;
    private Cost totalTax;

    private User user;
    private String created;
    private String deliveryStatus;
    private boolean guestCustomer;
    private String ordertrackUrl;
    private String status;
    private String statusDisplay;


    private List<AppliedOrderPromotions> appliedOrderPromotions;
    private List<DeliveryOrderGroups> deliveryOrderGroups;

    private List<Entries> entries;

    private List<Consignment> consignments;

    public String getType() {
        return type;
    }

    public boolean isCalculated() {
        return calculated;
    }

    public String getCode() {
        return code;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public Cost getDeliveryCost() {
        return deliveryCost;
    }

    public int getDeliveryItemsQuantity() {
        return deliveryItemsQuantity;
    }

    public DeliveryMode getDeliveryMode() {
        return deliveryMode;
    }

    public String getGuid() {
        return guid;
    }

    public boolean isNet() {
        return net;
    }

    public Cost getOrderDiscounts() {
        return orderDiscounts;
    }

    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    public int getPickupItemsQuantity() {
        return pickupItemsQuantity;
    }

    public Cost getProductDiscounts() {
        return productDiscounts;
    }

    public String getSite() {
        return site;
    }

    public String getStore() {
        return store;
    }

    public Cost getSubTotal() {
        return subTotal;
    }

    public Cost getTotalDiscounts() {
        return totalDiscounts;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public Cost getTotalPrice() {
        return totalPrice;
    }

    public Cost getTotalPriceWithTax() {
        return totalPriceWithTax;
    }

    public Cost getTotalTax() {
        return totalTax;
    }

    public User getUser() {
        return user;
    }

    public String getCreated() {
        return created;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public boolean isGuestCustomer() {
        return guestCustomer;
    }

    public String getOrdertrackUrl() {
        return ordertrackUrl;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusDisplay() {
        return statusDisplay;
    }

    public List<AppliedOrderPromotions> getAppliedOrderPromotions() {
        return appliedOrderPromotions;
    }
    public List<DeliveryOrderGroups> getDeliveryOrderGroups() {
        return deliveryOrderGroups;
    }

    public List<Entries> getEntries() {
        return entries;
    }

    public List<Consignment> getConsignments() {
        return consignments;
    }

}
