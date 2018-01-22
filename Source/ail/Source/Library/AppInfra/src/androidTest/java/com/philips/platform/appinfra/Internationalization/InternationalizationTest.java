package com.philips.platform.appinfra.Internationalization;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInstrumentation;
import com.philips.platform.appinfra.internationalization.InternationalizationInterface;

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

    public void testgetCompleteUILocale() {
        String localeString = mInternationalizationInterface.getCompleteUILocale();
        assertNotNull(localeString);
        boolean containsUnderscore = localeString.contains("_");
        assert(containsUnderscore == true);
    }

}
