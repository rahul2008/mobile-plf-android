package com.philips.cdp.registration.events;

import android.content.Context;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;


public class EventHelperTest extends TestCase {
    @Mock
    EventHelper mEventHelper;

    @Mock
    Context context;

    @Before
    public void setUp() throws Exception {

            super.setUp();

        assertNotNull(mEventHelper.getInstance());

        mEventHelper = mEventHelper.getInstance();
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