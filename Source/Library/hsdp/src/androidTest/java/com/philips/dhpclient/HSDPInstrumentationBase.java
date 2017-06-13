//package com.philips.dhpclient;
//
//
//import android.support.multidex.MultiDex;
//
//import org.junit.Before;
//
//import static android.support.test.InstrumentationRegistry.getInstrumentation;
//
//public class HSDPInstrumentationBase {
//    @Before
//    public void setUp() throws Exception {
//        MultiDex.install(getInstrumentation().getTargetContext());
//
//        System.setProperty("dexmaker.dexcache", getInstrumentation()
//                .getTargetContext().getCacheDir().getPath());
//
//    }
//
//}
