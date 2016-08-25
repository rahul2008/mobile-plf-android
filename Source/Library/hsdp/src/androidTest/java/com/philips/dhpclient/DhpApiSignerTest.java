package com.philips.dhpclient;

import android.test.InstrumentationTestCase;

import org.junit.Before;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/25/2016.
 */
public class DhpApiSignerTest  extends InstrumentationTestCase {
    DhpApiSigner mDhpApiSigner;

    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
//        MockitoAnnotations.initMocks(this);
        super.setUp();
        mDhpApiSigner = new DhpApiSigner("sample","sample");
    }

    public void testDhpApiClient(){
        assertNotNull(mDhpApiSigner);
        Map<String, String> headers = new HashMap<String,String>();
        assertNotNull(mDhpApiSigner.buildAuthorizationHeaderValue("requestMethod","queryString",headers,"dhpUrl","requestbody"));
    }
}