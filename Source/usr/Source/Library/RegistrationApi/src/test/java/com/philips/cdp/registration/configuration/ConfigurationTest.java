package com.philips.cdp.registration.configuration;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

public class ConfigurationTest {
    @Test
    public void shouldNotNull() {
        assertThat(Configuration.STAGING, is(notNullValue()));
        assertThat(Configuration.DEVELOPMENT, is(notNullValue()));
        assertThat(Configuration.EVALUATION, is(notNullValue()));
        assertThat(Configuration.TESTING, is(notNullValue()));
        assertThat(Configuration.EVALUATION, is(notNullValue()));
    }
}