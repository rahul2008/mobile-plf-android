package com.philips.dhpclient.util;

import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/19/2016.
 */
public class DateUtilTest extends InstrumentationTestCase{

    @Mock
    DateUtil dateUtil;

    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
//        MockitoAnnotations.initMocks(this);
        super.setUp();
        dateUtil = new DateUtil();
    }

    @Test
    public void testGetTimestamp() throws Exception {
       assertNotSame("dd",dateUtil.getTimestamp());

    }
}