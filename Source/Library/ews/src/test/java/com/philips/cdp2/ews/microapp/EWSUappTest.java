/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.microapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.logger.EWSLogger;
import com.philips.cdp2.ews.navigation.Navigator;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EWSLogger.class, CommCentral.class})
public class EWSUappTest {

    @Mock
    LoggingInterface mockLoggingInterface;
    @Mock
    AppTaggingInterface mockAppTaggingInterface;
    @Rule
    private ExpectedException thrownException = ExpectedException.none();
    private EWSUapp subject;
    @Mock
    private AppInfraInterface appInfraInterfaceMock;
    @Mock
    private EWSDependencies ewsDependenciesMock;
    @Mock
    private UappSettings uappSettingsMock;
    @Mock
    private FragmentLauncher fragmentLauncherMock;
    @Mock
    private ActivityLauncher activityLauncherMock;
    @Mock
    private Context contextMock;
    @Mock
    private Navigator mockNavigator;
    @Mock
    private Map<String, String> productKeyMap;
    @Mock
    private EWSComponent mockEwsComponent;
    @Mock
    private FragmentActivity mockFragmentActivity;
    @Mock
    private CommCentral mockCommCentral;
    @Mock
    private EWSTagger mockEWSTagger;
    @Mock
    private EWSLogger mockEWSLogger;

    @Before
    public void setUp() throws Exception {
        mockStatic(EWSLogger.class);
        mockStatic(CommCentral.class);
        initMocks(this);

        subject = spy(new EWSUapp());
        productKeyMap = new HashMap<>();
        productKeyMap.put(EWSUapp.PRODUCT_NAME, "product");

//
        doReturn(mockEwsComponent).when(subject).createEWSComponent(any(FragmentLauncher.class));

//        doAnswer(new Answer() {
//            @Override
//            public Void answer(InvocationOnMock invocation) throws Throwable {
//                return null;
//            }
//        }).when(EWSDependencyProvider.getInstance()).createEWSComponent(any(FragmentLauncher.class), any(ContentConfiguration.class));

        doAnswer(new Answer() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                subject.navigator = mockNavigator;
                return null;
            }
        }).when(mockEwsComponent).inject(any(EWSUapp.class));

        when(mockEwsComponent.getEWSTagger()).thenReturn(mockEWSTagger);
        when(mockEwsComponent.getEWSLogger()).thenReturn(mockEWSLogger);
    }

    @Test
    public void itShouldEnsureEWSDependenciesAreInitializedWhenOnInitIsCalled() throws Exception {
        verifyStatic();

        //EWSDependencyProvider.getInstance().initDependencies(appInfraInterfaceMock, productKeyMap, mockCommCentral);
    }

    @Test
    public void itShouldInitEWSDependenciesWhenOnInitIsCalled() throws Exception {
        initEWS();
        verifyStatic();
        // EWSDependencyProvider.getInstance().initDependencies(appInfraInterfaceMock, productKeyMap, mockCommCentral);
    }

    @Test
    public void itShouldLaunchEWSActivityIfLauncherConfigurationIsValid() throws Exception {
        initEWS();
        subject.launch(activityLauncherMock, new EWSLauncherInput());
        verify(contextMock).startActivity(isA(Intent.class));
    }

    @Test
    public void itShouldLaunchEWSAsFragmentIfLauncherConfigurationIsValid() throws Exception {
        initEWS();

        doReturn(mock(FragmentActivity.class, withSettings().extraInterfaces(EWSActionBarListener.class)))
                .when(fragmentLauncherMock).getFragmentActivity();
        subject.launch(fragmentLauncherMock, new EWSLauncherInput());
        verify(subject).launchAsFragment(any(FragmentLauncher.class), any(UappLaunchInput.class));
    }

    @Test
    public void itShouldLaunchEWSAsFragmentIfLauncherConfigurationIsNotValid() throws Exception {
        thrownException.expect(UnsupportedOperationException.class);
        thrownException.expectMessage(EWSUapp.ERROR_MSG_INVALID_IMPLEMENTATION);
        initEWS();
        subject.launch(fragmentLauncherMock, new EWSLauncherInput());
        verify(subject).launchAsFragment(any(FragmentLauncher.class), any(UappLaunchInput.class));
    }

    @Test
    public void itShouldThrowAnErrorIfLauncherConfigurationIsNotValid() throws Exception {
        initEWS();
        thrownException.expect(UnsupportedOperationException.class);
        thrownException.expectMessage(EWSUapp.ERROR_MSG_INVALID_CALL);
        thrownException.expectMessage(EWSUapp.ERROR_MSG_INVALID_IMPLEMENTATION);

        subject.launch(fragmentLauncherMock, new EWSLauncherInput());
    }

    @Test
    public void itShouldNavigateToFirstFragmentOnFragmentLauncher() throws Exception {
        doReturn(mock(FragmentActivity.class)).when(fragmentLauncherMock).getFragmentActivity();
        doReturn(1).when(fragmentLauncherMock).getParentContainerResourceID();
        subject.launchAsFragment(fragmentLauncherMock, new EWSLauncherInput());
        verify(mockNavigator).navigateToGettingStartedScreen();
    }

    @Test
    public void itShouldVerifyLaunchAsFragmentOnErrorCatchBlockCalled() throws Exception {
        doThrow(new IllegalStateException("error")).when(mockNavigator).navigateToGettingStartedScreen();
        subject.launchAsFragment(fragmentLauncherMock, new EWSLauncherInput());
        verify(mockEWSLogger).e(anyString(), anyString());
    }

    private void initEWS() {
        when(ewsDependenciesMock.getAppInfra()).thenReturn(appInfraInterfaceMock);
        when(ewsDependenciesMock.getAppInfra().getLogging()).thenReturn(mockLoggingInterface);
        when(ewsDependenciesMock.getAppInfra().getTagging()).thenReturn(mockAppTaggingInterface);
        when(ewsDependenciesMock.getProductKeyMap()).thenReturn(productKeyMap);
        when(uappSettingsMock.getContext()).thenReturn(contextMock);
        subject.init(ewsDependenciesMock, uappSettingsMock);
    }
}