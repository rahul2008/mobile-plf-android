package com.philips.cdp.registration.configuration;

import com.philips.cdp.registration.ui.utils.RLog;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;
@RunWith(MockitoJUnitRunner.class)
public class RegistrationLaunchModeTest extends TestCase {

    @Test
    public void shouldNotNull() {
        assertThat(RegistrationLaunchMode.MARKETING_OPT, is(notNullValue()));
        assertThat(RegistrationLaunchMode.USER_DETAILS, is(notNullValue()));
    }
}