
package com.philips.cdp.registration.dao;

public class DIUserProfile {

    private String email;

    private String givenName;

    private String familyName;

    private String password;

    private String displayName;

    private boolean isOlderThanAgeLimit;

    private String janrainUUID;

    private boolean isReceiveMarketingEmail;

    public DIUserProfile() {
    }

    public DIUserProfile(String email, String givenName, String password,
                         boolean isOlderThanAgeLimit, boolean isReceiveMarketingEmail) {
        this.email = email;
        this.givenName = givenName;
        this.password = password;
        this.isOlderThanAgeLimit = isOlderThanAgeLimit;
        this.isReceiveMarketingEmail = isReceiveMarketingEmail;
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

}
