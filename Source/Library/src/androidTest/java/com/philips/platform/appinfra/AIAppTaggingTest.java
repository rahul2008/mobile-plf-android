package com.philips.platform.appinfra;

import android.content.Context;

/**
 * Created by 310238655 on 4/29/2016.
 */
public class AIAppTaggingTest extends MockitoTestCase {

    private Context context;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();

        assertNotNull(context);
       
    }


}
