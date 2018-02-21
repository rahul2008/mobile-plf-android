package com.philips.platform.ths.consent;
/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

import android.content.Context;


import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.ths.R;
import com.philips.platform.ths.utility.THSManager;

import java.util.ArrayList;
import java.util.Locale;

public class THSLocationConsentProvider {

  // ConsentDefinition mConsentDefinition;

    public final static String THS_LOCATION = "THS_LOCATION";

    public static ConsentDefinition getTHSConsentDefinition(Context pContext, Locale pLocale) {
        String text = pContext.getString(R.string.ths_location_consent_title);
        String helpText = pContext.getString(R.string.ths_location_consent_help);
        final ArrayList<String> types = new ArrayList<>();
        types.add(THS_LOCATION);
        return new ConsentDefinition(text, helpText, types, 1, pLocale);
    }

    public static Locale getCompleteLocale() {
        Locale locale = Locale.US;
        if (THSManager.getInstance().getAppInfra() != null && THSManager.getInstance().getAppInfra().getInternationalization() != null && THSManager.getInstance().getAppInfra().getInternationalization().getUILocaleString() != null) {
            String[] localeComponents = THSManager.getInstance().getAppInfra().getInternationalization().getBCP47UILocale().split("-");
            if (localeComponents.length == 2) {
                locale = new Locale(localeComponents[0], localeComponents[1]);
            }
        }
        return locale;
    }
}
