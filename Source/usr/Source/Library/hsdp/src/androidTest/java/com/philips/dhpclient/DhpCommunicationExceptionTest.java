
package com.philips.dhpclient;

import android.support.multidex.MultiDex;
import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by 310243576 on 8/21/2016.
 */
public class DhpCommunicationExceptionTest extends InstrumentationTestCase {

    DhpCommunicationException mDhpCommunicationException;
    @Before
    public void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
//        MockitoAnnotations.initMocks(this);
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());

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
