/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.view;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.tagging.Tag;
import com.philips.cdp2.ews.viewmodel.BlinkingAccessPointViewModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static junit.framework.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.MockitoAnnotations.initMocks;

public class BlinkingAccessPointFragmentTest {

    private BlinkingAccessPointFragment fragment;

    @Mock
    private EWSComponent ewsComponentMock;
    @Mock
    private BlinkingAccessPointViewModel viewModelMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        fragment = new BlinkingAccessPointFragment();
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
        assertEquals(EWSResetDeviceFragment.FRAGMENT_HIERARCHY_LEVEL, fragment.getHierarchyLevel());
    }

    @Test
    public void shouldReturnCorrectHLayoutIdWhenAsked() throws Exception {
        assertEquals(R.layout.fragment_blinking_access_point, fragment.getLayoutId());
    }

    @Test
    public void shouldReturnCorrectPageNameForTagging() throws Exception {
        assertEquals(Tag.PAGE.BLINKING_ACCESS_POINT, fragment.getPageName());
    }

    @Test
    public void shouldReturnCorrectViewModelWhenAsked() throws Exception {
        assertSame(viewModelMock, fragment.getViewModel());
    }
}