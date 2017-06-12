package com.philips.cdp.registration.coppa.base;

import android.support.multidex.MultiDex;
import android.test.InstrumentationTestCase;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;


/**
 * Created by 310230979  on 8/31/2016.
 */
public class CoppaEmailStausTest  extends InstrumentationTestCase {

    Consent mConsent;

    @Override
    protected void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        super.setUp();

        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
        mConsent = new Consent();
    }
    @Test
    public void testConfiguration() {
        MultiDex.install(getInstrumentation().getTargetContext());
        assertThat(CoppaEmailStaus.valueOf("kDICOPPAConsentEmailSent"), is(notNullValue()));
        assertThat(CoppaEmailStaus.valueOf("kDICOPPAConfirmationEmailSent"), is(notNullValue()));
    }
}