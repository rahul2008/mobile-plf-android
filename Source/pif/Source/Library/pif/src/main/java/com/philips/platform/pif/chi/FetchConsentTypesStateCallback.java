package com.philips.platform.pif.chi;

import java.util.List;

public interface FetchConsentTypesStateCallback {
    void onGetConsentsSuccess(List<ConsentTypeState> consentTypeState);

    void onGetConsentsFailed(ConsentError error);
}
