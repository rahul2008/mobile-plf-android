package com.philips.cdp.registration.consents;

import com.philips.cdp.registration.R;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

import java.util.ArrayList;

/**
 * provides Consent used in User Registration of USR_MARKETING key type
 * Use same key if proposition wants to changes consent definition
 *
 * @since 2018.1.0
 */

public class URConsentProvider {
    public static final String USR_MARKETING_CONSENT = "USR_MARKETING_CONSENT";

    /**
     * @return ConsentDefinition for USR_MARKETING
     * @since 2018.1.0
     */

    public static ConsentDefinition fetchMarketingConsentDefinition() {
        int text = R.string.DLS_OptIn_Promotional_Message_Line1;
        int helpText = R.string.DLS_PhilipsNews_Description_Text;
        final ArrayList<String> types = new ArrayList<>();
        types.add(USR_MARKETING_CONSENT);
        return new ConsentDefinition(text, helpText, types, 0);
    }
}
