package com.philips.cdp.registration.app.tagging;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.registration.ui.traditional.RegistrationActivity;

/**
 * Created by 310243576 on 8/11/2016.
 */
public class AppTaggingErrorsTest extends ActivityInstrumentationTestCase2<RegistrationActivity> {

    AppTaggingErrors appTaggingErrors;
    Context mContext;




    public AppTaggingErrorsTest() {
        super(RegistrationActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
        mContext = getInstrumentation().getTargetContext();
        appTaggingErrors = new AppTaggingErrors();


    }

    public void testAppTaggingErrors() {

        synchronized(this){//synchronized block

            try {


        }catch(Exception e){System.out.println(e);}
    }
    }
}
