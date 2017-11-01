/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.view;

import com.philips.cdp2.ews.BuildConfig;
import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.tagging.Page;
import com.philips.cdp2.ews.viewmodel.TroubleshootIncorrectPasswordViewModel;

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
public class TroubleshootIncorrectPasswordFragmentTest {

    private TroubleshootIncorrectPasswordFragment subject;
    private static final int HIERARCHY_LEVEL = 7;

    @Mock
    private EWSComponent ewsComponentMock;
    @Mock
    private TroubleshootIncorrectPasswordViewModel viewModelMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        subject = new TroubleshootIncorrectPasswordFragment();
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
        assertEquals(R.layout.troubleshoot_incorrect_password, subject.getLayoutId());
    }

    @Test
    public void shouldReturnCorrectPageNameForTagging() throws Exception {
        assertEquals(Page.INCORRECT_PASSWORD, subject.getPageName());
    }

    @Test
    public void shouldReturnCorrectPasswordTitle() throws Exception {
        assertEquals(R.string.ews_22_header, subject.getToolbarTitle());
    }
}