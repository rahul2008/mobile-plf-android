package com.ecs.demouapp.ui.model;

/**
 * Created by philips on 2/4/19.
 */

public class AddressFieldEnabler {

    private boolean isSalutationEnabled = true, isFirstNameEnabled = true, isLastNmeEnabled = true,
            isEmailEnabled = true, isPhoneEnabled = true, isHouseNumberEnabled = true,
            isAddress1Enabled = true, isAddress2Enabled = true,
            isTownEnabled = true, isPostalCodeEnabled = true, isStateEnabled = true,
            isCountryEnabled = true;

    public boolean isSalutationEnabled() {
        return isSalutationEnabled;
    }

    public void setSalutationEnabled(boolean salutationEnabled) {
        this.isSalutationEnabled = salutationEnabled;
    }

    public boolean isFirstNameEnabled() {
        return isFirstNameEnabled;
    }

    public void setFirstNameEnabled(boolean firstNameEnabled) {
        this.isFirstNameEnabled = firstNameEnabled;
    }

    public boolean isLastNmeEnabled() {
        return isLastNmeEnabled;
    }

    public void setLastNmeEnabled(boolean lastNmeEnabled) {
        this.isLastNmeEnabled = lastNmeEnabled;
    }

    public boolean isEmailEnabled() {
        return isEmailEnabled;
    }

    public void setEmailEnabled(boolean emailEnabled) {
        this.isEmailEnabled = emailEnabled;
    }

    public boolean isPhoneEnabled() {
        return isPhoneEnabled;
    }

    public void setPhoneEnabled(boolean phoneEnabled) {
        this.isPhoneEnabled = phoneEnabled;
    }

    public boolean isHouseNumberEnabled() {
        return isHouseNumberEnabled;
    }

    public void setHouseNumberEnabled(boolean houseNumberEnabled) {
        this.isHouseNumberEnabled = houseNumberEnabled;
    }

    public boolean isAddress1Enabled() {
        return isAddress1Enabled;
    }

    public void setAddress1Enabled(boolean address1Enabled) {
        this.isAddress1Enabled = address1Enabled;
    }

    public boolean isTownEnabled() {
        return isTownEnabled;
    }

    public void setTownEnabled(boolean townEnabled) {
        this.isTownEnabled = townEnabled;
    }

    public boolean isPostalCodeEnabled() {
        return isPostalCodeEnabled;
    }

    public void setPostalCodeEnabled(boolean postalCodeEnabled) {
        this.isPostalCodeEnabled = postalCodeEnabled;
    }

    public boolean isStateEnabled() {
        return isStateEnabled;
    }

    public void setStateEnabled(boolean stateEnabled) {
        this.isStateEnabled = stateEnabled;
    }


    public boolean isAddress2Enabled() {
        return isAddress2Enabled;
    }

    public void setAddress2Enabled(boolean address2Enabled) {
        this.isAddress2Enabled = address2Enabled;
    }

    public boolean isCountryEnabled() {
        return isCountryEnabled;
    }

    public void setCountryEnabled(boolean countryEnabled) {
        isCountryEnabled = countryEnabled;
    }
}
