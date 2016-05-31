/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.Configuration;

import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.registration.configuration.JanRainConfiguration;
import com.philips.cdp.registration.configuration.RegistrationClientId;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.configuration.RegistrationDynamicConfiguration;
import com.philips.cdp.registration.configuration.RegistrationStaticConfiguration;
import com.philips.cdp.registration.ui.traditional.RegistrationActivity;

/**
 * Created by vinayak on 28/01/16.
 */
public class RegistrationJanrainConfigurationTest extends ActivityInstrumentationTestCase2<RegistrationActivity> {

    //Constructor for instrimental test case
    public RegistrationJanrainConfigurationTest() {
        super(RegistrationActivity.class);
    }
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        String CONFIGURATION_JSON_PATH = "registration/configuration/configuration_for_test_cases.json";
        RegistrationStaticConfiguration.getInstance().parseConfigurationJson(getInstrumentation().getTargetContext(), CONFIGURATION_JSON_PATH);
    }


    public void testJanRainConfigurationFlieldsWithStatic() {
       JanRainConfiguration janRainConfiguration = RegistrationConfiguration.getInstance().getJanRainConfiguration();

        if(!janRainConfiguration.getClientIds().getDevelopmentId().equalsIgnoreCase("ad7nn99y2mv5berw5jxewzagazafbyhu")){
            assertTrue(false);
        }

        if(!janRainConfiguration.getClientIds().getTestingId().equalsIgnoreCase("xru56jcnu3rpf8q7cgnkr7xtf9sh8pp7")){
            assertTrue(false);
        }

        if(!janRainConfiguration.getClientIds().getEvaluationId().equalsIgnoreCase("4r36zdbeycca933nufcknn2hnpsz6gxu")){
            assertTrue(false);
        }

        if(!janRainConfiguration.getClientIds().getProductionId().equalsIgnoreCase("mz6tg5rqrg4hjj3wfxfd92kjapsrdhy3")){
            assertTrue(false);
        }
        assertTrue(true);

    }

    public void testJanRainConfigurationFlieldsWithDynamicMerge() {

        RegistrationClientId registrationClientId = new RegistrationClientId();
        registrationClientId.setEvaluationId("4r36zdbeycca933nufcknn2hnpsz6gxu==");
        RegistrationDynamicConfiguration.getInstance().getJanRainConfiguration().setClientIds(registrationClientId);

        JanRainConfiguration janRainConfiguration = RegistrationConfiguration.getInstance().getJanRainConfiguration();

        if(!janRainConfiguration.getClientIds().getDevelopmentId().equalsIgnoreCase("ad7nn99y2mv5berw5jxewzagazafbyhu")){
            assertTrue(false);
        }

        if(!janRainConfiguration.getClientIds().getTestingId().equalsIgnoreCase("xru56jcnu3rpf8q7cgnkr7xtf9sh8pp7")){
            assertTrue(false);
        }

        if(!janRainConfiguration.getClientIds().getEvaluationId().equalsIgnoreCase("4r36zdbeycca933nufcknn2hnpsz6gxu==")){
            assertTrue(false);
        }

        if(!janRainConfiguration.getClientIds().getProductionId().equalsIgnoreCase("mz6tg5rqrg4hjj3wfxfd92kjapsrdhy3")){
            assertTrue(false);
        }
        assertTrue(true);
        RegistrationDynamicConfiguration.getInstance().resetDynamicConfiguration();
    }

    public void testJanRainConfigurationFlieldsOnlyDynamic() {

        RegistrationStaticConfiguration.getInstance().getJanRainConfiguration().setClientIds(null);

        RegistrationClientId registrationClientId = new RegistrationClientId();
        registrationClientId.setEvaluationId("4r36zdbeycca933nufcknn2hnpsz6gxu");
        RegistrationDynamicConfiguration.getInstance().getJanRainConfiguration().setClientIds(registrationClientId);

        JanRainConfiguration janRainConfiguration = RegistrationConfiguration.getInstance().getJanRainConfiguration();



        if(!janRainConfiguration.getClientIds().getEvaluationId().equalsIgnoreCase("4r36zdbeycca933nufcknn2hnpsz6gxu")){
            assertTrue(true);
        }

        RegistrationDynamicConfiguration.getInstance().resetDynamicConfiguration();
    }

}
