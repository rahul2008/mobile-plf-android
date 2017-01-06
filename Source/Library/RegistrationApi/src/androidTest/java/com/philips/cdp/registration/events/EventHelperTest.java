package com.philips.cdp.registration.events;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

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
        MultiDex.install(getInstrumentation().getTargetContext());
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
        List<String> list = new ArrayList<String>();
        EventListener observer = new EventListener() {
            @Override
            public void onEventReceived(String event) {

            }
        };
        mEventHelper.registerEventNotification(list,observer);
        mEventHelper.registerEventNotification("list",observer);
        mEventHelper.unregisterEventNotification("list",observer);
        mEventHelper.notifyEventOccurred("pEventName");

    }
}