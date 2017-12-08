package com.philips.cdp.registration.coppa.base;

import android.support.multidex.MultiDex;

import com.philips.cdp.registration.coppa.RegistrationApiInstrumentationBase;

import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;


public class CoppaEmailStausTest  extends RegistrationApiInstrumentationBase {


    @Test
    public void testConfiguration() {
        MultiDex.install(getInstrumentation().getTargetContext());
        assertThat(CoppaEmailStaus.valueOf("kDICOPPAConsentEmailSent"), is(notNullValue()));
        assertThat(CoppaEmailStaus.valueOf("kDICOPPAConfirmationEmailSent"), is(notNullValue()));
    }
}