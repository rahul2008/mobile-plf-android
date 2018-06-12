/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.utility;

import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.Map;

import static com.philips.pins.shinelib.utility.SHNTagger.DELIMITER;
import static com.philips.pins.shinelib.utility.SHNTagger.TAGGING_VERSION_STRING;
import static com.philips.platform.appinfra.tagging.AppInfraTaggingUtil.SEND_DATA;
import static com.philips.platform.appinfra.tagging.AppInfraTaggingUtil.TECHNICAL_ERROR;
import static com.philips.platform.appinfra.tagging.AppTaggingConstants.COMPONENT_VERSION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class SHNTaggerTest {

    private static final String COMPONENT_ID_BLL = "bll";

    @Mock
    private AppTaggingInterface appTaggingInterfaceMock;

    @Captor
    private ArgumentCaptor<Map<String, String>> dataObjectCaptor;

    @Before
    public void setUp() {
        initMocks(this);

        SHNTagger.taggingInstance = appTaggingInterfaceMock;
    }

    @Test
    public void givenATagger_thenTheComponentIdAndVersionShouldBeValid() {
        final String[] pieces = TAGGING_VERSION_STRING.split(DELIMITER);
        final String componentId = pieces[0];
        final String componentVersion = pieces[1];

        assertThat(componentId).isEqualTo(COMPONENT_ID_BLL);
        assertThat(componentVersion).isNotEmpty();
    }

    @Test
    public void givenATagger_whenSendingTechnicalError_thenDataObjectContainsComponentVersion() {
        final String technicalError = "testError";
        SHNTagger.sendTechnicalError(technicalError);

        verify(appTaggingInterfaceMock).trackActionWithInfo(eq(SEND_DATA), dataObjectCaptor.capture());
        assertThat(dataObjectCaptor.getValue().get(COMPONENT_VERSION)).isEqualTo(TAGGING_VERSION_STRING);
    }

    @Test
    public void givenATagger_whenSendingData_thenTrackActionWithInfoIsInvokedOnAppTaggingInterface() {
        final String technicalError = "testError";
        SHNTagger.sendTechnicalError(technicalError);

        verify(appTaggingInterfaceMock).trackActionWithInfo(eq(SEND_DATA), ArgumentMatchers.<String, String>anyMap());
    }

    @Test
    public void givenATagger_whenSendingTechnicalError_thenTrackActionWithInfoIsInvokedWithPredefinedErrorKeyAndSuppliedValue() {
        final String technicalError = "testError";
        SHNTagger.sendTechnicalError(technicalError);

        verify(appTaggingInterfaceMock).trackActionWithInfo(anyString(), dataObjectCaptor.capture());
        assertThat(dataObjectCaptor.getValue().get(TECHNICAL_ERROR)).isEqualTo(COMPONENT_ID_BLL + DELIMITER + technicalError);
    }

    @Test
    public void givenATagger_whenTaggerIsNotInitialized_thenTrackActionIsNotInvokedOnTaggingInstance() {
        SHNTagger.taggingInstance = null;

        SHNTagger.sendTechnicalError("dontcare");

        verify(appTaggingInterfaceMock, never()).trackActionWithInfo(anyString(), ArgumentMatchers.<String, String>anyMap());
    }
}
