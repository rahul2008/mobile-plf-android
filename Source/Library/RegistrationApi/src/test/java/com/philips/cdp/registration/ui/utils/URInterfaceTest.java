package com.philips.cdp.registration.ui.utils;

import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.CustomRobolectricRunner;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

/**
 * Created by philips on 11/30/17.
 */

@RunWith(CustomRobolectricRunner.class)
@org.robolectric.annotation.Config(constants = BuildConfig.class, sdk = 21)
public class URInterfaceTest {

    @Mock
    private RegistrationComponent mockRegistrationComponent;
    @Mock
    private LoggingInterface mockLoggingInterface;

    URInterface urInterface;
    @Mock
    private UiLauncher uiLauncherMock;

    @Mock
    private UappLaunchInput uappLaunchInputMock;
    @Mock
    private UappDependencies uappDependenciesMock;
    @Mock
    private UappSettings uAppSettingsMock;

    @Mock
    private android.content.Context contextMock;
    @Mock
    private com.philips.platform.appinfra.AppInfraInterface appInfraMock;

    @Mock
    private com.philips.platform.appinfra.securestorage.SecureStorageInterface storageMock;


    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        URInterface.setComponent(mockRegistrationComponent);
        RLog.setMockLogger(mockLoggingInterface);

        urInterface=new URInterface();
    }

    @Test
    public void launch() throws Exception {
        urInterface.launch(uiLauncherMock,uappLaunchInputMock);
    }

    @Test(expected = NullPointerException.class)
    public void init() throws Exception {

        //uappSettings.getContext(), uappDependencies.getAppInfra().getSecureStorage()
        Mockito.when(uAppSettingsMock.getContext()).thenReturn(contextMock);
        Mockito.when(appInfraMock.getSecureStorage()).thenReturn(storageMock);
        Mockito.when(uappDependenciesMock.getAppInfra()).thenReturn(appInfraMock);

        urInterface.init(uappDependenciesMock,uAppSettingsMock);
    }

    @Test
    public void getComponent() throws Exception {
        urInterface.getComponent();
    }

    @Test
    public void setComponent() throws Exception {
        urInterface.setComponent(mockRegistrationComponent);
    }

}