package com.philips.cdp.digitalcare;


import android.content.Context;

import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.util.DigitalCareConstants;
import com.philips.platform.appinfra.consentmanager.ConsentManagerInterface;
import com.philips.platform.mya.catk.device.DeviceStoredConsentHandler;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

import java.util.ArrayList;
import java.util.List;

public class CcConsentProvider {


    public static ConsentDefinition fetchLocationConsentDefinition(Context context) {
        return new ConsentDefinition(context.getResources().getString(R.string.dcc_location_consent_definition_text), context.getResources().getString(R.string.dcc_location_consent_definition_help_text), getLocationConsentType(), DigitalCareConstants.CC_LOCATION_CONSENT_VERSION);
    }

    private static List<String> getLocationConsentType() {
        List<String> types = new ArrayList<>();
        types.add(DigitalCareConstants.CC_CONSENT_TYPE_LOCATION);
        return types;
    }

    public static ConsentHandlerInterface fetchDeviceStoredConsentHandler() {
        return new DeviceStoredConsentHandler(DigitalCareConfigManager.getInstance().getAPPInfraInstance());
    }

    public static ConsentManagerInterface fetchConsentManager() {
        return DigitalCareConfigManager.getInstance().getAPPInfraInstance().getConsentManager();
    }
}
