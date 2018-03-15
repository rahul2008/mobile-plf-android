package com.philips.cdp.digitalcare;


import android.content.Context;

import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.util.DigitalCareConstants;
import com.philips.platform.appinfra.consentmanager.ConsentManagerInterface;
import com.philips.platform.mya.catk.device.DeviceStoredConsentHandler;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

import java.util.Collections;

public class CcConsentProvider {

    public static ConsentDefinition fetchLocationConsentDefinition(Context context) {
        return new ConsentDefinition(context.getResources().getString(R.string.dcc_location_consent_definition_text)
                , context.getResources().getString(R.string.dcc_location_consent_definition_help_text)
                , Collections.singletonList(DigitalCareConstants.CC_CONSENT_TYPE_LOCATION)
                , DigitalCareConstants.CC_LOCATION_CONSENT_VERSION);
    }

    public static ConsentHandlerInterface fetchDeviceStoredConsentHandler() {
        return new DeviceStoredConsentHandler(DigitalCareConfigManager.getInstance().getAPPInfraInstance());
    }

    public static ConsentManagerInterface fetchConsentManager() {
        return DigitalCareConfigManager.getInstance().getAPPInfraInstance().getConsentManager();
    }
}
