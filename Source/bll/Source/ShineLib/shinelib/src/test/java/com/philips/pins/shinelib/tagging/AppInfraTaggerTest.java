/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.tagging;

import android.support.annotation.NonNull;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Map;

import static com.philips.pins.shinelib.tagging.AppInfraTagger.DELIMITER;
import static com.philips.pins.shinelib.tagging.AppInfraTagger.TAGGING_VERSION_STRING;
import static com.philips.platform.appinfra.tagging.AppInfraTaggingUtil.SEND_DATA;
import static com.philips.platform.appinfra.tagging.AppInfraTaggingUtil.TECHNICAL_ERROR;
import static com.philips.platform.appinfra.tagging.AppTaggingConstants.COMPONENT_VERSION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class AppInfraTaggerTest {

    private static final String COMPONENT_ID_BLL = "bll";

    @Mock
    private AppInfraInterface appInfraInterfaceMock;

    @Mock
    private AppTaggingInterface appTaggingInterfaceMock;

    @Captor
    private ArgumentCaptor<Map<String, String>> dataObjectCaptor;

    private AppInfraTagger appInfraTagger;

    @Before
    public void setUp() {
        initMocks(this);

        appInfraTagger = new AppInfraTagger(appInfraInterfaceMock) {
            @Override
            AppTaggingInterface createTaggingInstance(@NonNull AppInfraInterface appInfraInterface) {
                return appTaggingInterfaceMock;
            }
        };
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
        appInfraTagger.sendTechnicalError(technicalError);

        verify(appTaggingInterfaceMock, times(1)).trackActionWithInfo(eq(SEND_DATA), dataObjectCaptor.capture());
        assertThat(dataObjectCaptor.getValue().get(COMPONENT_VERSION)).isEqualTo(TAGGING_VERSION_STRING);
    }

    @Test
    public void givenATagger_whenSendingData_thenTrackActionWithInfoIsInvokedOnAppTaggingInterface() {
        final String technicalError = "testError";
        appInfraTagger.sendTechnicalError(technicalError);

        verify(appTaggingInterfaceMock, times(1)).trackActionWithInfo(eq(SEND_DATA), ArgumentMatchers.<String, String>anyMap());
    }

    @Test
    public void givenATagger_whenSendingTechnicalError_thenTrackActionWithInfoIsInvokedWithANonEmptyValue() {
        final String technicalError = "testError";
        appInfraTagger.sendTechnicalError(technicalError);

        verify(appTaggingInterfaceMock, times(1)).trackActionWithInfo(anyString(), dataObjectCaptor.capture());
        assertThat(dataObjectCaptor.getValue().get(TECHNICAL_ERROR)).isNotEmpty();
    }

    @Test
    public void givenATagger_whenSendingTechnicalErrorWithExplanations_thenTheTaggedValueMustBeADelimitedString() {
        appInfraTagger.sendTechnicalError("error", "foo", "bar", "baz");

        verify(appTaggingInterfaceMock, times(1)).trackActionWithInfo(anyString(), dataObjectCaptor.capture());
        final String[] pieces = dataObjectCaptor.getValue().get(TECHNICAL_ERROR).split(DELIMITER);
        System.out.println(Arrays.toString(pieces));

        assertThat(pieces.length).isEqualTo(6);
        assertThat(pieces[0]).isEqualTo(COMPONENT_ID_BLL);
        assertThat(pieces[1]).isNotEmpty();
        assertThat(pieces[2]).isEqualTo("error");
        assertThat(pieces[3]).isEqualTo("foo");
        assertThat(pieces[4]).isEqualTo("bar");
        assertThat(pieces[5]).isEqualTo("baz");
    }
}
