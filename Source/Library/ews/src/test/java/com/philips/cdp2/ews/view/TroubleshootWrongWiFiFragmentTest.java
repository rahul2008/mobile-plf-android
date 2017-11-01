/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.view;

import com.philips.cdp2.ews.BuildConfig;
import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.tagging.Tag;
import com.philips.cdp2.ews.viewmodel.TroubleshootWrongWiFiViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 25, manifest = Config.NONE)
public class TroubleshootWrongWiFiFragmentTest {

    private static final int HIERARCHY_LEVEL = 7;
    private TroubleshootWrongWiFiFragment fragment;

    @Mock
    private EWSComponent ewsComponentMock;
    @Mock
    private TroubleshootWrongWiFiViewModel viewModelMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        fragment = new TroubleshootWrongWiFiFragment();
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
        assertEquals(HIERARCHY_LEVEL, fragment.getHierarchyLevel());
    }

    @Test
    public void shouldReturnCorrectLayoutIdWhenAsked() throws Exception {
        assertEquals(R.layout.troubleshoot_wrong_wifi_fragment, fragment.getLayoutId());
    }

    @Test
    public void shouldReturnCorrectPageNameForTagging() throws Exception {
        assertEquals(Tag.PAGE.WRONG_WIFI, fragment.getPageName());
    }

    @Test
    public void shouldReturnCorrectToolbarTitle() throws Exception {
        assertEquals(fragment.getToolbarTitle(), R.string.ews_21_header);
    }
}