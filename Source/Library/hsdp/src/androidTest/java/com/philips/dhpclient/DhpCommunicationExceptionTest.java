package com.philips.dhpclient;

import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/21/2016.
 */
public class DhpCommunicationExceptionTest extends InstrumentationTestCase {

    DhpCommunicationException mDhpCommunicationException;
    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
//        MockitoAnnotations.initMocks(this);
        super.setUp();
        Throwable e = new Throwable();
        mDhpCommunicationException =new DhpCommunicationException(e);

    }

    @Test
    public void testDhpcommunicationException()
    {
        assertNotNull(mDhpCommunicationException);
        mDhpCommunicationException =new DhpCommunicationException("sample","sample");
        assertNotNull(mDhpCommunicationException);
    }
}