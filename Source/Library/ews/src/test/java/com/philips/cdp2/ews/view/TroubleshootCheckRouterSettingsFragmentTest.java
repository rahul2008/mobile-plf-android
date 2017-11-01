/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.view;

import com.philips.cdp2.ews.BuildConfig;
import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.tagging.Tag;
import com.philips.cdp2.ews.viewmodel.TroubleshootCheckRouterSettingsViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.philips.cdp2.ews.view.TroubleshootCheckRouterSettingsFragment.ROUTER_ERROR_DIFFERENT_NETWORK;
import static com.philips.cdp2.ews.view.TroubleshootCheckRouterSettingsFragment.ROUTER_ERROR_NO_NETWORK;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 25, manifest = Config.NONE)
public class TroubleshootCheckRouterSettingsFragmentTest {

    private TroubleshootCheckRouterSettingsFragment fragment;
    private static final int HIERARCHY_LEVEL = 8;

    @Mock
    private EWSComponent ewsComponentMock;
    @Mock
    private TroubleshootCheckRouterSettingsViewModel viewModelMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        fragment = TroubleshootCheckRouterSettingsFragment.getInstance(ROUTER_ERROR_DIFFERENT_NETWORK);
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
        assertEquals(R.layout.troubleshoot_check_router_settings, fragment.getLayoutId());
    }

    @Test
    public void shouldReturnCorrectPageNameForTaggingWhenScreenTypeIsWrongWifi() throws Exception {
        fragment.screenType = ROUTER_ERROR_DIFFERENT_NETWORK;
        assertEquals(Tag.PAGE.ROUTER_SETTINGS_WRONG_WIFI, fragment.getPageName());
    }

    @Test
    public void shouldReturnCorrectPageNameForTaggingWhenScreenTypeIsWrongPassword() throws Exception {
        fragment.screenType = ROUTER_ERROR_NO_NETWORK;
        assertEquals(Tag.PAGE.ROUTER_SETTINGS, fragment.getPageName());
    }

    @Test
    public void shouldReturnCorrectToolbarTitleWhenScreenTypeIsWrongWifi() throws Exception {
        fragment.screenType = ROUTER_ERROR_DIFFERENT_NETWORK;
        assertEquals(R.string.ews_23_header_1, fragment.getToolbarTitle());
    }

    @Test
    public void shouldReturnCorrectToolbarTitleWhenScreenTypeIsNoPassword() throws Exception {
        fragment.screenType = ROUTER_ERROR_NO_NETWORK;
        assertEquals(R.string.ews_24_header, fragment.getToolbarTitle());
    }

    @Test
    public void shouldHaveTheMenuDisabled() throws Exception {
        assertFalse(fragment.hasMenu());
    }

}