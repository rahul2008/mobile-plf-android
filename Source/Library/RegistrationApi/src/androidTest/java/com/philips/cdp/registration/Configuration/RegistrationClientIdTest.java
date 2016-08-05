package com.philips.cdp.registration.Configuration;

import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.registration.configuration.RegistrationClientId;
import com.philips.cdp.registration.ui.traditional.RegistrationActivity;

/**
 * Created by 310243576 on 8/5/2016.
 */
public class RegistrationClientIdTest extends ActivityInstrumentationTestCase2<RegistrationActivity> {
    private RegistrationClientId registrationClientId;

    public RegistrationClientIdTest() {
        super(RegistrationActivity.class);
        registrationClientId = new RegistrationClientId();

    }

    public void testTestingIdTest() {
        registrationClientId.setTestingId(null);
        assertEquals(null, registrationClientId.getTestingId());
        registrationClientId.setTestingId("");
        assertEquals("", registrationClientId.getTestingId());
        registrationClientId.setTestingId("id12345");
        assertEquals("id12345", registrationClientId.getTestingId());

    }


    public void testDevelopmentId() {

        registrationClientId.setDevelopmentId(null);
        assertEquals(null, registrationClientId.getDevelopmentId());
        registrationClientId.setDevelopmentId("");
        assertEquals("", registrationClientId.getDevelopmentId());
        registrationClientId.setDevelopmentId("id12345");
        assertEquals("id12345", registrationClientId.getDevelopmentId());

    }

    public void testEvaluationIdTestTest() {
        registrationClientId.setEvaluationId(null);
        assertEquals(null, registrationClientId.getEvaluationId());
        registrationClientId.setEvaluationId("");
        assertEquals("", registrationClientId.getEvaluationId());
        registrationClientId.setEvaluationId("id12345");
        assertEquals("id12345", registrationClientId.getEvaluationId());

    }

    public void testProductionIdTest() {
        registrationClientId.setProductionId(null);
        assertEquals(null, registrationClientId.getProductionId());
        registrationClientId.setProductionId("");
        assertEquals("", registrationClientId.getProductionId());
        registrationClientId.setProductionId("id12345");
        assertEquals("id12345", registrationClientId.getProductionId());

    }


    public void testStagingIdTest() {
        registrationClientId.setStagingId(null);
        assertEquals(null, registrationClientId.getStagingId());
        registrationClientId.setStagingId("");
        assertEquals("", registrationClientId.getStagingId());
        registrationClientId.setStagingId("id12345");
        assertEquals("id12345", registrationClientId.getStagingId());
    }
}
