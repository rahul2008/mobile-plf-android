/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.dao;

import com.philips.cdp.registration.ui.utils.Gender;

import java.io.Serializable;
import java.util.Date;

public class DIUserProfile implements Serializable {
    //Warning : Don't alter this file which cause the security migration issues

    /* Email */
    private String email;

    /* Mobile */
    private String mobile;

    /* Given name */
    private String givenName;

    /* Family name */
    private String familyName;

    /* Password */
    private String password;

    /* Display name */
    private String displayName;

    /* Is older than age limit */
    private boolean isOlderThanAgeLimit;

    /* Janrain UUID */
    private String janrainUUID;

    /* Language code */
    private String languageCode;

    /* Country code */
    private String countryCode;

    /* Is receive marketing email */
    private boolean isReceiveMarketingEmail;

    /* Hsdp UUID */
    private String hsdpUUID;

    /* hsdp access token */
    private String hsdpAccessToken;

    /**
     * Get Birth date
     * @return Date
     */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     *  Set date of birth
     * @param dateOfBirth Date of birth
     */
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     *
     * @return Get
     */
    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    /* Gender */
    private Gender gender;

    /* Date of birth */
    private Date dateOfBirth;


    /**
     * Class constructor
     */
    public DIUserProfile() {
    }

    /**
     * get email
     *
     * @return email address
     *
     * @since 1.0.0
     */
    public String getEmail() {
        return email;
    }

    /**
     * set email
     *
     * @param email email address
     */
    public void setEmail(String email) {
        this.email = email;
    }



    /**
     * get mobile
     * @return mobile address
     *
     * @since 1.0.0
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * set mobile
     * @param mobile email address
     *
     *               @since 1.0.0
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * get password
     *
     * @return password
     *
     * @since 1.0.0
     */
    public String getPassword() {
        return password;
    }

    /**
     * set password
     *
     * @param password password
     *
     *                 @since 1.0.0
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * get name
     *
     * @return name
     */
    public String getGivenName() {
        return givenName;
    }

    /**
     * Set given name
     *
     * @param givenName given name
     *
     *                  @since 1.0.0
     */
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    /**
     * Validation  result older than age limit
     *
     * @return true if user is older than age limit else false
     *
     * @since 1.0.0
     */
    public boolean getOlderThanAgeLimit() {
        return isOlderThanAgeLimit;
    }

    /**
     * Validate older than age limit
     *
     * @param isOlderThanAgeLimit to validate the age limit
     *
     *                            @since 1.0.0
     */
    public void setOlderThanAgeLimit(boolean isOlderThanAgeLimit) {
        this.isOlderThanAgeLimit = isOlderThanAgeLimit;
    }

    /**
     * get receive market email
     *
     * @return true if marketing email available else false
     *
     * @since 1.0.0
     */
    public boolean getReceiveMarketingEmail() {
        return isReceiveMarketingEmail;
    }

    /**
     * set receive market email
     *
     * @param isReceiveMarketingEmail receive market email
     *
     *                                @since 1.0.0
     */
    public void setReceiveMarketingEmail(boolean isReceiveMarketingEmail) {
        this.isReceiveMarketingEmail = isReceiveMarketingEmail;
    }

    /**
     * get display name
     *
     * @return display name
     *
     * @since 1.0.0
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * set display name
     *
     * @param displayName display name
     *
     *                    @since 1.0.0
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * get famil name
     *
     * @return family name
     *
     * @since 1.0.0
     */
    public String getFamilyName() {
        return familyName;
    }

    /**
     * set family name
     *
     * @param familyName family name
     *
     *                   @since 1.0.0
     */
    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    /**
     * get janrain uuid
     *
     * @return janrainUUID - String janrainUUID
     *
     * @since 1.0.0
     */
    public String getJanrainUUID() {
        return janrainUUID;
    }

    /**
     * set janrain uuid
     *
     * @param janrainUUID - String janrainUUID
     *
     *                    @since 1.0.0
     */
    public void setJanrainUUID(String janrainUUID) {
        this.janrainUUID = janrainUUID;
    }

    /**
     * get hsdp uuid
     *
     * @return hsdpUUID - String hsdpUUID
     *
     * @since 1.0.0
     */
    public String getHsdpUUID() {
        return hsdpUUID;
    }

    /**
     * set hsdp uuid
     *
     * @param hsdpUUID - String hsdpUUID
     *                 @since 1.0.0
     */
    public void setHsdpUUID(String hsdpUUID) {
        this.hsdpUUID = hsdpUUID;
    }

    /**
     * get hspd access token
     *
     * @return hsdpAccessToken - String hsdpAccessToken
     * @since 1.0.0
     */
    public String getHsdpAccessToken() {
        return hsdpAccessToken;
    }

    /**
     * Set hsdp access token
     *
     * @param hsdpAccessToken - String hsdpAccessToken
     *                        @since 1.0.0
     */
    public void setHsdpAccessToken(String hsdpAccessToken) {
        this.hsdpAccessToken = hsdpAccessToken;
    }

    /**
     * {@code getLanguageCode} method to get language code
     *
     * @return languageCode - String languageCode
     * @since 1.0.0
     */
    public String getLanguageCode() {
        return languageCode;
    }

    /**
     * {@code setLanguageCode} method to set language code
     *
     * @param languageCode - String languageCode
     *                     @since 1.0.0
     */
    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    /**
     * {@code getCountryCode} method to get country code
     *
     * @return countryCode - String countryCode
     * @since 1.0.0
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * {@code setCountryCode} method to set country code
     *
     * @param countryCode - String countryCode
     *
     * @since 1.0.0
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }


}
