package com.philips.cdp.di.iap.eventhelper;

import com.philips.cdp.di.iap.utils.IAPConstant;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 310164421 on 3/9/2016.
 */
@RunWith(RobolectricTestRunner.class)
public class EventHelperTest extends TestCase {
    EventHelper eventHelper = EventHelper.getInstance();
    EventListener listener = Mockito.mock(EventListener.class);

    @Test
    public void testGetInstance() throws Exception {
        assertNotNull(eventHelper);
    }

    @Test
    public void testRegisterEventNotification() throws Exception {
        List<String> list = new ArrayList<>();
        list.add(IAPConstant.DELIVER_TO_THIS_ADDRESS);
        list.add(IAPConstant.EMPTY_CART_FRAGMENT_REPLACED);
        list.add(IAPConstant.BILLING_ADDRESS_FIELDS);
        eventHelper.registerEventNotification(list, listener);
        assertNotNull(list);
        assertNotNull(listener);
    }

    @Test
    public void testRegisterEventNotification1() throws Exception {
        eventHelper.registerEventNotification(IAPConstant.DELIVER_TO_THIS_ADDRESS, listener);
        assertNotNull(listener);
    }

    @Test
    public void testUnregisterEventNotification() throws Exception {
        eventHelper.unregisterEventNotification(IAPConstant.DELIVER_TO_THIS_ADDRESS, listener);
        assertNotNull(listener);
    }

    @Test
    public void testNotifyEventOccurred() throws Exception {
        eventHelper.notifyEventOccurred(IAPConstant.DELIVER_TO_THIS_ADDRESS);
        assertNotNull(eventHelper);
    }
}