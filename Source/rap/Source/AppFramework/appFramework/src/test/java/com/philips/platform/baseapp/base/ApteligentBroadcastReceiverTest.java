/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.baseapp.base;

import android.content.Intent;

import com.crittercism.app.Crittercism;
import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.annotation.Config;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.philips.platform.baseapp.base.ApteligentBroadcastReceiver.ACTION_NAME;
import static com.philips.platform.baseapp.base.ApteligentBroadcastReceiver.ACTION_TAGGING_DATA;
import static com.philips.platform.baseapp.base.ApteligentBroadcastReceiver.PAGE_NAME;
import static com.philips.platform.baseapp.base.ApteligentBroadcastReceiver.TAGGING_DATA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class)
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
@PrepareForTest(Crittercism.class)
public class ApteligentBroadcastReceiverTest {
    private ApteligentBroadcastReceiver broadcastReceiver;
    private AppFrameworkApplication mockAppFrameworkApplication;
    private Intent intent = null;
    private AppInfraInterface appInfraInterface = null;
    private AppTaggingInterface appTaggingInterface = null;

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(Crittercism.class);
        initMocks(this);

        intent = new Intent(ACTION_TAGGING_DATA);

        appInfraInterface = mock(AppInfraInterface.class);
        appTaggingInterface = mock(AppTaggingInterface.class);
        mockAppFrameworkApplication = mock(AppFrameworkApplication.class);

        when(mockAppFrameworkApplication.getAppInfra()).thenReturn(appInfraInterface);
        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);

        broadcastReceiver = new ApteligentBroadcastReceiver(mockAppFrameworkApplication);
    }

    @Test
    public void testTrackCorrectPageWelcomeScreen() throws Exception {
        Map<String, Object> contextData = addAnalyticsDataObjectForPage("Welcome");
        intent.putExtra(TAGGING_DATA, (Serializable)contextData);

        broadcastReceiver.onReceive(mockAppFrameworkApplication, intent);

        // Verify that the following 'leaveBreadcrumb' static mock method was called exactly 1 time
        PowerMockito.verifyStatic(Mockito.times(1));
        Crittercism.leaveBreadcrumb("{\"trackPage: \":\"Welcome\"}");
    }

    @Test
    public void testTrackSomeActionWhenPageNameIsNull() throws Exception {
        Map<String, Object> contextData = addAnalyticsDataObjectForPage(null);
        intent.putExtra(TAGGING_DATA, (Serializable)contextData);

        broadcastReceiver.onReceive(mockAppFrameworkApplication, intent);

        // Verify that the 'leaveBreadcrumb' static mock method was never called
        PowerMockito.verifyStatic(Mockito.never());
        Crittercism.leaveBreadcrumb("{\"trackPage: \":\"Welcome\"}");


        Map<String, Object> contextDataAction = addAnalyticsDataObjectForAction(null);
        intent.putExtra(TAGGING_DATA, (Serializable)contextDataAction);

        broadcastReceiver.onReceive(mockAppFrameworkApplication, intent);

        // Verify that the 'leaveBreadcrumb' static mock method was never called
        PowerMockito.verifyStatic(Mockito.never());
        Crittercism.leaveBreadcrumb("{\"trackAction: \":\"Skip\"}");
    }

    @Test
    public void testTrackSomeCorrectActionEvent() throws Exception {
        Map<String, Object> contextData = addAnalyticsDataObjectForAction("SomeCorrectAction");
        intent.putExtra(TAGGING_DATA, (Serializable)contextData);

        broadcastReceiver.onReceive(mockAppFrameworkApplication, intent);

        // Verify that the following 'leaveBreadcrumb' static mock method was called exactly 1 time
        PowerMockito.verifyStatic(Mockito.times(1));
        Crittercism.leaveBreadcrumb("{\"trackAction: \":\"SomeCorrectAction\"}");
    }

    @After
    public void releaseResources() throws Exception {
        broadcastReceiver = null;
        intent = null;
    }

    /**
     * Constructing default Object data for Page event
     **/
    private Map<String, Object> addAnalyticsDataObjectForPage(String pageName) {
        final Map<String, Object> contextData = new HashMap<>();
        contextData.put(PAGE_NAME, pageName);
        return contextData;
    }

    /**
     * Constructing default Object data for action event
     **/
    private Map<String, Object> addAnalyticsDataObjectForAction(String actionName) {
        final Map<String, Object> contextData = new HashMap<>();
        contextData.put(ACTION_NAME, actionName);
        return contextData;
    }
}