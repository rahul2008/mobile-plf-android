/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.microapp;

import android.support.v4.app.FragmentActivity;

import com.philips.cdp2.ews.configuration.ContentConfiguration;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class EWSDependencyProviderTest {

    @Mock
    private
    FragmentActivity mockFragmentActivity;
    @Mock
    ActionBarListener mockActionBarListener;

    @Mock
    private
    EWSComponent mockEWSComponent;

    private EWSDependencyProvider subject;
    @Mock
    private AppInfraInterface appInfraInterfaceMock;
    @Mock
    private LoggingInterface loggingInterfaceMock;
    @Mock
    private AppTaggingInterface taggingInterfaceMock;
    private Map<String, String> productKeyMap;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        when(appInfraInterfaceMock.getTagging()).thenReturn(taggingInterfaceMock);
        when(appInfraInterfaceMock.getTagging().createInstanceForComponent(anyString(), anyString())).thenReturn(taggingInterfaceMock);

        when(appInfraInterfaceMock.getLogging()).thenReturn(loggingInterfaceMock);
        when(appInfraInterfaceMock.getLogging().createInstanceForComponent(anyString(), anyString())).thenReturn(loggingInterfaceMock);

        subject = spy(EWSDependencyProvider.getInstance());
        productKeyMap = new HashMap<>();
        productKeyMap.put(EWSInterface.PRODUCT_NAME, "product");
    }

    @Test
    public void itShouldEnsureAllDependenciesAreInitialized() throws Exception {
        subject.initDependencies(appInfraInterfaceMock, productKeyMap);

        assertTrue(subject.areDependenciesInitialized());
        assertNotNull(subject.getAppInfra());
        assertNotNull(subject.getLoggerInterface());
        assertNotNull(subject.getTaggingInterface());
        assertNotNull(subject.getProductName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldThrowExceptionIfMapDoesNotContainProductName() throws Exception {
        subject.initDependencies(appInfraInterfaceMock, new HashMap<String, String>());
    }

    @Test(expected = IllegalStateException.class)
    public void itShouldThrowExceptionIfProductNameCalledWithoutInitialization() throws Exception {
        subject.getProductName();
    }

    @Test
    public void itShouldClearAllDependenciesWhenAsked() throws Exception {
        subject.clear();

        assertFalse(subject.areDependenciesInitialized());
        assertNull(subject.getAppInfra());
    }

    //Todo need to update test case
   /* @Test
    public void itShouldVerifyEWSComponentCreate() throws Exception {
        subject.createEWSComponent(new FragmentLauncher(mockFragmentActivity, 123, mockActionBarListener), new ContentConfiguration());
        verify(subject).createEWSComponent(any(FragmentActivity.class), anyInt(), any(ContentConfiguration.class));
    }*/

    @Test
    public void itShouldVerifyCreatedEWSComponentIsGettingOrNot() throws Exception {
        doAnswer(new Answer() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                subject.ewsComponent = mockEWSComponent;
                return null;
            }
        }).when(subject).createEWSComponent(any(FragmentActivity.class), anyInt(), any(ContentConfiguration.class));
        subject.createEWSComponent(new FragmentLauncher(mockFragmentActivity, 123, mockActionBarListener), new ContentConfiguration());
        assertSame(subject.getEwsComponent(),mockEWSComponent);
    }

}