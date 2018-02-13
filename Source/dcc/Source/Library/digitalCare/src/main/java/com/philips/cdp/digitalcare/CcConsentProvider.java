package com.philips.cdp.digitalcare;


import android.content.Context;

import com.philips.cdp.digitalcare.util.DigitalCareConstants;
import com.philips.platform.mya.chi.datamodel.ConsentDefinition;
import com.philips.cdp.digitalcare.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CcConsentProvider {

    public static ConsentDefinition fetchLocationConsentDefinitionFor(Context context, Locale locale){
        return new ConsentDefinition(context.getResources().getString(R.string.dcc_location_consent_definition_text), context.getResources().getString(R.string.dcc_location_consent_definition_help_text), getLocationConsentType(), DigitalCareConstants.CC_LOCATION_CONSENT_VERSION, locale);
    }

    private static List<String> getLocationConsentType(){
        List<String> types = new ArrayList<>();
        types.add(DigitalCareConstants.CC_CONSENT_TYPE_LOCATION);
        return types;
    }
}
