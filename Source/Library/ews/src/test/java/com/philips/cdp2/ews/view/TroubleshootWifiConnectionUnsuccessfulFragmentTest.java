/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.view;

import com.philips.cdp2.ews.BuildConfig;
import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.tagging.Page;
import com.philips.cdp2.ews.viewmodel.TroubleshootConnectionUnsuccessfulViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 25, manifest = Config.NONE)
public class TroubleshootWifiConnectionUnsuccessfulFragmentTest {

    private TroubleshootConnectionUnsuccessfulFragment subject;
    private static final int HIERARCHY_LEVEL = 6;

    @Mock
    private EWSComponent ewsComponentMock;
    @Mock
    private TroubleshootConnectionUnsuccessfulViewModel viewModelMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        subject = new TroubleshootConnectionUnsuccessfulFragment();
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
        assertEquals(HIERARCHY_LEVEL, subject.getHierarchyLevel());
    }

    @Test
    public void shouldReturnCorrectLayoutIdWhenAsked() throws Exception {
        assertEquals(R.layout.troubleshoot_connection_unsuccessful, subject.getLayoutId());
    }

    @Test
    public void shouldReturnCorrectPageNameForTagging() throws Exception {
        assertEquals(Page.CONNECTION_UNSUCCESSFUL, subject.getPageName());
    }

    @Test
    public void shouldHaveMenuWhenAsked() throws Exception {
        assertTrue(subject.hasMenu());
    }

    @Test
    public void shouldNotHaveNavigationBack() throws Exception {
        assertEquals(0, subject.getNavigationIconId());
    }

    @Test
    public void shouldNotHaveBackEnabled() throws Exception {
        assertTrue(subject.onBackPressed());
    }

    @Test
    public void shouldHaveTheCorrectToolbarTitle() throws Exception {
        assertEquals(subject.getToolbarTitle(), R.string.ews_20_header);
    }
}