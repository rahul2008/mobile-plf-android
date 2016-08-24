package com.philips.dhpclient;

import android.test.FlakyTest;
import android.test.InstrumentationTestCase;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/21/2016.
 */
public class DhpAuthenticationManagementClientTest extends InstrumentationTestCase {

    DhpAuthenticationManagementClient mDhpAuthenticationManagementClient;
    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
//        MockitoAnnotations.initMocks(this);
        super.setUp();
        DhpApiClientConfiguration dhpApiClientConfiguration = new DhpApiClientConfiguration("apiBaseUrl","dhpApplicationName","signingKey","signingSecret");
        mDhpAuthenticationManagementClient = new DhpAuthenticationManagementClient(dhpApiClientConfiguration);

    }

    @Test
    public void testDhpAuthenticateManagementClient(){
//        mDhpAuthenticationManagementClient.authenticate("username","password","secret");
        mDhpAuthenticationManagementClient.createRefreshSignature("refresh_Secret","date","accessToken");
        mDhpAuthenticationManagementClient.validateToken("userId","accessToken");
//        mDhpAuthenticationManagementClient.loginSocialProviders("email","socialaccesstoken");
        mDhpAuthenticationManagementClient.logout("sample","sample");
        DhpAuthenticationManagementClient.AuthenticationRequestJson mAuthenticationRequestJson= new DhpAuthenticationManagementClient.AuthenticationRequestJson("loginId","password");


        DhpAuthenticationManagementClient.RefreshTokenRequest mRefreshTokenRequest= new DhpAuthenticationManagementClient.RefreshTokenRequest("refreshToken");

        DhpAuthenticationManagementClient.ResetPasswordRequest mResetPasswordRequest= new DhpAuthenticationManagementClient.ResetPasswordRequest("resetPasswordRequest");

     assertNotNull(mDhpAuthenticationManagementClient);
    }
}