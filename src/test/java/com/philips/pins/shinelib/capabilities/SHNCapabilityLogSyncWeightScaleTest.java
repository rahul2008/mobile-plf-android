package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.services.weightscale.SHNServiceWeightScale;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * Created by 310188215 on 08/07/15.
 */
public class SHNCapabilityLogSyncWeightScaleTest {
    private SHNCapabilityLogSyncWeightScale shnCapabilityLogSyncWeightScale;
    private SHNServiceWeightScale mockedSHNServiceWeightScale;

    @Before
    public void setUp() throws Exception {
        mockedSHNServiceWeightScale = mock(SHNServiceWeightScale.class);

        shnCapabilityLogSyncWeightScale = new SHNCapabilityLogSyncWeightScale(mockedSHNServiceWeightScale);
    }

    @Test
    public void aInstanceCanBeCreated() {
        assertNotNull(shnCapabilityLogSyncWeightScale);
    }
}