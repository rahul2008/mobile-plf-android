package com.philips.platform.datasync.consent;

import android.content.Context;

import com.philips.platform.dataservices.R;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

import java.util.Arrays;
import java.util.Collections;

public class DSCConsentProvider {
    public static ConsentDefinition fetchMomentConsentDefinition(Context context) {
        return new ConsentDefinition(context.getString(R.string.Moment_Consent_Text), context.getString(R.string.Moment_Consent_Help_Text),
                Collections.singletonList("moment"), 1);
    }

    public static ConsentDefinition fetchCoachingConsentDefinition(Context context) {
        return new ConsentDefinition(context.getString(R.string.Coaching_Consent_Text), context.getString(R.string.Coaching_Consent_Help_Text),
                Collections.singletonList("coaching"), 1);
    }

    public static ConsentDefinition fetchBinaryConsentDefinition(Context context) {
        return new ConsentDefinition(context.getString(R.string.Binary_Consent_Text), context.getString(R.string.Binary_Consent_Help_Text),
                Collections.singletonList("binary"), 1);
    }

    public static ConsentDefinition fetchResearchAndAnalyticsConsentDefinition(Context context) {
        return new ConsentDefinition(context.getString(R.string.Research_Analytics_Consent_Text), context.getString(R.string.Research_Analytics_Consent_Help_Text),
                Arrays.asList("research", "analytics"), 1);
    }
}
