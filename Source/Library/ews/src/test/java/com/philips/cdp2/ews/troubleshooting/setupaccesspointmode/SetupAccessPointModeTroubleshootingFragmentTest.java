package com.philips.cdp2.ews.troubleshooting.setupaccesspointmode;

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

/**
 * Created by architsureja on 01/11/17.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(EWSTagger.class)
public class SetupAccessPointModeTroubleshootingFragmentTest {

    private SetupAccessPointModeTroubleshootingFragment subject;

    @Before
    public void setUp() throws Exception {
        mockStatic(EWSTagger.class);
        subject = new SetupAccessPointModeTroubleshootingFragment();
    }

    @Test
    public void shouldReturnCorrectPageNameForTagging() throws Exception {
        assertEquals("setupAccessPointMode", subject.getPageName());
    }

    @Test
    public void shouldCalltrackPageOnResume() throws Exception {
        subject.onResume();
        verifyStatic(times(1));
        EWSTagger.trackPage("setupAccessPointMode");
    }

}