package com.philips.cdp.di.iap.screens;

/**
 * Created by philips on 2/4/19.
 */

public interface AddressFieldDecider {

    boolean isHouseNoEnabled();
    boolean isStateEnabled();
    boolean isSalutationEnabled();
    boolean isFirstNameEnabled();
    boolean isLastNameEnabled();
    boolean isPhoneNumberEnabled();
    boolean isAddressLineOneEnabled();
    boolean isAddressLineTwoEnabled();
    boolean isTownEnabled();
    boolean isPostalCodeEnabled();
    boolean isCountryEnabled();
    boolean isEmailEnabled();

}
