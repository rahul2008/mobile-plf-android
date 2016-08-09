package com.philips.cdp.registration.Configuration;

import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.registration.configuration.Flow;
import com.philips.cdp.registration.configuration.JanRainConfiguration;
import com.philips.cdp.registration.configuration.RegistrationClientId;
import com.philips.cdp.registration.ui.traditional.RegistrationActivity;

import java.util.HashMap;

/**
 * Created by 310243576 on 8/5/2016.
 */
public class FlowTest extends ActivityInstrumentationTestCase2<RegistrationActivity> {


   private Flow flow;

    public FlowTest() {
        super(RegistrationActivity.class);
        flow = new Flow();

    }

    public void testFlowEmail() {

        flow.setEmailVerificationRequired(null);
        assertEquals(null, flow.isEmailVerificationRequired());

        flow.setEmailVerificationRequired(true);
        assertTrue(flow.isEmailVerificationRequired());

        flow.setEmailVerificationRequired(false);
        assertFalse(flow.isEmailVerificationRequired());

    }

    public void testFlowTermsAndConditionAcceptance() {

        flow.setTermsAndConditionsAcceptanceRequired(null);
        assertEquals(null, flow.isTermsAndConditionsAcceptanceRequired());

        flow.setTermsAndConditionsAcceptanceRequired(true);
        assertTrue(flow.isTermsAndConditionsAcceptanceRequired());

        flow.setTermsAndConditionsAcceptanceRequired(false);
        assertFalse(flow.isTermsAndConditionsAcceptanceRequired());

         }

    public void testMinAgeLimit() {
        assertEquals(0, flow.getMinAgeLimitByCountry("100"));

        flow.setMinAgeLimit(null);
        assertEquals(null, flow.getMinAgeLimit());

        HashMap<String, String> ageLimit = new HashMap<String, String>();
        ageLimit.put("100", "15");
        flow.setMinAgeLimit(ageLimit);

        assertEquals(ageLimit, flow.getMinAgeLimit());
        assertEquals(15, flow.getMinAgeLimitByCountry("100"));

    }
}
