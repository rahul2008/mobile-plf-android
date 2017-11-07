/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.view;

import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.confirmwifi.ConfirmWifiNetworkFragment;
import com.philips.cdp2.ews.confirmwifi.ConfirmWifiNetworkViewModel;
import com.philips.cdp2.ews.tagging.EWSTagger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EWSTagger.class)
public class ConfirmWifiNetworkFragmentTest {

    private ConfirmWifiNetworkFragment subject;

    @Mock
    private ConfirmWifiNetworkViewModel mockConfirmWifiNetworkViewModel;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockStatic(EWSTagger.class);
        subject = spy(new ConfirmWifiNetworkFragment());
        injectMembers();
    }

    private void injectMembers() {

        doAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                subject.viewModel = mockConfirmWifiNetworkViewModel;
                return null;
            }
        }).when(subject).createViewModel();
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                subject.sendPageTag();
                return null;
            }
        }).when(subject).showTroubleshootHomeWifiDialog(any(BaseContentConfiguration.class));
        subject.createViewModel();
    }

    @Test
    public void itShouldReturnCorrectPageNameForTagging() throws Exception {
        assertEquals("confirmWifiNetwork", subject.getPageName());
    }

    @Test
    public void itShouldCalltrackPageOnResume() throws Exception {
        subject.onResume();
        verifyStatic();
        EWSTagger.trackPage("confirmWifiNetwork");
    }
    @Test
    public void itShouldCalltrackPageOnShowTroubleshootHomeWifiDialog() throws Exception {
        subject.showTroubleshootHomeWifiDialog(any(BaseContentConfiguration.class));
        verifyStatic();
        EWSTagger.trackPage("selectHomeWifi");
    }
}