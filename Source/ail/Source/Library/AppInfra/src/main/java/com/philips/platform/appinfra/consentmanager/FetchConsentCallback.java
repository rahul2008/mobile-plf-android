package com.philips.platform.appinfra.consentmanager;

import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.datamodel.ConsentDefinitionStatus;

public interface FetchConsentCallback {

    void onGetConsentSuccess(ConsentDefinitionStatus consentDefinitionStatus);

    void onGetConsentFailed(ConsentError error);
}
