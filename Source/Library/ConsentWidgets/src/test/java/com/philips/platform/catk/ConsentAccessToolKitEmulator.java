package com.philips.platform.catk;


import com.philips.platform.catk.listener.ConsentResponseListener;
import com.philips.platform.catk.listener.CreateConsentListener;
import com.philips.platform.catk.model.Consent;

public class ConsentAccessToolKitEmulator extends ConsentAccessToolKit {

    public CatkInputs init_catkInputs;
    public ConfigCompletionListener init_listner;

    public String getStatusForConsentType_consentType;
    public int getStatusForConsentType_version;
    public ConsentResponseListener getStatusForConsentType_consentListener;


    @Override
    public void init(CatkInputs catkInputs, ConfigCompletionListener listner) {
        this.init_catkInputs = catkInputs;
        this.init_listner = listner;
        listner.onConfigurationCompletion();
    }

    @Override
    public void getStatusForConsentType(final String consentType, int version, final ConsentResponseListener consentListener) {
        this.getStatusForConsentType_consentListener = consentListener;
        this.getStatusForConsentType_consentType = consentType;
        this.getStatusForConsentType_version = version;
    }

    @Override
    public void createConsent(final Consent consent, final CreateConsentListener consentListener) {

    }

}
