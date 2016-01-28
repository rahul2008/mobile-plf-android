package com.philips.cdp.registration.Configuration;

import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.configuration.Flow;
import com.philips.cdp.registration.configuration.PILConfiguration;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.configuration.RegistrationDynamicConfiguration;
import com.philips.cdp.registration.configuration.RegistrationStaticConfiguration;
import com.philips.cdp.registration.ui.traditional.RegistrationActivity;

import java.util.HashMap;

/**
 * Created by vinayak on 28/01/16.
 */
public class RegistrationFlowConfigurationTest extends ActivityInstrumentationTestCase2<RegistrationActivity> {

    //Constructor for instrimental test case
    public RegistrationFlowConfigurationTest() {
        super(RegistrationActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        String CONFIGURATION_JSON_PATH = "registration/configuration/configuration_for_test_cases.json";
        RegistrationStaticConfiguration.getInstance().parseConfigurationJson(getInstrumentation().getTargetContext(), CONFIGURATION_JSON_PATH);
    }


    public void testFlowConfigurationFlieldsWithStatic() {

        Flow flowConfiguration = RegistrationConfiguration.getInstance().getFlow();

        if (!flowConfiguration.isEmailVerificationRequired()) {
            assertTrue(false);
        }

        if (!flowConfiguration.isTermsAndConditionsAcceptanceRequired()) {
            assertTrue(false);
        }

        if (!flowConfiguration.getMinAgeLimit().get("NL").equalsIgnoreCase("12")) {
            assertTrue(false);
        }

        if (!flowConfiguration.getMinAgeLimit().get("GB").equalsIgnoreCase("0")) {
            assertTrue(false);
        }

        if (!flowConfiguration.getMinAgeLimit().get("default").equalsIgnoreCase("16")) {
            assertTrue(false);
        }

        if (!(flowConfiguration.getMinAgeLimitByCountry("IN") == 16)) {
            assertTrue(false);
        }

        assertTrue(true);
    }

    public void testFlowConfigurationFlieldsWithDynamicMerge() {

        RegistrationDynamicConfiguration.getInstance().getFlow().setTermsAndConditionsAcceptanceRequired(false);
        RegistrationDynamicConfiguration.getInstance().getFlow().setTermsAndConditionsAcceptanceRequired(false);
        //Age mapping
        HashMap<String, String> map = new HashMap<>();
        map.put("NL", "40");
        map.put("GB", "20");
        map.put("IN", "80");
        RegistrationDynamicConfiguration.getInstance().getFlow().setMinAgeLimit(map);
        Flow flowConfiguration = RegistrationConfiguration.getInstance().getFlow();

        if (flowConfiguration.isEmailVerificationRequired()) {
            assertTrue(false);
        }

        if (flowConfiguration.isTermsAndConditionsAcceptanceRequired()) {
            assertTrue(false);
        }

        if (!flowConfiguration.getMinAgeLimit().get("NL").equalsIgnoreCase("40")) {
            assertTrue(false);
        }

        if (!flowConfiguration.getMinAgeLimit().get("GB").equalsIgnoreCase("20")) {
            assertTrue(false);
        }

        if (!flowConfiguration.getMinAgeLimit().get("IN").equalsIgnoreCase("80")) {
            assertTrue(false);
        }

        assertTrue(true);

        RegistrationDynamicConfiguration.getInstance().resetDynamicConfiguration();
    }

    public void testFlowConfigurationFlieldsOnlyDynamic() {

        RegistrationStaticConfiguration.getInstance().setFlow(null);

        RegistrationDynamicConfiguration.getInstance().getFlow().setEmailVerificationRequired(false);
        RegistrationDynamicConfiguration.getInstance().getFlow().setTermsAndConditionsAcceptanceRequired(false);
        //Age mapping
        HashMap<String, String> map = new HashMap<>();
        map.put("NL", "40");
        map.put("GB", "20");
        map.put("IN", "80");
        RegistrationDynamicConfiguration.getInstance().getFlow().setMinAgeLimit(map);
        Flow flowConfiguration = RegistrationConfiguration.getInstance().getFlow();


        if (flowConfiguration.isEmailVerificationRequired()) {
            assertTrue(false);
        }

        if (flowConfiguration.isTermsAndConditionsAcceptanceRequired()) {
            assertTrue(false);
        }

        if (!flowConfiguration.getMinAgeLimit().get("NL").equalsIgnoreCase("40")) {
            assertTrue(false);
        }

        if (!flowConfiguration.getMinAgeLimit().get("GB").equalsIgnoreCase("20")) {
            assertTrue(false);
        }

        if (!flowConfiguration.getMinAgeLimit().get("IN").equalsIgnoreCase("80")) {
            assertTrue(false);
        }

        assertTrue(true);

        RegistrationDynamicConfiguration.getInstance().resetDynamicConfiguration();
    }

}
