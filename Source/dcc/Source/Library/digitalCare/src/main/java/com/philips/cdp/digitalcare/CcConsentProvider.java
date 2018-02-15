package com.philips.cdp.digitalcare;


import android.content.Context;

import com.philips.cdp.digitalcare.util.DigitalCareConstants;
import com.philips.cdp.digitalcare.R;
import com.philips.platform.mya.catk.device.DeviceStoredConsentHandler;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CcConsentProvider {

    private static DeviceStoredConsentHandler deviceStoredConsentHandler;

    public static ConsentDefinition fetchLocationConsentDefinitionFor(Context context, Locale locale){
        return new ConsentDefinition(context.getResources().getString(R.string.dcc_location_consent_definition_text), context.getResources().getString(R.string.dcc_location_consent_definition_help_text), getLocationConsentType(), DigitalCareConstants.CC_LOCATION_CONSENT_VERSION, locale);
    }

    private static List<String> getLocationConsentType(){
        List<String> types = new ArrayList<>();
        types.add(DigitalCareConstants.CC_CONSENT_TYPE_LOCATION);
        return types;
    }

    public static DeviceStoredConsentHandler fetchDeviceStoredConsentHandler(){
        if(deviceStoredConsentHandler == null){
            deviceStoredConsentHandler = new DeviceStoredConsentHandler(DigitalCareConfigManager.getInstance().getAPPInfraInstance());
        }
        return deviceStoredConsentHandler;
    }
}
