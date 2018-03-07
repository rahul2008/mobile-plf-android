package com.philips.platform.pif.chi;

import com.philips.platform.pif.chi.datamodel.ConsentState;

import java.util.List;

public interface FetchConsentTypesStateCallback {
    void onGetConsentsSuccess(List<ConsentState> consentStateList);

    void onGetConsentsFailed(ConsentError error);
}
