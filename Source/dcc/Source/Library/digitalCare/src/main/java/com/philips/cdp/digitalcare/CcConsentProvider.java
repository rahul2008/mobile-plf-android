package com.philips.cdp.digitalcare;


import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.util.DigitalCareConstants;
import com.philips.platform.appinfra.consentmanager.ConsentManagerInterface;
import com.philips.platform.mya.catk.device.DeviceStoredConsentHandler;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;


public class CcConsentProvider {


    public static ConsentDefinition fetchLocationConsentDefinition() {
        return new ConsentDefinition(R.string.dcc_location_consent_definition_text, R.string.dcc_location_consent_definition_help_text, getLocationConsentType(), DigitalCareConstants.CC_LOCATION_CONSENT_VERSION);
    }

    public static ConsentHandlerInterface fetchDeviceStoredConsentHandler() {
        return new DeviceStoredConsentHandler(DigitalCareConfigManager.getInstance().getAPPInfraInstance());
    }

    public static ConsentManagerInterface fetchConsentManager() {
        return DigitalCareConfigManager.getInstance().getAPPInfraInstance().getConsentManager();
    }
}
