package com.philips.cdp.registration.events;

import android.content.Context;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class NetworkStateHelperTest extends TestCase {
    @Mock
    NetworkStateHelper mNetworkStateHelper;

    @Mock
    Context context;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        assertNotNull(mNetworkStateHelper.getInstance());
        mNetworkStateHelper = mNetworkStateHelper.getInstance();
    }

    @Test
    public void testGetInstance() throws Exception {
        assertNotNull(mNetworkStateHelper);
        mNetworkStateHelper.getInstance();
        NetworkStateListener observer = new NetworkStateListener() {
            @Override
            public void onNetWorkStateReceived(boolean isOnline) {

            }
        };

        mNetworkStateHelper.registerEventNotification(observer);

        mNetworkStateHelper.unregisterEventNotification(observer);

        mNetworkStateHelper.notifyEventOccurred(true);
        mNetworkStateHelper.notifyEventOccurred(false);
    }
}