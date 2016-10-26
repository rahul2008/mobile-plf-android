package com.philips.cdp.di.iap.core;

import com.philips.cdp.di.iap.networkEssential.NetworkEssentialsFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

/**
 * Created by indrajitkumar on 27/09/16.
 */
@RunWith(RobolectricTestRunner.class)
public class NetworkEssentialsFactoryTest {
    NetworkEssentialsFactory mNetworkEssentialsFactory;
    @Before
    public void setUp() throws Exception {
        mNetworkEssentialsFactory = new NetworkEssentialsFactory();
    }

    @Test
    public void getNetworkEssentialsForLocalData() throws Exception{
        NetworkEssentialsFactory.getNetworkEssentials(true);
    }
    @Test
    public void getNetworkEssentialsForHybrisData() throws Exception{
        NetworkEssentialsFactory.getNetworkEssentials(false);
    }
}