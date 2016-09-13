package com.philips.platform.appinfra.rest;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.MockitoTestCase;

/**
 * Created by 310238114 on 9/13/2016.
 */
public class RestClientTest extends MockitoTestCase {

    private Context context;
    private AppInfra mAppInfra;
    private RestInterface mRestInterface;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();

        assertNotNull(context);
        mAppInfra = new AppInfra.Builder().build(context);
        mRestInterface = mAppInfra.getRestClient();

    }

   void testRequestQueue(){

    }

}
