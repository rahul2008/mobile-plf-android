package com.philips.platform.pim.manager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.adobe.mobile.Analytics;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.pif.DataInterface.USR.enums.Error;
import com.philips.platform.pim.configration.PIMOIDCConfigration;
import com.philips.platform.pim.listeners.PIMLoginListener;
import com.philips.platform.pim.listeners.PIMTokenRequestListener;
import com.philips.platform.pim.listeners.PIMUserMigrationListener;
import com.philips.platform.pim.listeners.PIMUserProfileDownloadListener;

import junit.framework.TestCase;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

//TODO: Shashi, Add unit test cases.
@PrepareForTest({Uri.class, Analytics.class, PIMSettingManager.class, AuthorizationRequest.class, AuthorizationRequest.Builder.class, AuthorizationServiceConfiguration.class, AuthorizationResponse.class,
        AuthorizationException.class, PIMAuthManager.class, PIMLoginManager.class})
@RunWith(PowerMockRunner.class)
public class PIMLoginManagerTest extends TestCase {

    private PIMLoginManager pimLoginManager;

    @Mock
    private AuthState mockAuthState;
    @Mock
    private Intent mockIntent;
    @Mock
    private PIMAuthManager mockAuthManager;
    @Mock
    private PIMUserManager mockUserManager;
    @Mock
    AppTaggingInterface mockTaggingInterface;
    @Mock
    private PIMLoginListener mockPimLoginListener;
    @Captor
    private ArgumentCaptor<PIMTokenRequestListener> listenerArgumentCaptor;
    @Captor
    private ArgumentCaptor<PIMUserProfileDownloadListener> userProfileDwnldLstnrCaptor;
    @Mock
    private AuthorizationServiceConfiguration mockAuthorizationServiceConfiguration;
    @Mock
    private AppConfigurationInterface.AppConfigurationError mockAppConfigurationError;

