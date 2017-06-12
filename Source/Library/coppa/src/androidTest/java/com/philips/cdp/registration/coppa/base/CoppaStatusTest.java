package com.philips.cdp.registration.coppa.base;

import android.support.multidex.MultiDex;

import com.philips.cdp.registration.coppa.RegistrationApiInstrumentationBase;

import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;


public class CoppaStatusTest extends RegistrationApiInstrumentationBase {


    @Test
    public void testConfiguration() {
        MultiDex.install(getInstrumentation().getTargetContext());
        assertThat(CoppaStatus.valueOf("kDICOPPAConsentPending"), is(notNullValue()));
        assertThat(CoppaStatus.valueOf("kDICOPPAConsentNotGiven"), is(notNullValue()));
        assertThat(CoppaStatus.valueOf("kDICOPPAConsentGiven"), is(notNullValue()));
        assertThat(CoppaStatus.valueOf("kDICOPPAConfirmationPending"), is(notNullValue()));
        assertThat(CoppaStatus.valueOf("kDICOPPAConfirmationNotGiven"), is(notNullValue()));
        assertThat(CoppaStatus.valueOf("kDICOPPAConfirmationGiven"), is(notNullValue()));
    }
}