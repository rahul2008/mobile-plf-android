/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.view;

import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.tagging.Pages;
import com.philips.cdp2.ews.viewmodel.TroubleshootHomeWiFiViewModel;
import com.philips.cdp2.powersleep.BuildConfig;
import com.philips.cdp2.powersleep.R;
import com.philips.cdp2.powersleep.databinding.TroubleshootHomeWifiFragmentBinding;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 25, manifest = Config.NONE)
public class TroubleshootHomeWiFiFragmentTest {

    private static final int HIERARCHY_LEVEL = 2;
    private TroubleshootHomeWiFiFragment fragment;

    @Mock
    private TroubleshootHomeWifiFragmentBinding viewModelBinderMock;
    @Mock
    private EWSComponent ewsComponentMock;
    @Mock
    private TroubleshootHomeWiFiViewModel viewModelMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        fragment = TroubleshootHomeWiFiFragment.getInstance(HIERARCHY_LEVEL);
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
    public void shouldBindDataWhenAsked() throws Exception {
        fragment.bindViewModel(viewModelBinderMock);
        verify(viewModelBinderMock).setViewModel(viewModelMock);
    }

    @Test
    public void shouldReturnCorrectHierarchyLevelWhenAsked() throws Exception {
        assertEquals(HIERARCHY_LEVEL, fragment.getHierarchyLevel());
    }

    @Test
    public void shouldReturnCorrectLayoutIdWhenAsked() throws Exception {
        assertEquals(R.layout.troubleshoot_home_wifi_fragment, fragment.getLayoutId());
    }

    @Test
    public void shouldReturnCorrectPageNameForTagging() throws Exception {
        assertEquals(Pages.HOME_WIFI_OFF, fragment.getPageName());
    }

    @Test
    public void shouldNotAttachAnyMenuWhenAsked() throws Exception {
        assertFalse(fragment.hasMenu());
    }
}