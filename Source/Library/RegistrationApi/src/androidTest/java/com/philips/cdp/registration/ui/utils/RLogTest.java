package com.philips.cdp.registration.ui.utils;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.test.InstrumentationTestCase;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;

import org.junit.Before;

/**
 * Created by 310243576 on 9/6/2016.
 */
public class RLogTest extends InstrumentationTestCase{
    RLog rLog;

    Context context;
    @Before
    public void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
//        MockitoAnnotations.initMocks(this);
        super.setUp();
        rLog = new RLog();
        AppInfraInterface appInfraInterface = new AppInfra.Builder().build(getInstrumentation().getTargetContext());
        RLog.init();
        context = getInstrumentation().getTargetContext();
        synchronized(this){//synchronized block
        }
        RLog.init();
    }

}