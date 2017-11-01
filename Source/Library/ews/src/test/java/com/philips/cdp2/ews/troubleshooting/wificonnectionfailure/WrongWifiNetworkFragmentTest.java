/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.troubleshooting.wificonnectionfailure;

import com.philips.cdp2.ews.tagging.EWSTagger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EWSTagger.class)
public class WrongWifiNetworkFragmentTest {

    private WrongWifiNetworkFragment subject;

    @Before
    public void setUp() throws Exception {
        mockStatic(EWSTagger.class);
        subject = new WrongWifiNetworkFragment();
    }

    @Test
    public void shouldReturnCorrectPageNameForTagging() throws Exception {
        assertEquals("wrongWifiNetwork", subject.getPageName());
    }

    @Test
    public void shouldCalltrackPageOnResume() throws Exception {
        subject.onResume();
        verifyStatic(times(1));
        EWSTagger.trackPage("wrongWifiNetwork");
    }

}