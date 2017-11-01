/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.view;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.microapp.EWSCallbackNotifier;
import com.philips.cdp2.ews.tagging.Pages;
import com.philips.cdp2.ews.viewmodel.EWSGettingStartedViewModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class EWSGettingStartedFragmentTest {

    private EWSGettingStartedFragment fragment;

    @Mock
    private EWSComponent ewsComponentMock;
    @Mock
    private EWSGettingStartedViewModel viewModelMock;

    @Mock
    EWSCallbackNotifier ewsCallbackNotifierMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        fragment = new EWSGettingStartedFragment();
        injectMembers();
    }

    private void injectMembers() {
        doAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                fragment.viewModel = viewModelMock;
                fragment.ewsCallbackNotifier = ewsCallbackNotifierMock;
                return null;
            }
        }).when(ewsComponentMock).inject(fragment);
        fragment.inject(ewsComponentMock);
    }

    @Test
    public void shouldReturnCorrectHierarchyLevelWhenAsked() throws Exception {
        assertEquals(1, fragment.getHierarchyLevel());
    }

    @Test
    public void shouldReturnCorrectHLayoutIdWhenAsked() throws Exception {
        assertEquals(R.layout.fragment_ews_getting_started, fragment.getLayoutId());
    }

    @Test
    public void shouldHandleBackPressWhenAsked() throws Exception {
        assertTrue(fragment.onBackPressed());
        verify(viewModelMock).onBackPressed(ewsCallbackNotifierMock);
    }

    @Test
    public void shouldReturnCorrectPageNameForTagging() throws Exception {
        assertEquals(Pages.WIFI_SETUP, fragment.getPageName());
    }

    @Test
    public void shouldAttachAnyMenuForGetStartedScreen() throws Exception {
        assertTrue(fragment.hasMenu());
    }
}