    private String redirectrURI = "com.philips.apps.94e28300-565d-4110-8919-42dc4f817393://oauthredirect";

    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);

        mockStatic(PIMSettingManager.class);
        mockStatic(Analytics.class);

        PIMOIDCConfigration mockPimoidcConfigration = mock(PIMOIDCConfigration.class);
        AuthorizationService mockAuthorizationService = mock(AuthorizationService.class);
        LoggingInterface mockLoggingInterface = mock(LoggingInterface.class);
        PIMSettingManager mockPimSettingManager = mock(PIMSettingManager.class);
        Context mockContext = mock(Context.class);
        AppInfraInterface mockAppInfraInterface = mock(AppInfraInterface.class);
        AppConfigurationInterface mockAppConfigurationInterface = mock(AppConfigurationInterface.class);

        when(PIMSettingManager.getInstance()).thenReturn(mockPimSettingManager);
        when(mockPimSettingManager.getLoggingInterface()).thenReturn(mockLoggingInterface);
        when(mockPimSettingManager.getPimUserManager()).thenReturn(mockUserManager);
        when(mockPimSettingManager.getAppInfraInterface()).thenReturn(mockAppInfraInterface);
        when(mockAppInfraInterface.getConfigInterface()).thenReturn(mockAppConfigurationInterface);
        whenNew(AppConfigurationInterface.AppConfigurationError.class).withNoArguments().thenReturn(mockAppConfigurationError);
        when(PIMSettingManager.getInstance()).thenReturn(mockPimSettingManager);
        when(mockPimSettingManager.getLoggingInterface()).thenReturn(mockLoggingInterface);
        when(mockPimSettingManager.getAppInfraInterface()).thenReturn(mockAppInfraInterface);
        when(mockPimSettingManager.getLocale()).thenReturn("en-US");
        when(mockPimSettingManager.getTaggingInterface()).thenReturn(mockTaggingInterface);
        when(mockTaggingInterface.getPrivacyConsent()).thenReturn(AppTaggingInterface.PrivacyStatus.UNKNOWN);
        when(mockAppInfraInterface.getConfigInterface()).thenReturn(mockAppConfigurationInterface);
        String customeClaims = new PIMOIDCConfigration().getCustomClaims();
        when(mockPimoidcConfigration.getCustomClaims()).thenReturn(customeClaims);
        when(mockPimoidcConfigration.getAuthorizationServiceConfiguration()).thenReturn(mockAuthorizationServiceConfiguration);
        when(mockPimoidcConfigration.getClientId()).thenReturn("94e28300-565d-4110-8919-42dc4f817393");
        when(mockPimoidcConfigration.getRedirectUrl()).thenReturn(redirectrURI);
        when(mockPimoidcConfigration.getrsID()).thenReturn("philipspimregistrationdev");
        whenNew(AuthorizationService.class).withArguments(mockContext).thenReturn(mockAuthorizationService);
        whenNew(PIMAuthManager.class).withArguments(mockContext).thenReturn(mockAuthManager);
        when(mockAuthManager.getAuthState()).thenReturn(mockAuthState);

        pimLoginManager = new PIMLoginManager(mockContext, mockPimoidcConfigration);
    }

    @Test
    public void testGetAuthReqIntent() throws Exception {
        mockStatic(Uri.class);
        Uri mockUri = Mockito.mock(Uri.class);
        when(Uri.class, "parse", anyString()).thenReturn(mockUri);
        when(Analytics.getTrackingIdentifier()).thenReturn("66512F65BFEF4D06-060C8F54D06DE6AF");
        when(mockTaggingInterface.getTrackingIdentifier()).thenReturn("66512F65BFEF4D06-060C8F54D06DE6AF");
        PIMLoginListener mockPimLoginListener = mock(PIMLoginListener.class);
        pimLoginManager.getAuthReqIntent(mockPimLoginListener);
        verify(mockAuthManager).getAuthorizationRequestIntent(eq(mockAuthorizationServiceConfiguration), anyString(), anyString(), any(HashMap.class));
    }

    @Test
    public void testIsAuthorizationSuccess() {
        pimLoginManager.isAuthorizationSuccess(mockIntent);
        verify(mockAuthManager).isAuthorizationSuccess(mockIntent);
    }

    @Test
    public void testExchangeAuthorizeCode() throws Exception {
        pimLoginManager.exchangeAuthorizationCode(mockIntent);
        verify(mockAuthManager).performTokenRequest(eq(mockIntent), listenerArgumentCaptor.capture());
        PIMTokenRequestListener requestListener = listenerArgumentCaptor.getValue();
        requestListener.onTokenRequestSuccess();

        Whitebox.setInternalState(pimLoginManager, "mPimLoginListener", mockPimLoginListener);
        Error mockError = mock(Error.class);
        requestListener.onTokenRequestFailed(mockError);
        verify(mockPimLoginListener).onLoginFailed(mockError);
    }

    @Test
    public void testRequestUserProfile() throws Exception {
        Whitebox.setInternalState(pimLoginManager, "mPimLoginListener", mockPimLoginListener);
        pimLoginManager.exchangeAuthorizationCode(mockIntent);
        verify(mockAuthManager).performTokenRequest(eq(mockIntent), listenerArgumentCaptor.capture());
        PIMTokenRequestListener requestListener = listenerArgumentCaptor.getValue();
        requestListener.onTokenRequestSuccess();

        verify(mockUserManager).requestUserProfile(eq(mockAuthState), userProfileDwnldLstnrCaptor.capture());
        PIMUserProfileDownloadListener downloadListener = userProfileDwnldLstnrCaptor.getValue();
        downloadListener.onUserProfileDownloadSuccess();
        verify(mockPimLoginListener).onLoginSuccess();

        Error mockError = mock(Error.class);
        downloadListener.onUserProfileDownloadFailed(mockError);
        verify(mockPimLoginListener).onLoginFailed(mockError);
    }

    @Test
    public void testExchangeAuthorizeCodeForMigration() throws Exception {
        String authResponse = "authresponse";
        PIMUserMigrationListener mockMigrationListener = mock(PIMUserMigrationListener.class);
        AuthorizationRequest mockAuthorizationRequest = mock(AuthorizationRequest.class);
        pimLoginManager.exchangeAuthorizationCodeForMigration(mockAuthorizationRequest, authResponse, mockMigrationListener);
        verify(mockAuthManager).performTokenRequest(eq(mockAuthorizationRequest), eq(authResponse), listenerArgumentCaptor.capture());
        PIMTokenRequestListener requestListener = listenerArgumentCaptor.getValue();
        requestListener.onTokenRequestSuccess();
        verify(mockUserManager).requestUserProfile(eq(mockAuthState), userProfileDwnldLstnrCaptor.capture());
        PIMUserProfileDownloadListener downloadListener = userProfileDwnldLstnrCaptor.getValue();
        downloadListener.onUserProfileDownloadSuccess();
        verify(mockMigrationListener).onUserMigrationSuccess();

        Error mockDwonldErrorError = mock(Error.class);
        downloadListener.onUserProfileDownloadFailed(mockDwonldErrorError);
        verify(mockMigrationListener).onUserMigrationFailed(mockDwonldErrorError);

        Error mockError = mock(Error.class);
        requestListener.onTokenRequestFailed(mockError);
        verify(mockMigrationListener).onUserMigrationFailed(mockError);
    }

    @Test
    public void testCreateAdditionalParameterForLogin() throws Exception {
        Object additionalParameterForLogin = Whitebox.invokeMethod(pimLoginManager, "createAdditionalParameterForLogin");
        assertNotNull(additionalParameterForLogin);
    }

    @Test
    public void testCreateAuthReqForMigration() {
        pimLoginManager.createAuthRequestUriForMigration(anyMap());
        verify(mockAuthManager).createAuthRequestUriForMigration(anyMap());
    }

    public void tearDown() throws Exception {
    }
}