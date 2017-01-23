package com.philips.platform.database;

import java.io.Serializable;
import java.util.Date;

import com.j256.ormlite.field.DatabaseField;

public class AddressBook implements Serializable {

    /**
     * Model class for address database table
     */

    public static final String ID_FIELD = "address_id";

    // Primary key defined as an auto generated integer
    // If the database table column name differs than the Model class variable name, the way to map to use columnName
    @DatabaseField(generatedId = true, columnName = ID_FIELD)
    public int addressId;

    @DatabaseField(columnName = "first_name")
    public String firstName;


    @DatabaseField(columnName = "last_name")
    public String lastName;

    @DatabaseField
    public String address;

    @DatabaseField(columnName = "contact_number")
    public String contactNumber;

    public AddressBook() {

    }

    public AddressBook(String firstName, String lastName, String address, String contactNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.contactNumber = contactNumber;
    }

}
