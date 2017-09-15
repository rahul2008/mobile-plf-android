/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.view;

import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.tagging.Pages;
import com.philips.cdp2.ews.viewmodel.EWSWiFiConnectViewModel;
import com.philips.cdp2.powersleep.R;
import com.philips.cdp2.powersleep.databinding.FragmentEwsConnectDeviceBinding;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.MockitoAnnotations.initMocks;

public class EWSWiFiConnectFragmentTest {

    private EWSWiFiConnectFragment fragment;

    @Mock
    private FragmentEwsConnectDeviceBinding viewModelBinderMock;

    @Mock
    private EWSComponent ewsComponentMock;

    @Mock
    private EWSWiFiConnectViewModel viewModelMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        fragment = new EWSWiFiConnectFragment();
        injectMembers();
    }

    private void injectMembers() {
        doAnswer(new Answer() {
            @Override
            public Object answer(final InvocationOnMock invocation) throws Throwable {
                fragment.viewModel = viewModelMock;
                return null;
            }
        }).when(ewsComponentMock).inject(fragment);
        fragment.inject(ewsComponentMock);
    }

    @Test
    public void shouldReturnCorrectHierarchyLevelWhenAsked() throws Exception {
        assertEquals(5, fragment.getHierarchyLevel());
    }

    @Test
    public void shouldReturnCorrectHLayoutIdWhenAsked() throws Exception {
        assertEquals(R.layout.fragment_ews_connect_device, fragment.getLayoutId());
    }

    @Test
    public void shouldHandleBackPressWhenAsked() throws Exception {
        assertTrue(fragment.onBackPressed());
    }

    @Test
    public void shouldReturnCorrectPageNameForTagging() throws Exception {
        assertEquals(Pages.CONNECT_WIFI, fragment.getPageName());
    }

    @Test
    public void shouldNotShowBackArrowForGetStartedScreen() throws Exception {
        assertEquals(0, fragment.getNavigationIconId());
    }
}
