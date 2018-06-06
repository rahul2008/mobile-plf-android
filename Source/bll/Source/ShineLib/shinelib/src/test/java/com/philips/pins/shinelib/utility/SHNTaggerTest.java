/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.utility;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.tagging.AppInfraTaggingUtil;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static com.philips.platform.appinfra.tagging.AppInfraTaggingUtil.TECHNICAL_ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class SHNTaggerTest {

    @Mock
    private AppInfraInterface appInfraInterfaceMock;

    @Mock
    private AppTaggingInterface appTaggingInterfaceMock;

    @Captor
    private ArgumentCaptor<String> keyCaptor;

    @Captor
    private ArgumentCaptor<String> valueCaptor;

    private SHNTagger tagger;

    @Before
    public void setUp() {
        initMocks(this);

        tagger = new SHNTagger(appInfraInterfaceMock) {
            @Override
            AppTaggingInterface createTagging(AppInfraInterface appInfraInterface) {
                return appTaggingInterfaceMock;
            }
        };
    }

    @Test
    public void givenATagger_whenTrackActionInvoked_thenTrackActionWithInfoIsInvokedOnAppTaggingInterface() {
        final String key = "testKey";
        final String value = "testValue";
        tagger.sendData(key, value);

        verify(appTaggingInterfaceMock).trackActionWithInfo(eq(AppInfraTaggingUtil.SEND_DATA), eq(key), eq(value));
    }

    @Test
    public void givenATagger_whenSendingData_thenTrackActionWithInfoIsInvokedWithSuppliedKeyAndValue() {
        final String key = "testKey";
        final String value = "testValue";
        tagger.sendData(key, value);

        verify(appTaggingInterfaceMock).trackActionWithInfo(anyString(), keyCaptor.capture(), valueCaptor.capture());

        assertThat(keyCaptor.getValue().equals(key));
        assertThat(valueCaptor.getValue().equals(value));
    }

    @Test
    public void givenATagger_whenSendingTechnicalError_thenTrackActionWithInfoIsInvokedWithPredefinedErrorKeyAndSuppliedValue() {
        final String errorMsg = "Something went wrong!";
        tagger.sendTechnicalError(errorMsg);

        verify(appTaggingInterfaceMock).trackActionWithInfo(anyString(), keyCaptor.capture(), valueCaptor.capture());

        assertThat(keyCaptor.getValue().equals(TECHNICAL_ERROR));
        assertThat(valueCaptor.getValue().equals(errorMsg));
    }
}
