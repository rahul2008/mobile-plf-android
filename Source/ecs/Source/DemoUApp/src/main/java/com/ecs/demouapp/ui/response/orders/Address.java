package com.ecs.demouapp.ui.response.orders;


import com.ecs.demouapp.ui.response.addresses.Country;

public class Address {

    private Country country;
    private String firstName;
    private String formattedAddress;
    private String id;
    private String lastName;
    private String line1;
    private String houseNumber;
    private String line2;
    private String phone1;
    private String phone2;
    private String postalCode;
    private boolean shippingAddress;
    private String titleCode;
    private Region region;
    private String town;
    private boolean visibleInAddressBook;


    public Country getCountry() {
        return country;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public String getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getLine1() {
        return line1;
    }

    public String getLine2() {
        return line2;
    }

    public String getPhone1() {
        return phone1;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public boolean isShippingAddress() {
        return shippingAddress;
    }

    public String getTitleCode() {
        return titleCode;
    }

    public Region getRegion() {
        return region;
    }

    public String getTown() {
        return town;
    }

    public boolean isVisibleInAddressBook() {
        return visibleInAddressBook;
    }

}
