package com.philips.cdp.registration.apptagging;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.platform.appinfra.AppInfra;

import org.junit.Before;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 310243576 on 8/30/2016.
 */
public class AppTaggingTest extends InstrumentationTestCase{

    Context mContext;

    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
        super.setUp();
        mContext = getInstrumentation().getTargetContext();
    }

    public void testAppTagging(){

        synchronized(this){//synchronized block

                try{
                    RegistrationHelper.getInstance().
                            setAppInfraInstance(new AppInfra.Builder().build(mContext));
                }catch(Exception e){System.out.println(e);}
            }
//
//        AppTagging.trackPage("hello");
//        AppTagging.trackFirstPage("hello");
//        AppTagging.trackAction("hello","sample","sample");
//        Map<String,String> str = new HashMap<String, String>();
//        AppTagging.trackMultipleActions("sample",str);
//        AppTagging.pauseCollectingLifecycleData();
        assertNotNull(AppTagging.getCommonGoalsMap());
    }
}