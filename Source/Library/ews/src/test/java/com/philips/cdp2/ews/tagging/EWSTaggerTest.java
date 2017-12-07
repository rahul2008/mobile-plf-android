/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.tagging;

import android.app.Activity;

import com.philips.cdp2.ews.tagging.Tag.ACTION;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
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
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
public class EWSTaggerTest {

    @Mock
    private AppTaggingInterface mockTaggingInterface;

    private EWSTagger subject;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        subject = new EWSTagger(mockTaggingInterface);

    }
    @Ignore
    @Deprecated
    @Test
    //todo constructor is not private anymore
    public void itShouldVerifyConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<EWSTagger> constructor = EWSTagger.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }

    @Test
    public void itShouldCallOnResumeTaggingLifeCycleInfoWhenAsked() throws Exception {
        final Activity activityMock = mock(Activity.class);

        subject.collectLifecycleInfo(activityMock);

        verify(mockTaggingInterface).collectLifecycleInfo(activityMock);
    }

    @Test
    public void itShouldTagPauseLifeCycleInfoWhenAsked() throws Exception {
        subject.pauseLifecycleInfo();

        verify(mockTaggingInterface).pauseLifecycleInfo();
    }

    @Test
    public void itShouldTrackPageNameWhenAsked() throws Exception {
        final String pageName = "SomePageName";
        subject.trackPage(pageName);

        verify(mockTaggingInterface).trackPageWithInfo(pageName, null);
    }

    @Test
    public void itShouldTrackInAppNotificationResponseWhenAsked() throws Exception {
        final ArgumentCaptor<Map> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        final String key = Tag.KEY.SEND_DATA;
        final String notificationResopnseKey = "inAppNotificationResponse";
        final String successTagValue = "successValueTag";
        subject.trackInAppNotificationResponse(successTagValue);
        verify(mockTaggingInterface).trackActionWithInfo(eq(key), mapArgumentCaptor.capture());
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

        subject.trackInAppNotification(pageName,dialogTagValue);

        verify(mockTaggingInterface).trackPageWithInfo(eq(pageName), mapArgumentCaptor.capture());

        Map map = mapArgumentCaptor.getValue();
        assertEquals(1, map.size());
        assertEquals(dialogTagValue, map.get(notificationResponse));

    }

    @Test
    public void itShouldTrackActionWhenAsked() throws Exception {
        final Map<String, String> map = new HashMap<>();
        map.put(Tag.KEY.IN_APP_NOTIFICATION, Tag.VALUE.CONN_ERROR_NOTIFICATION);

        subject.trackAction(ACTION.CONNECTION_UNSUCCESSFUL, map);

        verify(mockTaggingInterface).trackActionWithInfo(ACTION.CONNECTION_UNSUCCESSFUL, map);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void itShouldTrackActionWithDifferentParamsWhenAsked() throws Exception {
        final ArgumentCaptor<Map> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        final String key = Tag.KEY.CONNECTED_PRODUCT_NAME;
        final String value = "taggedValue";

        subject.trackAction(ACTION.CONNECTION_UNSUCCESSFUL, key, value);

        verify(mockTaggingInterface).trackActionWithInfo(eq(ACTION.CONNECTION_UNSUCCESSFUL), mapArgumentCaptor.capture());

        Map map = mapArgumentCaptor.getValue();

        assertEquals(1, map.size());
        assertEquals(value, map.get(key));
    }

    @Test
    public void itShouldTrackActionSendDataWhenAsked() throws Exception {
        final ArgumentCaptor<Map> mapArgumentCaptor = ArgumentCaptor.forClass(Map.class);
        final String key = Tag.KEY.SEND_DATA;
        final String successTagValue = "successValueTag";
        subject.trackActionSendData(key, successTagValue);
        verify(mockTaggingInterface).trackActionWithInfo(eq(key), mapArgumentCaptor.capture());
        Map map = mapArgumentCaptor.getValue();
        assertEquals(1, map.size());
        assertEquals(successTagValue, map.get(key));
    }

    @Test
    public void itShouldGiveTaggInterfaceWhenAsked() throws Exception {
        subject.getAppTaggingInterface();
        assertNotNull(mockTaggingInterface);
    }

    @Test
    public void itShouldStartTimedActionWhenAsked() throws Exception {
        String timeActionStart = "timeActionStart";
        subject.startTimedAction(timeActionStart);
        verify(mockTaggingInterface).trackTimedActionStart(eq(timeActionStart));
    }

    @Test
    public void itShouldStopTimedActionWhenAsked() throws Exception{
        String timeActionEnd = "timeActionStart";
        subject.stopTimedAction(timeActionEnd);
        verify(mockTaggingInterface).trackTimedActionEnd(eq(timeActionEnd));
    }


}