/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.utility;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.Map;

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
    private ArgumentCaptor<Map<String, String>> infoMapCaptor;

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

        final String action = "testAction";
        tagger.trackAction(action, anyString(), anyString());

        verify(appTaggingInterfaceMock).trackActionWithInfo(eq(action), ArgumentMatchers.<String, String>anyMap());
    }

    @Test
    public void givenATagger_whenTrackActionInvoked_thenTrackActionWithInfoIsInvokedWithSuppliedKeyAndValue() {

        final String key = "testKey";
        final String value = "testValue";
        tagger.trackAction("testAction", key, value);

        verify(appTaggingInterfaceMock).trackActionWithInfo(anyString(), infoMapCaptor.capture());

        final Map<String, String> infoMap = infoMapCaptor.getValue();

        assertThat(infoMap.containsKey(eq(key)));
        assertThat(infoMap.get(key)).isEqualTo(value);
    }

    @Test
    public void givenATagger_whenTrackActionInvoked_thenTrackActionWithInfoIsInvokedWithMandatoryEntries() {

        tagger.trackAction("testAction", anyString(), anyString());

        verify(appTaggingInterfaceMock).trackActionWithInfo(anyString(), infoMapCaptor.capture());

        final Map<String, String> infoMap = infoMapCaptor.getValue();
        assertThat(infoMap.containsKey(SHNTagger.Key.KEY_LIBRARY_VERSION));
        assertThat(infoMap.containsKey(SHNTagger.Key.KEY_MANUFACTURER));
        assertThat(infoMap.containsKey(SHNTagger.Key.KEY_MODEL));
        assertThat(infoMap.containsKey(SHNTagger.Key.KEY_OS));
    }
}
