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
import com.philips.cdp.registration.configuration.RegistrationBaseConfiguration;
import com.philips.cdp.registration.configuration.RegistrationClientId;
import com.philips.cdp.registration.ui.traditional.RegistrationActivity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by vinayak on 28/01/16.
 */
public class RegistrationBaseConfigurationTest extends ActivityInstrumentationTestCase2<RegistrationActivity> {

    //Constructor for instrimental test case
    public RegistrationBaseConfigurationTest() {
        super(RegistrationActivity.class);
    }

    public void testConvertStreamToString() {

        String testingString = "Hello stream";
        InputStream inputStream = new ByteArrayInputStream( testingString.getBytes() );

        RegistrationBaseConfiguration registrationBaseConfiguration = new RegistrationBaseConfiguration();




        assertEquals(testingString, registrationBaseConfiguration.convertStreamToString(inputStream));

    }


}
