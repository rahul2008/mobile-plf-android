package com.philips.platform.datasync.synchronisation;

import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.catk.ConsentInteractor;
import com.philips.platform.catk.error.ConsentNetworkError;
import com.philips.platform.catk.model.Consent;

/**
 * Created by Entreco on 13/12/2017.
 */

public class ConsentInteractorMock extends ConsentInteractor {

    public Consent getStatusForConsentType_returnConsent;
    public ConsentNetworkError getStatusForConsentType_returnError;
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
