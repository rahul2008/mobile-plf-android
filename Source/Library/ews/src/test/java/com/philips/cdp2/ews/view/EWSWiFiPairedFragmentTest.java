/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.view;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.viewmodel.EWSWiFIPairedViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class EWSWiFiPairedFragmentTest {

    private EWSWiFiPairedFragment subject;

    @Mock
    private EWSComponent ewsComponentMock;

    @Mock
    private EWSWiFIPairedViewModel viewModelMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        subject = new EWSWiFiPairedFragment();
        injectMembers();
    }

    private void injectMembers() {
        doAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                subject.viewModel = viewModelMock;
                return null;
            }
        }).when(ewsComponentMock).inject(subject);
        subject.inject(ewsComponentMock);
    }

    @Test
    public void shouldReturnCorrectHierarchyLevelWhenAsked() throws Exception {
        assertEquals(6, subject.getHierarchyLevel());
    }

    @Test
    public void shouldReturnCorrectHLayoutIdWhenAsked() throws Exception {
        assertEquals(R.layout.fragment_ews_wifi_paired, subject.getLayoutId());
    }

    @Test
    public void shouldNotHandleBackPressWhenAsked() throws Exception {
        assertTrue(subject.onBackPressed());
    }

    @Test
    public void shouldReturnCorrectPageNameForTagging() throws Exception {
        assertEquals("connectionSuccessful", subject.getPageName());
    }
}
