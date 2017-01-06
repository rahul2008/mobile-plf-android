package com.philips.cdp.registration.events;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

/**
 * Created by 310243576 on 8/18/2016.
 */
public class NetworkStateHelperTest extends InstrumentationTestCase{
    @Mock
    NetworkStateHelper mNetworkStateHelper;

    @Mock
    Context context;

    @Before
    public void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        // Necessary to get Mockito framework working
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
//        MockitoAnnotations.initMocks(this);
        super.setUp();

        assertNotNull(mNetworkStateHelper.getInstance());

        mNetworkStateHelper = mNetworkStateHelper.getInstance();
        context = getInstrumentation().getTargetContext();

    }
    @Test
    public void testGetInstance() throws Exception {
        assertNotNull(mNetworkStateHelper);
        mNetworkStateHelper.getInstance();
        NetworStateListener observer = new NetworStateListener() {
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