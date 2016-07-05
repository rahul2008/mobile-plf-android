package com.philips.platform.appinfra.tagging;

import android.content.Context;

import com.philips.platform.appinfra.MockitoTestCase;

/**
 * Created by 310238655 on 4/29/2016.
 */
public class AppTaggingTest extends MockitoTestCase {

    private Context context;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();

        assertNotNull(context);
       
    }


}
