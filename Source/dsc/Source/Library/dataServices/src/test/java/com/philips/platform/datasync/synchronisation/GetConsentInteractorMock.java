package com.philips.platform.datasync.synchronisation;

import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.catk.GetConsentInteractor;
import com.philips.platform.catk.error.ConsentNetworkError;
import com.philips.platform.catk.model.Consent;

/**
 * Created by Entreco on 13/12/2017.
 */

public class GetConsentInteractorMock extends GetConsentInteractor {

    public Consent getStatusForConsentType_returnConsent;
    public ConsentNetworkError getStatusForConsentType_returnError;
    public String getStatusForConsentType_consentType;

    public GetConsentInteractorMock(ConsentAccessToolKit consentAccessToolKit) {
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
