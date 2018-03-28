package com.philips.platform.appinfra.consentmanager;

import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.datamodel.ConsentDefinitionStatus;

import java.util.List;

public interface FetchConsentsCallback {
    void onGetConsentsSuccess(List<ConsentDefinitionStatus> consentDefinitionStatusList);

    void onGetConsentsFailed(ConsentError error);
}
