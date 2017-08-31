package com.philips.cdp.registration.events;

import android.content.Context;

import com.philips.cdp.registration.RegistrationApiInstrumentationBase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertNotNull;

public class NetworkStateHelperTest extends RegistrationApiInstrumentationBase {
    @Mock
    NetworkStateHelper mNetworkStateHelper;

    @Mock
    Context context;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        assertNotNull(mNetworkStateHelper.getInstance());
        mNetworkStateHelper = mNetworkStateHelper.getInstance();
        context = getInstrumentation().getTargetContext();
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