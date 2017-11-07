/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.view;

import android.view.LayoutInflater;

import com.philips.cdp2.ews.base.BaseFragment;
import com.philips.cdp2.ews.tagging.EWSTagger;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.anyInt;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EWSTagger.class, LayoutInflater.class})
public class BaseFragmentTest {

    private BaseFragment subject;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        mockStatic(EWSTagger.class);
        subject = Mockito.mock(BaseFragment.class, Mockito.CALLS_REAL_METHODS);
        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(subject).showCancelDialog(anyInt());
    }

    @Test
    public void itShouldSendPageTagOnCancelDialogShown() throws Exception{
        subject.handleCancelButtonClicked(0);
        verifyStatic();
        EWSTagger.trackPage("cancelWifiSetup");
    }
}