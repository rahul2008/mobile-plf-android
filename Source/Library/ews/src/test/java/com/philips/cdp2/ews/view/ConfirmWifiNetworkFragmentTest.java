/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.view;

import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.viewmodel.ConfirmWifiNetworkViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EWSTagger.class)
public class ConfirmWifiNetworkFragmentTest {

    private ConfirmWifiNetworkFragment subject;

    @Mock
    private ConfirmWifiNetworkViewModel mockConfirmWifiNetworkViewModel;

    @Mock private EWSComponent ewsComponentMock;

    private ConfirmWifiNetworkFragment spySubject;


    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockStatic(EWSTagger.class);
        subject = new ConfirmWifiNetworkFragment();
        injectMembers();
    }

    private void injectMembers() {

        spySubject = spy(subject);
        //doReturn(mockConfirmWifiNetworkViewModel).when(spySubject.viewModel);
        doAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                spySubject.viewModel = mockConfirmWifiNetworkViewModel;
                return null;
            }
        }).when(spySubject).createViewModel();
        spySubject.createViewModel();
    }

    @Test
    public void shouldReturnCorrectPageNameForTagging() throws Exception {
        assertEquals("confirmWifiNetwork", subject.getPageName());
    }

    @Test
    public void shouldCalltrackPageOnResume() throws Exception {
        spySubject.onResume();
        verifyStatic(times(1));
        EWSTagger.trackPage("confirmWifiNetwork");
    }
}