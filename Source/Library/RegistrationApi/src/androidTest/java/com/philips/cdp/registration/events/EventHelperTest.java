package com.philips.cdp.registration.events;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.ui.utils.FontLoader;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/18/2016.
 */
public class EventHelperTest extends InstrumentationTestCase{
    @Mock
    EventHelper mEventHelper;

    @Mock
    Context context;

    @Before
    public void setUp() throws Exception {
        // Necessary to get Mockito framework working
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
//        MockitoAnnotations.initMocks(this);
        super.setUp();

        assertNotNull(mEventHelper.getInstance());

        mEventHelper = mEventHelper.getInstance();
        context = getInstrumentation().getTargetContext();

    }
    @Test
    public void testGetInstance() throws Exception {
        assertNotNull(mEventHelper);
        mEventHelper.getInstance();
    }
}