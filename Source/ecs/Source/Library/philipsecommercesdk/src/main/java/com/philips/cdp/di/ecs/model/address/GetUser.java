package com.philips.cdp.di.ecs.model.address;

public class GetUser {
    private Addresses defaultAddress;

    private String type;
    private String name;
    private String uid;
    private String displayUid;
    private String firstName;
    private String lastName;

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public Addresses getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(Addresses defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public String getDisplayUid() {
        return displayUid;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
