/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.troubleshooting.resetconnection;

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
public class ResetConnectionTroubleshootingFragmentTest {

    private ResetConnectionTroubleshootingFragment subject;

    @Before
    public void setUp() throws Exception {
        mockStatic(EWSTagger.class);
        subject = new ResetConnectionTroubleshootingFragment();
    }

    @Test
    public void itShouldReturnCorrectPageNameForTagging() throws Exception {
        assertEquals("resetConnection", subject.getPageName());
    }

    @Test
    public void itShouldCalltrackPageOnResume() throws Exception {
        subject.onResume();
        verifyStatic();
        EWSTagger.trackPage("resetConnection");
    }

}