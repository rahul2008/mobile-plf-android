package com.philips.cdpp.dicommtestapp.appliance.property;

import java.util.ArrayList;
import java.util.List;

public class PortSpecification {
    String name;
    int productID;
    boolean supportsSubscription;
    List<Property> properties;

    public PortSpecification() {
        this.properties = new ArrayList<>();
    }

    public boolean supportsSubscription() {
        return supportsSubscription;
    }

    public String getName() {
        return name;
    }

    public int getProductID() {
        return productID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public void addProperty(Property property) {
        properties.add(property);
    }

    public void setSubscriptionSupport(boolean doesSupport) {
        this.supportsSubscription = doesSupport;
    }

    public List<Property> getProperties() {
        return properties;
    }
}