
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.dao;

import java.io.Serializable;

public class DIUserProfile implements Serializable {

    private String email;

    private String givenName;

    private String familyName;

    private String password;

    private String displayName;

    private boolean isOlderThanAgeLimit;

    private String janrainUUID;

    private String languageCode;

    private String countryCode;

    private boolean isReceiveMarketingEmail;

    private String hsdpUUID;

    private String hsdpAccessToken;

    public DIUserProfile() {
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public boolean getOlderThanAgeLimit() {
        return isOlderThanAgeLimit;
    }

    public void setOlderThanAgeLimit(boolean isOlderThanAgeLimit) {
        this.isOlderThanAgeLimit = isOlderThanAgeLimit;
    }

    public boolean getReceiveMarketingEmail() {
        return isReceiveMarketingEmail;
    }

    public void setReceiveMarketingEmail(boolean isReceiveMarketingEmail) {
        this.isReceiveMarketingEmail = isReceiveMarketingEmail;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String mFamilyName) {
        this.familyName = mFamilyName;
    }

    public String getJanrainUUID() {
        return janrainUUID;
    }

    public void setJanrainUUID(String janrainUUID) {
        this.janrainUUID = janrainUUID;
    }

    public String getHsdpUUID() {
        return hsdpUUID;
    }

    public void setHsdpUUID(String hsdpUUID) {
        this.hsdpUUID = hsdpUUID;
    }

    public String getHsdpAccessToken() {
        return hsdpAccessToken;
    }

    public void setHsdpAccessToken(String hsdpAccessToken) {
        this.hsdpAccessToken = hsdpAccessToken;
    }


    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }


}
