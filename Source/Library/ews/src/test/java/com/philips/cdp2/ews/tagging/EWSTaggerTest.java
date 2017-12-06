/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.tagging;

import android.app.Activity;

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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertTrue;
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
    public void itShouldVerifyConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<EWSTagger> constructor = EWSTagger.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void itShouldCallOnResumeTaggingLifeCycleInfoWhenAsked() throws Exception {
        final Activity activityMock = mock(Activity.class);

        EWSTagger.collectLifecycleInfo(activityMock);

        verify(appTaggingInterfaceMock).collectLifecycleInfo(activityMock);
    }

    @Test
    public void itShouldTagPauseLifeCycleInfoWhenAsked() throws Exception {
        EWSTagger.pauseLifecycleInfo();

        verify(appTaggingInterfaceMock).pauseLifecycleInfo();
    }

    @Test
    public void itShouldTrackPageNameWhenAsked() throws Exception {
        final String pageName = "SomePageName";
        EWSTagger.trackPage(pageName);

        verify(appTaggingInterfaceMock).trackPageWithInfo(pageName, null);
    }

    @Test
    public void itShouldTrackInAppNotificationResponseWhenAsked() throws Exception {
        final ArgumentCaptor<Map> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        final String key = Tag.KEY.SEND_DATA;
        final String notificationResopnseKey = "inAppNotificationResponse";
        final String successTagValue = "successValueTag";
        EWSTagger.trackInAppNotificationResponse(successTagValue);
        verify(appTaggingInterfaceMock).trackActionWithInfo(eq(key), mapArgumentCaptor.capture());
        Map map = mapArgumentCaptor.getValue();
        assertEquals(1, map.size());
        assertEquals(successTagValue, map.get(notificationResopnseKey));
    }
    
    @Test
    public void itShouldTrackInAppNotificationWhenAsked() throws Exception {
        final String pageName = "SomePageName";
        final ArgumentCaptor<Map> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        final String notificationResponse = "inAppNotification";
        final String dialogTagValue = "dialogTagValue";

        EWSTagger.trackInAppNotification(pageName,dialogTagValue);

        verify(appTaggingInterfaceMock).trackPageWithInfo(eq(pageName), mapArgumentCaptor.capture());

        Map map = mapArgumentCaptor.getValue();
        assertEquals(1, map.size());
        assertEquals(dialogTagValue, map.get(notificationResponse));

    }

    @Test
    public void itShouldTrackActionWhenAsked() throws Exception {
        final Map<String, String> map = new HashMap<>();
        map.put(Tag.KEY.IN_APP_NOTIFICATION, Tag.VALUE.CONN_ERROR_NOTIFICATION);

        EWSTagger.trackAction(ACTION.CONNECTION_UNSUCCESSFUL, map);

        verify(appTaggingInterfaceMock).trackActionWithInfo(ACTION.CONNECTION_UNSUCCESSFUL, map);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void itShouldTrackActionWithDifferentParamsWhenAsked() throws Exception {
        final ArgumentCaptor<Map> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        final String key = Tag.KEY.CONNECTED_PRODUCT_NAME;
        final String value = "taggedValue";

        EWSTagger.trackAction(ACTION.CONNECTION_UNSUCCESSFUL, key, value);

        verify(appTaggingInterfaceMock).trackActionWithInfo(eq(ACTION.CONNECTION_UNSUCCESSFUL), mapArgumentCaptor.capture());

        Map map = mapArgumentCaptor.getValue();

        assertEquals(1, map.size());
        assertEquals(value, map.get(key));
    }

    @Test
    public void itShouldTrackActionSendDataWhenAsked() throws Exception {
        final ArgumentCaptor<Map> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        final String key = Tag.KEY.SEND_DATA;
        final String successTagValue = "successValueTag";
        EWSTagger.trackActionSendData(key, successTagValue);
        verify(appTaggingInterfaceMock).trackActionWithInfo(eq(key), mapArgumentCaptor.capture());
        Map map = mapArgumentCaptor.getValue();
        assertEquals(1, map.size());
        assertEquals(successTagValue, map.get(key));
    }

    @Test
    public void itShouldGiveTaggInterfaceWhenAsked() throws Exception {
        EWSTagger.getAppTaggingInterface();
        assertNotNull(appTaggingInterfaceMock);
    }

    @Test
    public void itShouldStartTimedActionWhenAsked() throws Exception {
        String timeActionStart = "timeActionStart";
        EWSTagger.startTimedAction(timeActionStart);
        verify(appTaggingInterfaceMock).trackTimedActionStart(eq(timeActionStart));
    }

    @Test
    public void itShouldStopTimedActionWhenAsked() throws Exception{
        String timeActionEnd = "timeActionStart";
        EWSTagger.stopTimedAction(timeActionEnd);
        verify(appTaggingInterfaceMock).trackTimedActionEnd(eq(timeActionEnd));
    }


}