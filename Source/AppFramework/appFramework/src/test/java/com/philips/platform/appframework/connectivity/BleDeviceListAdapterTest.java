/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.connectivity;

import android.content.Context;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.platform.appframework.connectivity.appliance.RefAppBleReferenceAppliance;

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

    ArrayList<RefAppBleReferenceAppliance> refAppBleReferenceAppliances;

    @Mock
    NetworkNode networkNode;

    @Mock
    CommunicationStrategy bleDiscoveryStrategy;

    private BleDeviceListAdapter adapter;

    @Before
    public void setUp(){
        refAppBleReferenceAppliances =new ArrayList<>();
        adapter=new BleDeviceListAdapter(context, refAppBleReferenceAppliances);
    }
    @Test
    public void addDevice() throws Exception {
        adapter.addDevice(new RefAppBleReferenceAppliance(networkNode,bleDiscoveryStrategy));
        Assert.assertEquals(1,adapter.getCount());
    }

}