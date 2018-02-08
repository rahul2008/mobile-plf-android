/*
 * Copyright (c) Koninklijke Philips N.V., 2018.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.datasync.synchronisation;

import com.philips.platform.mya.catk.ConsentAccessToolKit;
import com.philips.platform.mya.catk.ConsentInteractor;
import com.philips.platform.mya.chi.CheckConsentsCallback;
import com.philips.platform.mya.chi.ConsentCallback;
import com.philips.platform.mya.chi.ConsentError;
import com.philips.platform.mya.chi.datamodel.Consent;
import com.philips.platform.mya.chi.datamodel.ConsentDefinition;

public class ConsentInteractorMock extends ConsentInteractor {

    public Consent getStatusForConsentType_returnConsent;
    public ConsentError getStatusForConsentType_returnError;
    public String getStatusForConsentType_consentType;

    public ConsentInteractorMock(ConsentAccessToolKit consentAccessToolKit) {
        super(consentAccessToolKit);
    }

    @Override
    public void getStatusForConsentType(final String consentType, ConsentCallback callback) {
        getStatusForConsentType_consentType = consentType;
        if (getStatusForConsentType_returnConsent != null) {
            callback.onGetConsentRetrieved(getStatusForConsentType_returnConsent);
        } else {
            callback.onGetConsentFailed(getStatusForConsentType_returnError);
        }
    }

    @Override
    public void fetchConsentState(ConsentDefinition consentDefinition, CheckConsentsCallback callback) {

    }
}
