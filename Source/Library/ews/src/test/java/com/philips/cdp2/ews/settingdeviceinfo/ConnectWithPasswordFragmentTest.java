/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.settingdeviceinfo;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.tagging.EWSTagger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EWSTagger.class)
public class ConnectWithPasswordFragmentTest {

    private ConnectWithPasswordFragment subject;

    @Mock
    private EWSComponent ewsComponentMock;

    @Mock
    private ConnectWithPasswordViewModel viewModelMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockStatic(EWSTagger.class);
        subject = new ConnectWithPasswordFragment();
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
        assertEquals(5, subject.getHierarchyLevel());
    }

    @Test
    public void shouldReturnCorrectHLayoutIdWhenAsked() throws Exception {
        assertEquals(R.layout.fragment_ews_connect_device, subject.getLayoutId());
    }

    @Test
    public void shouldHandleBackPressWhenAsked() throws Exception {
        assertTrue(subject.onBackPressed());
    }

    @Test
    public void shouldReturnCorrectPageNameForTagging() throws Exception {
        assertEquals("connectWithPassword", subject.getPageName());
    }

    @Test
    public void shouldCalltrackPageOnResume() throws Exception {
        subject.onResume();
        verifyStatic(times(1));
        EWSTagger.trackPage("connectWithPassword");
    }
}
