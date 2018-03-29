package com.philips.platform.ths.payment;


import com.americanwell.sdk.entity.Address;

public interface THSCreditCardDetailViewInterface {
    void updateCreditCardDetails(THSPaymentMethod thsPaymentMethod);
    void showCvvDetail(final boolean showLargeContent, final boolean isWithTitle, final boolean showIcon);
    void updateAddress(Address address);
    void updateCheckBoxState(boolean isEnabled);
}
