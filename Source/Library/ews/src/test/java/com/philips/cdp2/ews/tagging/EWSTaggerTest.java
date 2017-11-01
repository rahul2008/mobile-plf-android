/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.tagging;

import android.app.Activity;

import com.philips.cdp2.ews.microapp.EWSDependencyProvider;
import com.philips.cdp2.ews.tagging.Tag.ACTION;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EWSDependencyProvider.class)
public class EWSTaggerTest {

    @Mock
    private AppTaggingInterface appTaggingInterfaceMock;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        PowerMockito.mockStatic(EWSDependencyProvider.class);
        final EWSDependencyProvider ewsDependencyProviderMock = mock(EWSDependencyProvider.class);

        when(EWSDependencyProvider.getInstance()).thenReturn(ewsDependencyProviderMock);
        when(EWSDependencyProvider.getInstance().getTaggingInterface()).thenReturn(appTaggingInterfaceMock);
    }

    @Test
    public void shouldCallOnResumeTaggingLifeCycleInfoWhenAsked() throws Exception {
        final Activity activityMock = mock(Activity.class);

        EWSTagger.collectLifecycleInfo(activityMock);

        verify(appTaggingInterfaceMock).collectLifecycleInfo(activityMock);
    }

    @Test
    public void shouldTagPauseLifeCycleInfoWhenAsked() throws Exception {
        EWSTagger.pauseLifecycleInfo();

        verify(appTaggingInterfaceMock).pauseLifecycleInfo();
    }

    @Test
    public void shouldTrackPageNameWhenAsked() throws Exception {
        final String pageName = "SomePageName";
        EWSTagger.trackPage(pageName);

        verify(appTaggingInterfaceMock).trackPageWithInfo(pageName, null);
    }

    @Test
    public void shouldTrackActionWhenAsked() throws Exception {
        final Map<String, String> map = new HashMap<>();
        map.put(Tag.KEY.IN_APP_NOTIFICATION, Tag.VALUE.CONN_ERROR_NOTIFICATION);

        EWSTagger.trackAction(ACTION.CONNECTION_UNSUCCESSFUL, map);

        verify(appTaggingInterfaceMock).trackActionWithInfo(ACTION.CONNECTION_UNSUCCESSFUL, map);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldTrackActionWithDifferentParamsWhenAsked() throws Exception {
        final ArgumentCaptor<HashMap> mapArgumentCaptor = ArgumentCaptor.forClass(HashMap.class);
        final String key = Tag.KEY.CONNECTED_PRODUCT_NAME;
        final String value = "taggedValue";

        EWSTagger.trackAction(ACTION.CONNECTION_UNSUCCESSFUL, key, value);

        verify(appTaggingInterfaceMock).trackActionWithInfo(eq(ACTION.CONNECTION_UNSUCCESSFUL), mapArgumentCaptor.capture());

        HashMap map = mapArgumentCaptor.getValue();

        assertEquals(1, map.size());
        assertEquals(value, map.get(key));
    }

    @Test
    public void shouldTrackActionSendDataWhenAsked() throws Exception {
        final ArgumentCaptor<HashMap> mapArgumentCaptor = ArgumentCaptor.forClass(HashMap.class);
        final String key = Tag.KEY.SEND_DATA;
        final String successTagValue = "successValueTag";
        EWSTagger.trackActionSendData(key, successTagValue);
        verify(appTaggingInterfaceMock).trackActionWithInfo(eq(key), mapArgumentCaptor.capture());
        HashMap map = mapArgumentCaptor.getValue();
        assertEquals(1, map.size());
        assertEquals(successTagValue, map.get(key));
    }

    @Test
    public void shouldGiveTaggInterfaceWhenAsked() throws Exception {
        EWSTagger.getAppTaggingInterface();
        assertNotNull(appTaggingInterfaceMock);
    }

    @Test
    public void shouldStartTimedActionWhenAsked() throws Exception {
        String timeActionStart = "timeActionStart";
        EWSTagger.startTimedAction(timeActionStart);
        verify(appTaggingInterfaceMock).trackTimedActionStart(eq(timeActionStart));
    }

    @Test
    public void shouldStopTimedActionWhenAsked() throws Exception{
        String timeActionEnd = "timeActionStart";
        EWSTagger.stopTimedAction(timeActionEnd);
        verify(appTaggingInterfaceMock).trackTimedActionEnd(eq(timeActionEnd));
    }


}