package com.philips.platform.ths.consent;
/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.ths.R;

import java.util.ArrayList;

public class THSLocationConsentProvider {

    public final static String THS_LOCATION = "THS_location";

    public static ConsentDefinition getTHSConsentDefinition() {
        int text = R.string.ths_consent_privacy_text;
        int helpText = R.string.ths_consent_description_text;
        final ArrayList<String> types = new ArrayList<>();
        types.add(THS_LOCATION);
        return new ConsentDefinition(text, helpText, types, 1);
    }
}
