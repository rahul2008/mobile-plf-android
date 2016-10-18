package com.philips.cdp.registration.ui.utils;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.platform.appinfra.AppInfra;

import org.junit.Before;

/**
 * Created by 310243576 on 9/6/2016.
 */
public class RLogTest extends InstrumentationTestCase{
    RLog rLog;

    Context context;
    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
//        MockitoAnnotations.initMocks(this);
        super.setUp();
        rLog = new RLog();
        context = getInstrumentation().getTargetContext();
        synchronized(this){//synchronized block

            try{
                RegistrationHelper.getInstance().setAppInfraInstance(new AppInfra.Builder().build(context));


            }catch(Exception e){System.out.println(e);}
        }
        RLog.init(context);

    }
    public void testRLog(){
        assertTrue(RLog.isLoggingEnabled());
        RLog.enableLogging();
        assertTrue(RLog.isLoggingEnabled());
        RLog.d("tag","message");
        RLog.e("tag","message");
        RLog.i("tag","message");
        RLog.v("tag","message");
        try{RLog.init(context);}
        catch(Exception e){}
        RLog.disableLogging();
        assertFalse(RLog.isLoggingEnabled());
    }
}