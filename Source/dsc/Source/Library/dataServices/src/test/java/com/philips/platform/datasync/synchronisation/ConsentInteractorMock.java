package com.philips.platform.datasync.synchronisation;

import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.catk.ConsentInteractor;
import com.philips.platform.consenthandlerinterface.ConsentCallback;
import com.philips.platform.consenthandlerinterface.ConsentError;
import com.philips.platform.consenthandlerinterface.datamodel.Consent;

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
}
