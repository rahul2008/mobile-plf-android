/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.baseapp.base;

import android.app.Activity;

import com.philips.platform.appframework.homescreen.HamburgerActivity;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class AppFrameworkTaggingTest extends TestCase {
    private AppTaggingInterface appTaggingInterface;
    private AppInfraInterface appInfraInterface;
    private AppFrameworkApplication context;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        super.setUp();
        appTaggingInterface = mock(AppTaggingInterface.class);
        context = mock(AppFrameworkApplication.class);
        appInfraInterface = mock(AppInfraInterface.class);
        when(context.getAppInfra()).thenReturn(appInfraInterface);
        when(context.getAppInfra().getTagging()).thenReturn(appTaggingInterface);
        AppFrameworkTagging.getInstance().initAppTaggingInterface(context);
    }

    /*
     * If pageName is null then page track should not happen.
     */
    @Test
    public void testTrackPageNameShouldNotNull() throws Exception {
        String pageName = null;
        AppFrameworkTagging.getInstance().trackPage(pageName);
        verify(appTaggingInterface, times(0)).trackPageWithInfo(pageName, null);
    }

    @Test
    public void testTrackPage() throws Exception {
        String pageName = "AnyPageName";
        AppFrameworkTagging.getInstance().trackPage(pageName);
        verify(appTaggingInterface, times(1)).trackPageWithInfo(pageName, null);
    }


    /*
     * If actionName is null then event track should not happen.
     */
    @Test
    public void testTrackActionNameShouldNotNull() throws Exception {
        String actionName = null;
        AppFrameworkTagging.getInstance().trackAction(actionName);
        verify(appTaggingInterface, times(0)).trackActionWithInfo(actionName, null);
    }

    @Test
    public void testTrackAction() throws Exception {
        String actionName = "AnyActionName";
        AppFrameworkTagging.getInstance().trackAction(actionName);
        verify(appTaggingInterface, times(1)).trackActionWithInfo(actionName, null);
    }

    /*
 * If actionName is null then event track should not happen.
 */
    @Test
    public void testTrackLifeCycleShouldNotNull() throws Exception {
        Activity activity = null;
        AppFrameworkTagging.getInstance().collectLifecycleData(activity);
        verify(appTaggingInterface, times(0)).collectLifecycleInfo(activity);
    }

    @Test
    public void testTrackLifeCycle() throws Exception {
        Activity activity = mock(HamburgerActivity.class);
        AppFrameworkTagging.getInstance().collectLifecycleData(activity);
        verify(appTaggingInterface, times(1)).collectLifecycleInfo(activity);
    }

    @Test
    public void testPauseLifeCycle() throws Exception {
        AppFrameworkTagging.getInstance().pauseCollectingLifecycleData();
        verify(appTaggingInterface, times(1)).pauseLifecycleInfo();
    }
}