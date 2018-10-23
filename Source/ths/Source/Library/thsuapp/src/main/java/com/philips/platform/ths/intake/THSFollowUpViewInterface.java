package com.philips.platform.ths.intake;

import com.philips.platform.ths.base.THSBaseView;

interface THSFollowUpViewInterface extends THSBaseView{

    boolean validatePhoneNumber();
    void showNoticeOfPrivacyFragment();
    String getConsumerPhoneNumber();
    void startProgressButton();
    void hideProgressButton();
    void showProviderDetailsFragment();
    void showConditionsFragment();
    void showInvalidPhoneNumberToast(String message);
    void showInlineError();
    void hideInlineError();
    void showError(String error);
}
