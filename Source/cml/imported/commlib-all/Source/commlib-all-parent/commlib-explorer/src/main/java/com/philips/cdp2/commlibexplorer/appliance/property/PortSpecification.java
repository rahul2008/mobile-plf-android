/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.commlibexplorer.appliance.property;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class PortSpecification {
    String name;
    int productID;
    boolean supportsSubscription;
    Set<Property> properties;

    public PortSpecification() {
        this.properties = new CopyOnWriteArraySet<>();
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

    public void isSubscriptionSupported(boolean isSupported) {
        this.supportsSubscription = isSupported;
    }

    public Set<Property> getProperties() {
        return properties;
    }
}