package com.philips.cdp.registration.coppa.base;

import android.test.InstrumentationTestCase;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Created by 310230979  on 8/30/2016.
 */
public class CoppaStatusTest extends InstrumentationTestCase{


    @Test
    public void testConfiguration() {
        assertThat(CoppaStatus.valueOf("kDICOPPAConsentPending"), is(notNullValue()));
        assertThat(CoppaStatus.valueOf("kDICOPPAConsentNotGiven"), is(notNullValue()));
        assertThat(CoppaStatus.valueOf("kDICOPPAConsentGiven"), is(notNullValue()));
        assertThat(CoppaStatus.valueOf("kDICOPPAConfirmationPending"), is(notNullValue()));
        assertThat(CoppaStatus.valueOf("kDICOPPAConfirmationNotGiven"), is(notNullValue()));
        assertThat(CoppaStatus.valueOf("kDICOPPAConfirmationGiven"), is(notNullValue()));
    }
}