/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.connectivity;

import android.content.Context;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.platform.appframework.ConnectivityDeviceType;
import com.philips.platform.appframework.connectivity.appliance.BleReferenceAppliance;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

/**
 * Test for BleDeviceListAdapter
 */
@RunWith(MockitoJUnitRunner.class)
public class BleDeviceListAdapterTest {

    @Mock
    Context context;

    ArrayList<BleReferenceAppliance> bleReferenceAppliances;

    @Mock
    NetworkNode networkNode;

    @Mock
    CommunicationStrategy bleDiscoveryStrategy;

    private BleDeviceListAdapter adapter;

    @Before
    public void setUp(){
        bleReferenceAppliances=new ArrayList<>();
        adapter=new BleDeviceListAdapter(context,bleReferenceAppliances);
    }
    @Test
    public void addDevice() throws Exception {
        adapter.addDevice(new BleReferenceAppliance(networkNode,bleDiscoveryStrategy, ConnectivityDeviceType.REFERENCE_NODE));
        Assert.assertEquals(1,adapter.getCount());
    }

}