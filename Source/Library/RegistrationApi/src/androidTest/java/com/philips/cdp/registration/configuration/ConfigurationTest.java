package com.philips.cdp.registration.configuration;

import android.test.InstrumentationTestCase;

import org.junit.Test;


/**
 * Created by 310230979  on 8/30/2016.
 */
public class ConfigurationTest extends InstrumentationTestCase {
    @Test
    public void testConfiguration() {
        assertEquals("STAGING", Configuration.STAGING.getValue());
        assertEquals("EVALUATION", Configuration.EVALUATION.getValue());
        assertEquals("TESTING", Configuration.TESTING.getValue());
        assertEquals("PRODUCTION", Configuration.PRODUCTION.getValue());
        assertEquals("DEVELOPMENT", Configuration.DEVELOPMENT.getValue());
    }
}