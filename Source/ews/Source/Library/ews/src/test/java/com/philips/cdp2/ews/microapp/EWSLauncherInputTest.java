/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.microapp;

import android.support.v4.app.FragmentManager;

import com.philips.cdp2.ews.base.BaseFragment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class EWSLauncherInputTest {

    EWSLauncherInput subject;

    @Mock
    private FragmentManager mockFragmentManager;
    @Mock
    private BaseFragment mockBaseFragment;
    private int containerId = 5;


    @Before
    public void setUp() throws Exception {
        initMocks(this);
        subject = new EWSLauncherInput();
        subject.setFragmentManager(mockFragmentManager);
        subject.setContainerFrameId(containerId);
        when(mockFragmentManager.findFragmentById(containerId)).thenReturn(mockBaseFragment);
    }

    @Test
    public void itShouldCalledBackButtonClickOfBaseFragment() throws Exception {
        subject.handleCloseButtonClick();
        verify(mockFragmentManager).findFragmentById(containerId);
        verify(mockBaseFragment).handleCancelButtonClicked();
    }

    @Test
    public void getContainerFrameId() throws Exception {
        assertEquals(EWSLauncherInput.getContainerFrameId(), 5);
    }


    @Test
    public void getFragmentManager() throws Exception {
        subject.setFragmentManager(mockFragmentManager);
        assertEquals(EWSLauncherInput.getFragmentManager(),mockFragmentManager);
    }
}