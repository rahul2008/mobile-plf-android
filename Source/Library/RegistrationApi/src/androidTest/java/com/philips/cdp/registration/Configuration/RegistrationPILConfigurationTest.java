/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.Configuration;

import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.configuration.JanRainConfiguration;
import com.philips.cdp.registration.configuration.PILConfiguration;
import com.philips.cdp.registration.configuration.RegistrationClientId;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.configuration.RegistrationDynamicConfiguration;
import com.philips.cdp.registration.configuration.RegistrationStaticConfiguration;
import com.philips.cdp.registration.ui.traditional.RegistrationActivity;

/**
 * Created by vinayak on 28/01/16.
 */
public class RegistrationPILConfigurationTest extends ActivityInstrumentationTestCase2<RegistrationActivity> {

    //Constructor for instrimental test case
    public RegistrationPILConfigurationTest() {
        super(RegistrationActivity.class);
    }
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        String CONFIGURATION_JSON_PATH = "registration/configuration/configuration_for_test_cases.json";
        RegistrationStaticConfiguration.getInstance().parseConfigurationJson(getInstrumentation().getTargetContext(), CONFIGURATION_JSON_PATH);
    }




    public void testPILConfigurationFlieldsWithStatic() {
       PILConfiguration pilConfiguration = RegistrationConfiguration.getInstance().getPilConfiguration();

        if(!pilConfiguration.getMicrositeId().equalsIgnoreCase("77000")){
           assertTrue(false);
        }

        if(!pilConfiguration.getRegistrationEnvironment().equalsIgnoreCase("Evaluation")){
            assertTrue(false);
        }

        if(pilConfiguration.getCampaignID() != null){
            assertTrue(false);
        }

        assertTrue(true);
    }

    public void testPILConfigurationFlieldsWithDynamicMerge() {

        RegistrationDynamicConfiguration.getInstance().getPilConfiguration().setCampaignID("CL20150501_PC_TB_COPPA");
        RegistrationDynamicConfiguration.getInstance().getPilConfiguration().setMicrositeId("test");
        RegistrationDynamicConfiguration.getInstance().getPilConfiguration().setRegistrationEnvironment(Configuration.STAGING);



        if(!RegistrationConfiguration.getInstance().getPilConfiguration().getMicrositeId().equalsIgnoreCase("test")){
            assertTrue(false);
        }

        if(!RegistrationConfiguration.getInstance().getPilConfiguration().getRegistrationEnvironment().equalsIgnoreCase("Staging")){
            assertTrue(false);
        }

        if(RegistrationConfiguration.getInstance().getPilConfiguration().getCampaignID().equalsIgnoreCase("CL20150501_PC_TB_COPPA")){
            assertTrue(true);
        }
        assertTrue(true);

        RegistrationDynamicConfiguration.getInstance().resetDynamicConfiguration();
    }

    public void testPILConfigurationFlieldsOnlyDynamic() {

        RegistrationStaticConfiguration.getInstance().setPilConfiguration(null);

        RegistrationDynamicConfiguration.getInstance().getPilConfiguration().setCampaignID("CL20150501_PC_TB_COPPAwefef");
        RegistrationDynamicConfiguration.getInstance().getPilConfiguration().setMicrositeId("test");
        RegistrationDynamicConfiguration.getInstance().getPilConfiguration().setRegistrationEnvironment(Configuration.STAGING);




        if(!RegistrationConfiguration.getInstance().getPilConfiguration().getMicrositeId().equalsIgnoreCase("test")){
            assertTrue(false);
        }

        if(!RegistrationConfiguration.getInstance().getPilConfiguration().getRegistrationEnvironment().equalsIgnoreCase("Staging")){
            assertTrue(false);
        }

        if(RegistrationConfiguration.getInstance().getPilConfiguration().getCampaignID().equalsIgnoreCase("CL20150501_PC_TB_COPPAwefef")){
            assertTrue(true);
        }
        assertTrue(true);

        RegistrationDynamicConfiguration.getInstance().resetDynamicConfiguration();
    }

}
