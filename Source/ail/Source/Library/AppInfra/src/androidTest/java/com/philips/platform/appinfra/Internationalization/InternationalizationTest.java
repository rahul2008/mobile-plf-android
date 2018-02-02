package com.philips.platform.appinfra.Internationalization;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInstrumentation;
import com.philips.platform.appinfra.internationalization.InternationalizationInterface;

import android.content.Context;

/**
 * Internationalization Test class.
 */

public class InternationalizationTest extends AppInfraInstrumentation {
    InternationalizationInterface mInternationalizationInterface = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Context context = getInstrumentation().getContext();
        assertNotNull(context);
        AppInfra mAppInfra = new AppInfra.Builder().build(context);
        mInternationalizationInterface = mAppInfra.getInternationalization();
        assertNotNull(mInternationalizationInterface);

    }


    public void testgetUILocaleString() {
        assertNotNull(mInternationalizationInterface.getUILocaleString());
    }

    public void test_givenInterfaceCreated_whenGetBCP47UILocale_thenShouldReturnNonNull() {
        String localeString = mInternationalizationInterface.getBCP47UILocale();
        assertNotNull(localeString);
    }

    public void test_givenInterfaceCreated_whenGetBCP47UILocale_thenShouldReturnContainingUnderscore() {
        String localeString = mInternationalizationInterface.getBCP47UILocale();
        assertTrue(localeString.contains("_"));
    }

}
