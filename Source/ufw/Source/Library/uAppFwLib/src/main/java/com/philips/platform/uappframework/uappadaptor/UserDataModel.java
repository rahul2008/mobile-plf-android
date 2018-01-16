/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.uappframework.uappadaptor;

import java.io.Serializable;
import java.util.Date;

/**
 * User data model class
 * @since 2018.1.0
 */
public class UserDataModel implements DataModel, Serializable {

    private static final long serialVersionUID = 6023320968318363270L;
    private String firstName;
    private String lastName;
    private String uuid;
    private String hsdpuuid;
    private String accessToken;
    private String mobileNumber;
    private String emailAddress;
    private String gender;
    private Date dateOfBirth;

    /**
     * Get the user's first name
     * @return returns the user's first name
     * @since 2018.1.0
     */
    public String getUserFirstName() {
        return firstName;
    }

    /**
     * set the user's first name
     * @param firstName user's first name
     * @since 2018.1.0
     */
    public void setUserFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Get the user's last name
     * @return returns the user's last name
     * @since 2018.1.0
     */
    public String getUserLastName() {
        return lastName;
    }

    /**
     * Set the user's last name
     * @param lastName user's last name
     * @since 2018.1.0
     */
    public void setUserLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Get the UUID
     * @return returns the UUID
     * @since 2018.1.0
     */
    public String getUUID() {
        return uuid;
    }

    /**
     * Set the UUID
     * @param uuid UUID
     * @since 2018.1.0
     */
    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    /**
     * Get the HSDP UUID
     * @return returns the HSDP UUID
     * @since 2018.1.0
     */
    public String getHSDPUUID() {
        return hsdpuuid;
    }

    /**
     * Set HSDP UUID
     * @param hsdpuuid HSDP UUID
     * @since 2018.1.0
     */
    public void setHSDPUUID(String hsdpuuid) {
        this.hsdpuuid = hsdpuuid;
    }

    /**
     * Get the access token
     * @return returns the access token
     * @since 2018.1.0
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * Set the access token
     * @param accessToken access token
     * @since 2018.1.0
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Get the user's mobile number
     * @return returns the user's mobile number
     * @since 2018.1.0
     */
    public String getMobileNumber() {
        return mobileNumber;
    }

    /**
     * Set the mobile number
     * @param mobileNumber Mobile number
     * @since 2018.1.0
     */
    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    /**
     * Get the user's email address
     * @return returns the user's email address
     * @since 2018.1.0
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Set the user's email id
     * @param emailAddress user's email id
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * Get the user's gender
     * @return returns the user's gender
     * @since 2018.1.0
     */
    public String getGender() {
        return gender;
    }

    /**
     * Set the gender of the user
     * @param gender gender of user
     * @since 2018.1.0
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Get the user's data of birth
     * @return returns the date of birth of user
     * @since 2018.1.0
     */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Set the date of birth
     * @param dateOfBirth Date of birth of user
     * @since 2018.1.0
     */
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Get the type of data model
     * @return returns the type of data model
     * @since 2018.1.0
     */
    @Override
    public DataModelType getDataModelType() {
        return DataModelType.USER;
    }
}
