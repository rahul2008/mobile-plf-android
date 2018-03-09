/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
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

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BleDeviceListAdapterTest {

    @Mock
    private Context context;

    @Mock
    private NetworkNode networkNode;

    @Mock
    private CommunicationStrategy bleDiscoveryStrategy;

    private BleDeviceListAdapter adapter;

    @Before
    public void setUp() {
        when(networkNode.getModelId()).thenReturn(RefAppBleReferenceAppliance.MODEL_NAME_HH1600);
        final ArrayList<RefAppBleReferenceAppliance> refAppBleReferenceAppliances = new ArrayList<>();
        adapter = new BleDeviceListAdapter(context, refAppBleReferenceAppliances);
    }

    @Test
    public void addDevice() throws Exception {
        adapter.addDevice(new RefAppBleReferenceAppliance(networkNode, bleDiscoveryStrategy));
        Assert.assertEquals(1, adapter.getCount());
    }
}
