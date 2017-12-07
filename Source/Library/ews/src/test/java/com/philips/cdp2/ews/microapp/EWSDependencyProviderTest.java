/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.microapp;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.ews.configuration.ContentConfiguration;
import com.philips.cdp2.ews.injections.DaggerEWSComponent;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.injections.EWSConfigurationModule;
import com.philips.cdp2.ews.injections.EWSModule;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.thememanager.ThemeConfiguration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;


@RunWith(PowerMockRunner.class)
@PrepareForTest({DaggerEWSComponent.class,CommCentral.class,EWSDependencyProvider.class})
public class EWSDependencyProviderTest {

    @Mock
    ActionBarListener mockActionBarListener;
    @Mock
    private
    FragmentActivity mockFragmentActivity;
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
    @Mock
    private ThemeConfiguration mockThemeConfiguration;
    @Mock
    private Context mockContext;
    private Map<String, String> productKeyMap;
    @Mock
    DaggerEWSComponent.Builder mockDaggerEWSComponentBuilder;

    @Mock
    private CommCentral mockCommCentral;

    @Before
    public void setUp() throws Exception {
        mockStatic(DaggerEWSComponent.class);
        initMocks(this);

        when(appInfraInterfaceMock.getTagging()).thenReturn(taggingInterfaceMock);
        when(appInfraInterfaceMock.getTagging().createInstanceForComponent(anyString(), anyString())).thenReturn(taggingInterfaceMock);

        when(appInfraInterfaceMock.getLogging()).thenReturn(loggingInterfaceMock);
        when(appInfraInterfaceMock.getLogging().createInstanceForComponent(anyString(), anyString())).thenReturn(loggingInterfaceMock);

        subject = spy(EWSDependencyProvider.getInstance());
        subject.setThemeConfiguration(mockThemeConfiguration);


        when(DaggerEWSComponent.builder()).thenReturn(mockDaggerEWSComponentBuilder);

        productKeyMap = new HashMap<>();
        productKeyMap.put(EWSUapp.PRODUCT_NAME, "product");

    }

    @Test
    public void itShouldEnsureAllDependenciesAreInitialized() throws Exception {
        subject.initDependencies(appInfraInterfaceMock, productKeyMap, mockCommCentral);

        assertTrue(subject.areDependenciesInitialized());
        assertNotNull(subject.getAppInfra());
        assertNotNull(subject.getLoggerInterface());
        assertNotNull(subject.getTaggingInterface());
        assertNotNull(subject.getProductName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void itShouldThrowExceptionIfMapDoesNotContainProductName() throws Exception {
        subject.initDependencies(appInfraInterfaceMock, new HashMap<String, String>(), mockCommCentral);
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
        assertSame(subject.getEwsComponent(), mockEWSComponent);
    }

    @Test
    public void itShouldVerifyGettingThemeConfiguration() throws Exception{
        assertSame(subject.getThemeConfiguration(),mockThemeConfiguration);

    }

    @Test
    public void itShouldVerifyContext() throws Exception{
        subject.setContext(mockContext);
        assertSame(subject.context,mockContext);

    }

    @Test
    public void itShouldVerifyCreateEwsComponent() throws Exception{

        when(mockDaggerEWSComponentBuilder.eWSConfigurationModule(any(EWSConfigurationModule.class))).thenReturn(mockDaggerEWSComponentBuilder);
        when(mockDaggerEWSComponentBuilder.eWSModule(any(EWSModule.class))).thenReturn(mockDaggerEWSComponentBuilder);


        subject.createEWSComponent(mockFragmentActivity,123,mock(ContentConfiguration.class));

        ArgumentCaptor<EWSConfigurationModule> argCaptorEWSConfigurationModule = ArgumentCaptor.forClass(EWSConfigurationModule.class);
        ArgumentCaptor<EWSModule> argCaptorEWSModule = ArgumentCaptor.forClass(EWSModule.class);

        //verify(DaggerEWSComponent).builder();
        verify(mockDaggerEWSComponentBuilder).eWSModule(any(EWSModule.class));
        verify(mockDaggerEWSComponentBuilder).eWSConfigurationModule(any(EWSConfigurationModule.class));
        verify(mockDaggerEWSComponentBuilder).build();
    }
}