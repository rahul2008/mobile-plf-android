package com.philips.platform.appinfra.abtestclient;

import android.content.Context;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.MockitoTestCase;

/**
 * Created by 310243577 on 10/13/2016.
 */

public class ABTestClienTest extends MockitoTestCase {

    private ABTestClientInterface mAbTestClientInterface;
    private AppInfra mAppInfra;
    private Context mContext;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContext = getInstrumentation().getContext();
        assertNotNull(mContext);

        mAppInfra = new AppInfra.Builder().build(mContext);
        assertNotNull(mAppInfra);

    }
}
