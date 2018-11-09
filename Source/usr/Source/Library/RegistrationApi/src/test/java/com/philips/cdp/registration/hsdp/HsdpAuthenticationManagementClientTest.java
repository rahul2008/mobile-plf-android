package com.philips.cdp.registration.hsdp;

import com.philips.cdp.registration.configuration.HSDPConfiguration;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class HsdpAuthenticationManagementClientTest extends TestCase {
    private HsdpAuthenticationManagementClient mDhpAuthenticationManagementClient;
    @Mock
    HSDPConfiguration hsdpConfiguration;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
//        HSDPConfiguration dhpApiClientConfiguration = new HSDPConfiguration("apiBaseUrl", "dhpApplicationName", "signingKey", "signingSecret");
        Mockito.when(hsdpConfiguration.getHsdpBaseUrl()).thenReturn("apiBaseUrl");
        Mockito.when(hsdpConfiguration.getHsdpAppName()).thenReturn("dhpApplicationName");
        Mockito.when(hsdpConfiguration.getHsdpSecretId()).thenReturn("signingKey");
        Mockito.when(hsdpConfiguration.getHsdpSharedId()).thenReturn("signingSecret");
        mDhpAuthenticationManagementClient = new HsdpAuthenticationManagementClient(hsdpConfiguration);

    }

//    @Test
//    public void testDhpAuthenticateManagementClient() throws Exception {
////        mDhpAuthenticationManagementClient.authenticate("username", "password", "secret");
////        mDhpAuthenticationManagementClient.createRefreshSignature("refresh_Secret", "date", "accessToken");
////        mDhpAuthenticationManagementClient.createRefreshSignature("refresh_Secret", "", "");
////        mDhpAuthenticationManagementClient.validateToken("userId", "accessToken");
////        mDhpAuthenticationManagementClient.validateToken(null, null);
////        mDhpAuthenticationManagementClient.validateToken("", "");
//        mDhpAuthenticationManagementClient.loginSocialProviders("email", "socialaccesstoken", "asjdbwdbwdbejkwfbjkewbwejkdw");
//        mDhpAuthenticationManagementClient.logout("sample", "sample");
//        mDhpAuthenticationManagementClient.logout(null, null);
//        mDhpAuthenticationManagementClient.logout("", "");
//
//        assertNotNull(mDhpAuthenticationManagementClient);
//    }

    @Test
    public void testSign() throws Exception {
        Method method = null;
        String s = "sample";
        Map<String, String> headers = new HashMap<>();
        Map<String, Object> rawResponse = new HashMap<>();
        HsdpResponse dhpResponse = new HsdpResponse(rawResponse);

        method = HsdpAuthenticationManagementClient.class.getDeclaredMethod("getDhpAuthenticationResponse", HsdpResponse.class);
        method.setAccessible(true);
        method.invoke(mDhpAuthenticationManagementClient, dhpResponse);
        dhpResponse = null;
        method.invoke(mDhpAuthenticationManagementClient, dhpResponse);
    }

    @Test
    public void testGetUTCdatetimeAsString() throws Exception {
        Method method = null;
        method = HsdpAuthenticationManagementClient.class.getDeclaredMethod("getUTCdatetimeAsString");
        method.setAccessible(true);
        method.invoke(mDhpAuthenticationManagementClient);
    }
}