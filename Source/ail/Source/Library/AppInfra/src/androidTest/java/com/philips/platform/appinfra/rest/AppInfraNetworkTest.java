package com.philips.platform.appinfra.rest;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInstrumentation;

import org.junit.Before;

import static java.lang.Thread.sleep;
import static org.junit.Assert.*;


public class AppInfraNetworkTest extends AppInfraInstrumentation {

    private RestInterface mRestInterface = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Context context = getInstrumentation().getContext();
        assertNotNull(context);
        AppInfra mAppInfra = new AppInfra.Builder().build(context);
        assertNotNull(mAppInfra);
        mRestInterface = mAppInfra.getRestClient();
        assertNotNull(mRestInterface);
    }

    private void waitForResponse() {
        try {
            sleep(750);
        } catch (InterruptedException e) {

        }
    }


}