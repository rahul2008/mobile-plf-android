/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.homewificonnection;

import com.philips.cdp2.ews.tagging.EWSTagger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EWSTagger.class)
public class ConnectingDeviceWithWifiFragmentTest {

    private ConnectingDeviceWithWifiFragment subject;

    @Before
    public void setUp() throws Exception {
        mockStatic(EWSTagger.class);
        subject = new ConnectingDeviceWithWifiFragment();
    }

    @Test
    public void itShouldReturnCorrectPageNameForTagging() throws Exception {
        assertEquals("connectingDeviceWithWifi", subject.getPageName());
    }

    @Test
    public void itShouldCalltrackPageOnResume() throws Exception {
        subject.onResume();
        verifyStatic();
        EWSTagger.trackPage("connectingDeviceWithWifi");
    }

}