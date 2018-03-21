package com.philips.cdp.registration.consents;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.registration.R;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

import java.util.ArrayList;
import java.util.Locale;

/**
 * provides Consent used in User Registration of USR_MARKETING key type
 * Use same key if proposition wants to changes consent definition
 *
 * @since 2018.1.0
 */

public class URConsentProvider {
    public static final String USR_MARKETING_CONSENT = "USR_MARKETING_CONSENT";

    /**
     * @param pContext pass instance of context
     * @return ConsentDefinition for USR_MARKETING
     * @since 1.0.0
     */

    public static ConsentDefinition fetchMarketingConsentDefinition(@NonNull Context pContext) {
        String text = pContext.getString(R.string.reg_DLS_OptIn_Promotional_Message_Line1);
        String helpText = pContext.getString(R.string.reg_DLS_PhilipsNews_Description_Text);
        final ArrayList<String> types = new ArrayList<>();
        types.add(USR_MARKETING_CONSENT);
        return new ConsentDefinition(text, helpText, types, 1);
    }
}
