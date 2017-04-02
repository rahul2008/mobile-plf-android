package com.philips.cdp.registration.ui.social;

/**
 * Created by philips on 3/31/17.
 */

public interface AlmostDoneContract {

    void handleUiAcceptTerms();

    void updateUiStatus();

    void handleTermsAndCondition();

    void hideAcceptTermsView();

    void updateTermsAndConditionView();

    boolean isTermsAndConditionAccepted();

    void updateReceiveMarktingView();

    boolean isReceiveMarketingEmail();
}
