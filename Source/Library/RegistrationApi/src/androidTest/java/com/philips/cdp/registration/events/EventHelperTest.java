package com.philips.cdp.registration.events;

import android.content.Context;

import com.philips.cdp.registration.RegistrationApiInstrumentationBase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertNotNull;


public class EventHelperTest extends RegistrationApiInstrumentationBase {
    @Mock
    EventHelper mEventHelper;

    @Mock
    Context context;

    @Before
    public void setUp() throws Exception {

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