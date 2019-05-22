package com.philips.platform.pim.manager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.pif.DataInterface.USR.enums.Error;
import com.philips.platform.pim.configration.PIMOIDCConfigration;
import com.philips.platform.pim.fragment.PIMFragment;
import com.philips.platform.pim.listeners.PIMLoginListener;

import junit.framework.TestCase;

import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationServiceConfiguration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.DEBUG;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

//TODO: Shashi, Add unit test cases.
@PrepareForTest({Uri.class, PIMSettingManager.class, AuthorizationRequest.class, AuthorizationRequest.Builder.class, AuthorizationServiceConfiguration.class, AuthorizationResponse.class, AuthorizationException.class})
@RunWith(PowerMockRunner.class)
public class PIMLoginManagerTest extends TestCase {

    private PIMLoginManager pimLoginManager;
    @Mock
    private PIMOIDCConfigration mockPimoidcConfigration;
    @Mock
    private LoggingInterface mockLoggingInterface;
    @Mock
    private PIMSettingManager mockPimSettingManager;
    @Mock
    private PIMLoginListener mockPimLoginListener;
    @Mock
    private Uri mockUri;
    @Mock
    private Intent mockIntent;
    @Mock
    private AuthorizationServiceConfiguration mockAuthorizationServiceConfiguration;
    @Mock
    private Context mockContext;
    @Mock
    private Bundle mockBundle;
    @Mock
    private PIMFragment mockPimFragment;
    @Mock
    private AppInfraInterface mockAppInfraInterface;
    @Mock
    private AppConfigurationInterface mockAppConfigurationInterface;
    @Mock
    private AppConfigurationInterface.AppConfigurationError mockAppConfigurationError;


    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);

        mockStatic(PIMSettingManager.class);
        when(PIMSettingManager.getInstance()).thenReturn(mockPimSettingManager);
        when(mockPimSettingManager.getLoggingInterface()).thenReturn(mockLoggingInterface);
        pimLoginManager = new PIMLoginManager(mockPimoidcConfigration);
        when(mockPimSettingManager.getAppInfraInterface()).thenReturn(mockAppInfraInterface);
        when(mockAppInfraInterface.getConfigInterface()).thenReturn(mockAppConfigurationInterface);
        whenNew(AppConfigurationInterface.AppConfigurationError.class).withNoArguments().thenReturn(mockAppConfigurationError);
    }

  /*  @Test
    public void verifyLogOnLoginFailed_WhenContextIsNull() {
        pimLoginManager.oidcLogin(null, null, mockPimFragment,mockPimLoginListener);
        verify(mockLoggingInterface).log(DEBUG, PIMLoginManager.class.getSimpleName(), "OIDC Login failed, Reason : context is null.");
        Error error = new Error(Error.UserDetailError.NetworkError)
        verify(mockPimLoginListener).onLoginFailed(0);
    }

    @Test
    public void verifyLogOnLoginFailed_WhenPimoidcConfigrationIsNull() {
        pimLoginManager = new PIMLoginManager(null);
        pimLoginManager.oidcLogin(mockContext, mockBundle, mockPimFragment,mockPimLoginListener);
        verify(mockLoggingInterface).log(DEBUG, PIMLoginManager.class.getSimpleName(), "OIDC Login failed, Reason : PIMOIDCConfigration is null.");
        verify(mockPimLoginListener).onLoginFailed(0);
    }

    @Test
    public void verifyLogOnLoginFailed_WhenAuthRequestIsNull() {
        PIMOIDCConfigration pimoidcConfigration = new PIMOIDCConfigration(mockAuthorizationServiceConfiguration, mockAppInfraInterface);
        when(mockAppConfigurationInterface.getPropertyForKey("Dummy_ClientID", "PIM", mockAppConfigurationError)).thenReturn("clientId");
        pimLoginManager.oidcLogin(mockContext, mockBundle,mockPimFragment, mockPimLoginListener);
        verify(mockLoggingInterface).log(DEBUG, PIMLoginManager.class.getSimpleName(), "OIDC Login failed, Reason : authReqIntent is null.");
        verify(mockPimLoginListener).onLoginFailed(0);
    }

    @Test
    public void verifyOnLoginFailed_WhenContextIsNullInExchangeAuthorizationCode() {
        pimLoginManager.oidcLogin(mockContext, mockBundle,mockPimFragment, mockPimLoginListener);
        pimLoginManager.exchangeAuthorizationCode(null, mockIntent);
        verify(mockLoggingInterface).log(DEBUG, PIMLoginManager.class.getSimpleName(), "Token request failed, Reason : context is null.");
    }

    @Test
    public void verifyOnLoginFailed_WhenIntentIsNullInExchangeAuthorizationCode() {
        pimLoginManager.oidcLogin(mockContext, mockBundle,mockPimFragment, mockPimLoginListener);
        pimLoginManager.exchangeAuthorizationCode(mockContext, null);
        verify(mockLoggingInterface).log(DEBUG, PIMLoginManager.class.getSimpleName(), "Token request failed, Reason : dataIntent is null.");
    }*/


    public void tearDown() throws Exception {
    }
}