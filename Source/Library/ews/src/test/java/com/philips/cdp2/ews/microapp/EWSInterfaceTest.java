/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.microapp;

import android.content.Context;
import android.content.Intent;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.uappinput.UappSettings;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EWSDependencyProvider.class, EWSCallbackNotifier.class})
public class EWSInterfaceTest {

    @Rule
    private ExpectedException thrownException = ExpectedException.none();

    private EWSInterface ewsInterface;

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
    private AppTaggingInterface appTaggingInterfaceMock;

    @Mock
    private Context contextMock;

    @Mock
    private Map<String, String> productKeyMap;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        PowerMockito.mock(EWSDependencyProvider.class);
        PowerMockito.mock(EWSCallbackNotifier.class);

        ewsInterface = new EWSInterface();
        productKeyMap = new HashMap<>();
        productKeyMap.put(EWSInterface.PRODUCT_NAME, "product");
    }

    @Test
    public void itShouldEnsureEWSDependenciesAreInitializedWhenOnInitIsCalled() throws Exception {
        PowerMockito.verifyStatic();

        EWSDependencyProvider.getInstance().initDependencies(appInfraInterfaceMock, productKeyMap);
    }

    @Test
    public void itShouldNotSupportFragmentApproachIfEWSIsLaunchedWithFragmentLauncher() throws Exception {
        thrownException.expect(UnsupportedOperationException.class);
        thrownException.expectMessage(EWSInterface.ERROR_MSG_UNSUPPORTED_LAUNCHER_TYPE);

        ewsInterface.launch(fragmentLauncherMock, null);
    }

    @Test
    public void itShouldInitEWSDependenciesWhenOnInitIsCalled() throws Exception {
        initEWS();

        PowerMockito.verifyStatic();

        EWSDependencyProvider.getInstance().initDependencies(appInfraInterfaceMock, productKeyMap);
    }

    @Test
    public void itShouldLaunchEWSActivityIfLauncherConfigurationIsValid() throws Exception {
        initEWS();
        ewsInterface.launch(activityLauncherMock, new EWSLauncherInput());

        verify(contextMock).startActivity(isA(Intent.class));
    }

    private void initEWS() {
        when(ewsDependenciesMock.getAppInfra()).thenReturn(appInfraInterfaceMock);
        when(ewsDependenciesMock.getProductKeyMap()).thenReturn(productKeyMap);
        when(uappSettingsMock.getContext()).thenReturn(contextMock);

        ewsInterface.init(ewsDependenciesMock, uappSettingsMock);
    }
}