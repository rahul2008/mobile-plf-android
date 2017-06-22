package com.philips.platform.appinfra.Internationalization;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInstrumentation;
import com.philips.platform.appinfra.internationalization.InternationalizationInterface;

/**
 * Created by 310238655 on 11/16/2016.
 */

public class InternationalizationTest extends AppInfraInstrumentation {
    InternationalizationInterface mInternationalizationInterface = null;

    private Context context;
    private AppInfra mAppInfra;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        assertNotNull(context);
        mAppInfra = new AppInfra.Builder().build(context);
        mInternationalizationInterface = mAppInfra.getInternationalization();
        assertNotNull(mInternationalizationInterface);

    }

    public void testgetUILocale() {
        assertNotNull(mInternationalizationInterface.getUILocale());
    }

    public void testgetUILocaleString() {
        assertNotNull(mInternationalizationInterface.getUILocaleString());
    }

}
