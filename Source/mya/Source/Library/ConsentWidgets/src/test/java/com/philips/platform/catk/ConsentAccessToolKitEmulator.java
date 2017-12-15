package com.philips.platform.catk;


import com.philips.platform.catk.listener.ConsentResponseListener;
import com.philips.platform.catk.listener.CreateConsentListener;
import com.philips.platform.consenthandlerinterface.datamodel.BackendConsent;

import java.util.List;

public class ConsentAccessToolKitEmulator extends ConsentAccessToolKit {

    public CatkInputs init_catkInputs;

    public String getStatusForConsentType_consentType;
    public int getStatusForConsentType_version;
    public ConsentResponseListener getStatusForConsentType_consentListener;


    @Override
    public void init(CatkInputs catkInputs) {
        this.init_catkInputs = catkInputs;
    }

    @Override
    public void getStatusForConsentType(final String consentType, int version, final ConsentResponseListener consentListener) {
        this.getStatusForConsentType_consentListener = consentListener;
        this.getStatusForConsentType_consentType = consentType;
        this.getStatusForConsentType_version = version;
    }

    @Override
    public void createConsent(final List<BackendConsent> consent, final CreateConsentListener consentListener) {

    }

}
