package com.philips.platform.pim.rest;

import android.net.Uri;

import junit.framework.TestCase;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.AuthorizationServiceDiscovery;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
public class UserProfileRequestTest extends TestCase {

    private UserProfileRequest userProfileRequest;

    private final String userInfoEndpoint = "https://stg.accounts.philips.com/c2a48310-9715-3beb-895e-000000000000/profiles/oidc/userinfo";

    @Mock
    private AuthState mockAuthState;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        MockitoAnnotations.initMocks(this);

        Uri mockUri = mock(Uri.class);
        AuthorizationResponse mockAuthorizationResponse = mock(AuthorizationResponse.class);
        AuthorizationRequest mockAuthorizationRequest = mock(AuthorizationRequest.class);
        AuthorizationServiceConfiguration mockAuthorizationServiceConfiguration = mock(AuthorizationServiceConfiguration.class);
        AuthorizationServiceDiscovery mockAuthorizationServiceDiscovery = mock(AuthorizationServiceDiscovery.class);

        Mockito.when(mockAuthState.getLastAuthorizationResponse()).thenReturn(mockAuthorizationResponse);
        Whitebox.setInternalState(mockAuthorizationResponse, "request", mockAuthorizationRequest);
        Whitebox.setInternalState(mockAuthorizationRequest, "configuration", mockAuthorizationServiceConfiguration);
        Whitebox.setInternalState(mockAuthorizationServiceConfiguration, "discoveryDoc", mockAuthorizationServiceDiscovery);
        Mockito.when(mockAuthorizationServiceDiscovery.getUserinfoEndpoint()).thenReturn(mockUri);
        when(mockUri.toString()).thenReturn(userInfoEndpoint);

        userProfileRequest = new UserProfileRequest(mockAuthState);
    }

    @Test
    public void testGetUrl() {
        String url = userProfileRequest.getUrl();
        assertSame(url, userInfoEndpoint);
    }

    @Test
    public void testGetHeader() {
        Map<String, String> header = userProfileRequest.getHeader();
        int size = header.size();
        assertEquals(size, 1);
    }

    @Test
    public void testGetBody() {
        String body = userProfileRequest.getBody();
        assertNull(body);
    }

    @Test
    public void testGetMethodType() {
        int methodType = userProfileRequest.getMethodType();
        assertEquals(methodType, PIMRequest.Method.GET);
    }

    @After
    public void tearDown() throws Exception {
    }
}