package com.philips.cdp.registration.coppa.base;

import android.test.InstrumentationTestCase;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;


/**
 * Created by 310230979  on 8/31/2016.
 */
public class CoppaEmailStausTest  extends InstrumentationTestCase {


    @Test
    public void testConfiguration() {
        assertThat(CoppaEmailStaus.valueOf("kDICOPPAConsentEmailSent"), is(notNullValue()));
        assertThat(CoppaEmailStaus.valueOf("kDICOPPAConfirmationEmailSent"), is(notNullValue()));
    }
}